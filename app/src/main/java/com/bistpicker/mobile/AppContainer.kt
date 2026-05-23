package com.bistpicker.mobile

import android.content.Context
import com.bistpicker.mobile.data.BistRepository
import com.bistpicker.mobile.data.LocalBistRepository
import com.bistpicker.mobile.data.local.SnapshotDao
import com.bistpicker.mobile.data.local.SnapshotDatabase
import com.bistpicker.mobile.data.api.LivePriceClient
import com.bistpicker.mobile.data.sync.SnapshotApplier
import com.bistpicker.mobile.data.sync.SnapshotFeedClient
import com.bistpicker.mobile.data.sync.SnapshotSyncStateStore
import com.bistpicker.mobile.data.sync.SnapshotSyncWorker
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Manual DI surface. Hilt would be overkill for this app — we have one
 * activity, one persistence layer, no scopes beyond "process".
 *
 * The container is built once per process by [BistPickerApplication] and
 * exposed via [AppContainerProvider]. Snapshot rollover is handled by
 * rebuilding the Room handle internally without invalidating the
 * container, so ViewModels can hold the same repository reference for the
 * lifetime of the activity.
 */
interface AppContainer {
    val repository: BistRepository
    val applier: SnapshotApplier
    val feedClient: SnapshotFeedClient
    val syncState: SnapshotSyncStateStore

    /** Close & rebuild the Room handle after a snapshot file rollover. */
    fun rebuildDatabase()

    /** Read snapshot_metadata.snapshot_date from the freshly built DB. */
    suspend fun metadataSnapshotDate(): String?

    /** Drop the canonical DB and reseed from the bundled asset. */
    fun resetToBundle()

    /** Manifest URL the worker will hit. */
    val manifestUrl: String
}

class DefaultAppContainer(
    private val appContext: Context,
    override val manifestUrl: String,
) : AppContainer {

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .callTimeout(120, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    private val json: Json = Json { ignoreUnknownKeys = true; isLenient = true }

    @Volatile
    private var database: SnapshotDatabase = SnapshotDatabase.build(appContext)

    private fun activeDao(): SnapshotDao = database.snapshotDao()

    override val applier: SnapshotApplier = SnapshotApplier(appContext)

    override val feedClient: SnapshotFeedClient = SnapshotFeedClient(
        httpClient = httpClient,
        manifestUrl = manifestUrl,
        json = json,
    )

    override val syncState: SnapshotSyncStateStore = SnapshotSyncStateStore(appContext)

    private val livePriceClient = LivePriceClient(httpClient, json)

    override val repository: BistRepository = LocalBistRepository(
        context = appContext,
        daoProvider = ::activeDao,
        json = json,
        livePriceClient = livePriceClient,
    )

    @Synchronized
    override fun rebuildDatabase() {
        runCatching { database.close() }
        database = SnapshotDatabase.build(appContext)
        (repository as? LocalBistRepository)?.notifyDatabaseRebuilt()
    }

    override suspend fun metadataSnapshotDate(): String? =
        activeDao().getMetadata()?.snapshotDate

    override fun resetToBundle() {
        runCatching { database.close() }
        // Drop the canonical file (and Room sidecars) so the next call
        // path can reseed from the bundled asset cleanly.
        val canonical = SnapshotDatabase.canonicalDbFile(appContext)
        listOf("", "-wal", "-shm", "-journal").forEach { suffix ->
            java.io.File(canonical.absolutePath + suffix).delete()
        }
        applier.seedFromBundleIfMissing()
        database = SnapshotDatabase.build(appContext)
        (repository as? LocalBistRepository)?.notifyDatabaseRebuilt()
    }

    fun bootstrap() {
        applier.seedFromBundleIfMissing()
        SnapshotSyncWorker.enqueuePeriodic(appContext)
        SnapshotSyncWorker.enqueueOneOff(appContext)
    }
}

object AppContainerProvider {
    private lateinit var instance: AppContainer

    fun set(container: AppContainer) {
        if (::instance.isInitialized) return
        instance = container
    }

    fun get(@Suppress("UNUSED_PARAMETER") context: Context): AppContainer {
        check(::instance.isInitialized) { "AppContainer not initialised" }
        return instance
    }

    fun isReady(): Boolean = ::instance.isInitialized
}

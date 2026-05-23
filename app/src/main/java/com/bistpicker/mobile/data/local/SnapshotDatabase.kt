package com.bistpicker.mobile.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

/**
 * Room sees the snapshot DB as a read-only resource.
 */
@Database(
    version = 12,
    exportSchema = false,
    entities = [
        SnapshotMetadataEntity::class,
        HomeSummaryEntity::class,
        OpenPositionEntity::class,
        PortfolioHistoryEntity::class,
        CompanyEntity::class,
        ScoringLatestEntity::class,
        SectorBenchmarkEntity::class,
        AdjustedMetricsEntity::class,
        PriceHistoryEntity::class,
        FactorHistoryEntity::class,
        ModelPerformanceEntity::class,
    ],
)
abstract class SnapshotDatabase : RoomDatabase() {
    abstract fun snapshotDao(): SnapshotDao

    companion object {
        const val DB_FILENAME = "bist_snapshot.db"

        fun build(context: Context): SnapshotDatabase {
            val dbFile = canonicalDbFile(context)
            return Room.databaseBuilder(
                context.applicationContext,
                SnapshotDatabase::class.java,
                dbFile.absolutePath,
            )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
        }

        fun canonicalDbFile(context: Context): File =
            context.getDatabasePath(DB_FILENAME)
    }
}

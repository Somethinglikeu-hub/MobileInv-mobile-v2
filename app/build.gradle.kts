plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.bistpicker.mobile"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.bistpicker.mobile"
        // v2 starts at versionCode 3 so it cleanly upgrades the v1 0.2.0
        // (versionCode 2) install in place — same package, signed key
        // change required only if the user re-keys.
        minSdk = 28
        targetSdk = 35
        versionCode = 3
        versionName = "2.0.0"

        // Feed URL is configurable via gradle property so a debug build
        // can point at a staging snapshot. Production reads the same URL
        // the v1 APK already uses.
        buildConfigField(
            "String",
            "DEFAULT_MANIFEST_URL",
            "\"https://raw.githubusercontent.com/Somethinglikeu-hub/MobileInv-feed/gh-pages/manifest.json\"",
        )

        // Bump SCHEMA_VERSION_MIN in lockstep with the Python side
        // (mobile_snapshot.SNAPSHOT_SCHEMA_VERSION). v2 of the snapshot
        // adds alpha_x_*/factor_history_quarterly columns the APK consumes.
        buildConfigField("int", "SCHEMA_VERSION_MIN", "2")

        vectorDrawables { useSupportLibrary = true }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf(
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=kotlin.time.ExperimentalTime",
        )
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core + lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)

    // Compose (BOM-aligned)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.extended)
    debugImplementation(libs.compose.ui.tooling)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // WorkManager
    implementation(libs.work.runtime.ktx)

    // DataStore
    implementation(libs.datastore.preferences)

    // Network
    implementation(libs.okhttp)

    // Serialization + coroutines
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.android)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
}

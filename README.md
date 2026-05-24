# BIST Stock Picker Mobile App v2.0 (Android) 📱

This repository contains the mobile client application for the **BIST Stock Picker** system. It is a native Android application built with Kotlin, Jetpack Compose, Room Database, and modern reactive practices.

---

## 🏗️ Architecture & Core Components

The app follows the official Android architecture guidelines:
* **UI Layer:** Jetpack Compose for a premium, responsive user interface with glassmorphic designs, custom gauge graphs, and theme-level stability tags.
* **State Management:** MVVM architecture with Kotlin `Flow` and `StateFlow` to propagate reactive state changes from the database to the UI.
* **Database Layer:** Room Database containing multiple tables mapping BIST companies, historical suggestions, scoring statistics, and macro indicators.
* **Background sync:** `WorkManager` triggers background checks for data updates via `SnapshotSyncWorker`.

---

## 🔄 SQLite & Sync Mechanism

The application is **offline-first**. It works out-of-the-box using a pre-packaged database and automatically synchronizes with the remote feed in the background.

1. **Remote Feed:** The backend pipeline uploads the processed market database to the `MobileInv-feed` GitHub Pages repository as a compressed file (`mobile_snapshot.db.gz`) along with a metadata `manifest.json`.
2. **Local Sync:** On application startup or when clicking **Refresh**, `SnapshotSyncWorker` queries the manifest. If a new version is detected, it:
   * Downloads `mobile_snapshot.db.gz` to a temporary cache.
   * Decompresses it.
   * Shuts down the current database connections.
   * Replaces the database file on disk.
   * Rebuilds the Room database.
   * Dispatches a database rebuild event to UI listeners so that the UI updates dynamically without requiring an app restart.

### ⚠️ Room Database Schema Version (Critical)
* The Android app's Room database version is set to **`12`**.
* **Important:** The SQLite database produced by the Python backend *must* set the SQLite `user_version` header to `12` (`PRAGMA user_version = 12`) before compression. If the version header does not match, Android's Room library will flag a version mismatch, execute a destructive migration, and empty out the tables.

---

## 📈 Yahoo Finance Cookie-Crumb Handshake

To support real-time portfolio returns, the app uses Yahoo Finance for live BIST stock prices (e.g., `THYAO.IS`). To bypass `401 Unauthorized` restrictions, a specialized cookie-crumb handshake client (`LivePriceClient.kt`) is implemented:
1. **Cookie Harvesting:** The app requests `https://fc.yahoo.com` using a desktop User-Agent to collect session cookies (specifically the `A3` cookie).
2. **Crumb Fetching:** Using the harvested cookies, the app queries `https://query1.finance.yahoo.com/v1/test/getcrumb` to retrieve a unique session token (the "crumb").
3. **API Quote Query:** The app queries the real-time quote endpoint using the collected cookies and the `&crumb={crumb}` URL query parameter.
4. **Self-Healing:** In case of a `401` expiration, the handshake cache is invalidated, and a new session is automatically negotiated.
5. **BIST100 Index Ticker (`XU100.IS`):** Note that the standard caret-prefixed Yahoo Finance ticker `^XU100` stopped updating on June 28, 2019. The app queries the active ticker **`XU100.IS`** (which has the correct current price, e.g. `13808.20`) and automatically strips the `.IS` suffix upon receipt to map back to `"XU100"` internally.

---

## 🛠️ Local Development & Debugging

When writing and testing database updates locally:
1. **Redirect Manifest:** Debug builds (`BuildConfig.DEBUG`) are configured to query the local development server at `http://192.168.240.1:8000/manifest.json` (the Waydroid container host gateway) instead of the GitHub Pages repository.
   - *Note:* If using a standard Android Studio AVD emulator, this IP should be changed back to the standard loopback alias `10.0.2.2`.
2. **Launch Local Server:** In the `MobileInv-feed` directory (ensuring you are on the `gh-pages` branch), start the server:
   ```bash
   python -m http.server 8000
   ```
3. **Configure Host Firewall:** Since Waydroid runs in a separate network namespace, the host firewall (e.g. UFW) may block requests to port 8000. Allow incoming traffic from the Waydroid network interface:
   ```bash
   sudo ufw allow in on waydroid0 to any port 8000
   ```
4. **Test Synced Changes:** When the mobile app runs in the simulator, it will pull the database directly from your local HTTP server, allowing you to test pipeline changes instantly without pushing database files to GitHub.

---

## 📦 How to Build the APK

To build the project from this computer or any other computer after downloading it from GitHub:

### Prerequisites
* **Android Studio Ladybug** (or newer)
* **JDK 17** (Ensure `JAVA_HOME` is set to JDK 17)
* **Android SDK** (API level 34+)

### Steps to Build

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Somethinglikeu-hub/MobileInv-mobile-v2.git
   cd MobileInv-mobile-v2
   ```

2. **Verify/Setup local.properties:**
   Android Studio should auto-generate a `local.properties` file with your SDK path. If not, create a file named `local.properties` in the root folder and add your Android SDK path:
   ```properties
   # On Windows:
   sdk.dir=C\:\\Users\\YourUsername\\AppData\\Local\\Android\\Sdk
   # On macOS/Linux:
   sdk.dir=/Users/YourUsername/Library/Android/sdk
   ```

3. **Build Debug APK:**
   To build a debuggable APK for testing:
   ```bash
   ./gradlew assembleDebug
   ```
   The generated APK will be saved at:
   `app/build/outputs/apk/debug/app-debug.apk`

4. **Build Release APK:**
   To build an unsigned release-ready APK:
   ```bash
   ./gradlew assembleRelease
   ```
   The generated APK will be saved at:
   `app/build/outputs/apk/release/app-release-unsigned.apk`

5. **Run Tests:**
   To run unit tests:
   ```bash
   ./gradlew test
   ```

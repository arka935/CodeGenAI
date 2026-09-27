# CodeGenAI - Termux Build Guide

## Prerequisites

Before building the APK in Termux, ensure you have:

- **Termux** installed on your Android device
- At least **2GB of free storage**
- A stable internet connection

## Installation Steps

### Step 1: Update and Install Required Packages

```bash
pkg update
pkg upgrade
pkg install git openjdk-17 android-tools android-sdk
```

### Step 2: Set Environment Variables

Add these to your `~/.bashrc` file:

```bash
export ANDROID_HOME=$PREFIX/opt/android-sdk
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
```

Then reload:

```bash
source ~/.bashrc
```

### Step 3: Accept Android SDK Licenses

```bash
echo -e "\n\ny\n" | sdkmanager --licenses
```

### Step 4: Clone the Repository

```bash
cd ~
git clone https://github.com/arka935/CodeGenAI.git
cd CodeGenAI
```

### Step 5: Build the APK

**Option A: Using the automated build script**

```bash
chmod +x build-apk.sh setup-termux.sh
./setup-termux.sh  # Only needed first time
source ~/.bashrc
./build-apk.sh
```

**Option B: Manual build using Gradle**

```bash
./gradlew clean
./gradlew assembleDebug
```

### Step 6: Locate the APK

After a successful build, your APK will be at:

```
app/build/outputs/apk/debug/app-debug.apk
```

### Step 7: Install or Share the APK

**To install directly on your device:**

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

**To copy to Downloads folder:**

```bash
cp app/build/outputs/apk/debug/app-debug.apk /sdcard/Download/app-debug.apk
```

**To share via file manager:**

```bash
ls -lh app/build/outputs/apk/debug/app-debug.apk
```

## Troubleshooting

### Issue: "ANDROID_HOME is not set"

**Solution:**
```bash
export ANDROID_HOME=$PREFIX/opt/android-sdk
```

### Issue: "No space left on device"

**Solution:**
The build requires significant space. Free up at least 2GB:
```bash
rm -rf app/build
./gradlew clean
```

### Issue: "Java not found"

**Solution:**
```bash
pkg install openjdk-17
```

### Issue: Gradle wrapper fails

**Solution:**
Make gradlew executable:
```bash
chmod +x gradlew
```

## Build Variants

### Debug Build (Faster, for testing)

```bash
./gradlew assembleDebug
```
Output: `app/build/outputs/apk/debug/app-debug.apk`

### Release Build (Optimized, for distribution)

```bash
./gradlew assembleRelease
```
Output: `app/build/outputs/apk/release/app-release-unsigned.apk`

**Note:** Release builds require a signing key. See [Android Signing](https://developer.android.com/studio/publish/app-signing) for details.

## Build Time Estimates

- First build: **5-10 minutes** (downloads dependencies)
- Subsequent builds: **2-3 minutes**
- Clean rebuild: **8-15 minutes**

## File Locations

```
CodeGenAI/
├── app/                           # Main application module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/              # Kotlin/Java source code
│   │   │   ├── res/               # Resources (layouts, drawables)
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   ├── build.gradle               # App-level build config
│   └── build/
│       └── outputs/
│           └── apk/
│               ├── debug/
│               │   └── app-debug.apk
│               └── release/
│                   └── app-release-unsigned.apk
├── gradle/                        # Gradle wrapper files
├── build.gradle                   # Root build config
├── settings.gradle
├── gradlew                        # Gradle wrapper script
├── build-apk.sh                   # Automated build script
└── setup-termux.sh               # Environment setup script
```

## Next Steps

1. **Configure API Keys:**
   - Launch the app
   - Go to Settings
   - Add your AI provider API keys (OpenAI, Gemini, Grok, etc.)

2. **Create Your First Project:**
   - Tap "Start Creating"
   - Select an AI model
   - Describe what you want to build
   - Download the generated code as ZIP

3. **Share Your Creations:**
   - Find generated APKs in `/sdcard/Download/`
   - Share via your favorite method

## Additional Resources

- [Termux Wiki](https://wiki.termux.com/wiki/Main_Page)
- [Android SDK Documentation](https://developer.android.com/docs)
- [Gradle Documentation](https://docs.gradle.org/)
- [OpenAI API](https://platform.openai.com/docs)
- [Google Gemini API](https://ai.google.dev/)

## Support

If you encounter issues:

1. Check the Termux Setup section above
2. Ensure all packages are up to date: `pkg upgrade`
3. Check GitHub Issues: https://github.com/arka935/CodeGenAI/issues
4. Review Android logs: `adb logcat`

---

**Happy Building! 🚀**

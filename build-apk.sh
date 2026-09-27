#!/bin/bash

# CodeGenAI - Build APK using Termux
# This script builds the Android application into an APK file

echo "================================"
echo "CodeGenAI APK Builder for Termux"
echo "================================"
echo ""

# Check if we're in the correct directory
if [ ! -f "settings.gradle" ]; then
    echo "ERROR: settings.gradle not found!"
    echo "Please run this script from the root of the CodeGenAI project."
    exit 1
fi

echo "Step 1: Checking Termux environment..."
if [ -z "$ANDROID_HOME" ]; then
    echo "ERROR: ANDROID_HOME is not set."
    echo "Please set up Android SDK in Termux first."
    echo ""
    echo "Install Android SDK:"
    echo "  pkg install android-sdk"
    echo "  export ANDROID_HOME=\$PREFIX/opt/android-sdk"
    exit 1
fi

echo "✓ ANDROID_HOME is set to: $ANDROID_HOME"
echo ""

echo "Step 2: Checking Java installation..."
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed."
    echo "Install Java:"
    echo "  pkg install openjdk-17"
    exit 1
fi

java_version=$(java -version 2>&1 | head -n 1)
echo "✓ Java is installed: $java_version"
echo ""

echo "Step 3: Building APK..."
echo "This may take several minutes..."
echo ""

# Clean previous builds
echo "Cleaning previous builds..."
./gradlew clean

if [ $? -ne 0 ]; then
    echo "ERROR: Gradle clean failed"
    exit 1
fi

echo ""
echo "Building debug APK..."
./gradlew assembleDebug

if [ $? -ne 0 ]; then
    echo "ERROR: Gradle build failed"
    exit 1
fi

echo ""
echo "================================"
echo "BUILD SUCCESSFUL!"
echo "================================"
echo ""
echo "APK Location:"
echo "  app/build/outputs/apk/debug/app-debug.apk"
echo ""
echo "To install on your device:"
echo "  adb install app/build/outputs/apk/debug/app-debug.apk"
echo ""
echo "Or copy the APK file to share it:"
echo "  cp app/build/outputs/apk/debug/app-debug.apk ~/app-debug.apk"
echo ""

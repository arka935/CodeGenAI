#!/bin/bash

# CodeGenAI - Setup Termux Environment
# This script sets up Termux for Android development

echo "======================================"
echo "CodeGenAI Termux Setup"
echo "======================================"
echo ""

echo "Step 1: Updating Termux packages..."
pkg update -y
pkg upgrade -y

echo ""
echo "Step 2: Installing required packages..."
pkg install -y openjdk-17
pkg install -y android-tools
pkg install -y git
pkg install -y curl

echo ""
echo "Step 3: Setting up Android SDK..."
echo "Installing Android SDK..."
pkg install -y android-sdk

echo ""
echo "Step 4: Configuring environment variables..."
echo 'export ANDROID_HOME=$PREFIX/opt/android-sdk' >> $HOME/.bashrc
echo 'export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools' >> $HOME/.bashrc

echo ""
echo "Step 5: Creating necessary directories..."
mkdir -p $PREFIX/opt/android-sdk/licenses

echo ""
echo "Step 6: Accepting Android SDK licenses..."
echo -e "\n\ny\n" | sdkmanager --licenses

echo ""
echo "======================================"
echo "Setup Complete!"
echo "======================================"
echo ""
echo "Important: Run this command to load environment variables:"
echo "  source ~/.bashrc"
echo ""
echo "Then navigate to your project and run:"
echo "  ./build-apk.sh"
echo ""

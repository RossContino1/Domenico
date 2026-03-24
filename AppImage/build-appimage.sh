#!/bin/bash
set -e

APPDIR="AppDir"

# Clean previous build
rm -rf "$APPDIR"

# Recreate structure
mkdir -p "$APPDIR/usr/bin"
mkdir -p "$APPDIR/usr/lib"
mkdir -p "$APPDIR/usr/share/applications"
mkdir -p "$APPDIR/usr/share/icons/hicolor/256x256/apps"

# Copy app files
cp Domenico.jar "$APPDIR/usr/lib/"
cp -r runtime "$APPDIR/usr/lib/runtime"

# Copy launcher and metadata
cp AppDir/usr/bin/DomenicoLauncher "$APPDIR/usr/bin/"
cp AppDir/AppRun "$APPDIR/"
cp AppDir/domenico.desktop "$APPDIR/"
cp AppDir/domenico.desktop "$APPDIR/usr/share/applications/"
cp domenico.png "$APPDIR/"
cp domenico.png "$APPDIR/usr/share/icons/hicolor/256x256/apps/"

chmod +x "$APPDIR/AppRun"
chmod +x "$APPDIR/usr/bin/DomenicoLauncher"

# Build AppImage
./linuxdeploy-x86_64.AppImage \
  --appdir "$APPDIR" \
  --desktop-file "$APPDIR/usr/share/applications/domenico.desktop" \
  --icon-file "$APPDIR/usr/share/icons/hicolor/256x256/apps/domenico.png" \
  --output appimage

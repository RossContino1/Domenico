#!/bin/bash
set -e

APPIMAGE="Domenico-x86_64.AppImage"
APP_NAME="domenico"

rm -f "$HOME/Applications/$APPIMAGE"
rm -f "$HOME/.local/share/applications/${APP_NAME}.desktop"
rm -f "$HOME/.local/share/icons/hicolor/256x256/apps/${APP_NAME}.png"

echo "Domenico uninstalled."

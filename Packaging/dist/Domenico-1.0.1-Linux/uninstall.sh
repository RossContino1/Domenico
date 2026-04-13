#!/usr/bin/env bash
set -euo pipefail

BIN_DIR="$HOME/.local/bin"
LEGACY_APP_DIR="$HOME/Applications"
APPS_DIR="$HOME/.local/share/applications"
ICON_THEME_DIR="$HOME/.local/share/icons/hicolor/256x256/apps"
PIXMAPS_DIR="$HOME/.local/share/pixmaps"

echo "Removing Domenico installs..."

find "$BIN_DIR" -maxdepth 1 -type f \( -iname 'Domenico*.AppImage' -o -iname 'domenico*.AppImage' \) -print -delete 2>/dev/null || true
find "$LEGACY_APP_DIR" -maxdepth 1 -type f \( -iname 'Domenico*.AppImage' -o -iname 'domenico*.AppImage' \) -print -delete 2>/dev/null || true

rm -f "$APPS_DIR/com.bytesbreadbbq.domenico.desktop"
rm -f "$APPS_DIR/domenico.desktop"
rm -f "$APPS_DIR/domcenico.desktop"

rm -f "$ICON_THEME_DIR/Domenico.png"
rm -f "$PIXMAPS_DIR/Domenico.png"

command -v update-desktop-database >/dev/null 2>&1 && update-desktop-database "$APPS_DIR" || true
command -v gtk-update-icon-cache >/dev/null 2>&1 && gtk-update-icon-cache -f -t "$HOME/.local/share/icons/hicolor" >/dev/null 2>&1 || true

echo
echo "Uninstall complete."
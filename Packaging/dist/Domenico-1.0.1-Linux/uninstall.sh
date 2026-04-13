#!/usr/bin/env bash
set -euo pipefail

INSTALL_DIR="$HOME/.local/bin"
APPS_DIR="$HOME/.local/share/applications"
ICON_DIR="$HOME/.local/share/icons/hicolor/256x256/apps"

rm -f "$INSTALL_DIR"/Domenico*.AppImage "$INSTALL_DIR"/domenico*.AppImage
rm -f "$APPS_DIR/com.bytesbreadbbq.domenico.desktop"
rm -f "$ICON_DIR/Domenico.png"
rm -f "$ICON_DIR/domenico.png"

command -v update-desktop-database >/dev/null 2>&1 && update-desktop-database "$APPS_DIR" || true
command -v gtk-update-icon-cache >/dev/null 2>&1 && gtk-update-icon-cache "$HOME/.local/share/icons/hicolor" >/dev/null 2>&1 || true

echo "Domenico uninstalled."

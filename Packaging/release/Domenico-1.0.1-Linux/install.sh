#!/usr/bin/env bash
set -euo pipefail

APP_NAME="Domenico"
APPIMAGE_NAME="Domenico.AppImage"
ICON_NAME="Domenico.png"
INSTALL_DIR="$HOME/.local/bin"
APPS_DIR="$HOME/.local/share/applications"
ICON_DIR="$HOME/.local/share/icons/hicolor/256x256/apps"
INSTALLED_APPIMAGE="$INSTALL_DIR/$APPIMAGE_NAME"
DESKTOP_FILE="$APPS_DIR/com.bytesbreadbbq.domenico.desktop"
ICON_TARGET="$ICON_DIR/$ICON_NAME"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SOURCE_APPIMAGE="$SCRIPT_DIR/$APPIMAGE_NAME"
SOURCE_ICON="$SCRIPT_DIR/$ICON_NAME"

if [[ ! -f "$SOURCE_APPIMAGE" ]]; then
  echo "Error: $APPIMAGE_NAME not found next to install.sh"
  exit 1
fi

mkdir -p "$INSTALL_DIR" "$APPS_DIR" "$ICON_DIR"

find "$INSTALL_DIR" -maxdepth 1 -type f \( -name 'Domenico*.AppImage' -o -name 'domenico*.AppImage' \) -print -delete || true

cp "$SOURCE_APPIMAGE" "$INSTALLED_APPIMAGE"
chmod +x "$INSTALLED_APPIMAGE"

if [[ -f "$SOURCE_ICON" ]]; then
  cp "$SOURCE_ICON" "$ICON_TARGET"
fi

cat > "$DESKTOP_FILE" <<EOF_DESKTOP
[Desktop Entry]
Type=Application
Name=Domenico
Comment=Create animated GIFs from short video clips
Exec=$INSTALLED_APPIMAGE
Icon=${ICON_TARGET%.*}
Categories=AudioVideo;Video;Graphics;
Terminal=false
StartupNotify=true
EOF_DESKTOP

chmod 644 "$DESKTOP_FILE"

command -v update-desktop-database >/dev/null 2>&1 && update-desktop-database "$APPS_DIR" || true
command -v gtk-update-icon-cache >/dev/null 2>&1 && gtk-update-icon-cache "$HOME/.local/share/icons/hicolor" >/dev/null 2>&1 || true

echo "Domenico installed."
echo "Launcher: $DESKTOP_FILE"
echo "AppImage: $INSTALLED_APPIMAGE"

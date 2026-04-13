#!/usr/bin/env bash
set -euo pipefail

APPIMAGE_NAME="Domenico.AppImage"
ICON_NAME="Domenico.png"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SOURCE_APPIMAGE="$SCRIPT_DIR/$APPIMAGE_NAME"
SOURCE_ICON="$SCRIPT_DIR/$ICON_NAME"

BIN_DIR="$HOME/.local/bin"
LEGACY_APP_DIR="$HOME/Applications"
APPS_DIR="$HOME/.local/share/applications"
ICON_THEME_DIR="$HOME/.local/share/icons/hicolor/256x256/apps"
PIXMAPS_DIR="$HOME/.local/share/pixmaps"

TARGET_APPIMAGE="$BIN_DIR/$APPIMAGE_NAME"
TARGET_THEME_ICON="$ICON_THEME_DIR/$ICON_NAME"
TARGET_PIXMAP_ICON="$PIXMAPS_DIR/$ICON_NAME"

if [[ ! -f "$SOURCE_APPIMAGE" ]]; then
  echo "Error: $SOURCE_APPIMAGE not found"
  exit 1
fi

mkdir -p "$BIN_DIR" "$APPS_DIR" "$ICON_THEME_DIR" "$PIXMAPS_DIR"

echo "Removing old Domenico installs..."

# Remove old/new AppImages from both common locations
find "$BIN_DIR" -maxdepth 1 -type f \( -iname 'Domenico*.AppImage' -o -iname 'domenico*.AppImage' \) -print -delete 2>/dev/null || true
find "$LEGACY_APP_DIR" -maxdepth 1 -type f \( -iname 'Domenico*.AppImage' -o -iname 'domenico*.AppImage' \) -print -delete 2>/dev/null || true

# Remove old/new desktop entries, including legacy typo/name
rm -f "$APPS_DIR/com.bytesbreadbbq.domenico.desktop"
rm -f "$APPS_DIR/domenico.desktop"
rm -f "$APPS_DIR/domcenico.desktop"

# Remove old icons
rm -f "$TARGET_THEME_ICON"
rm -f "$TARGET_PIXMAP_ICON"

echo "Installing new Domenico AppImage..."
cp -f "$SOURCE_APPIMAGE" "$TARGET_APPIMAGE"
chmod 755 "$TARGET_APPIMAGE"

if [[ -f "$SOURCE_ICON" ]]; then
  echo "Installing icon..."
  cp -f "$SOURCE_ICON" "$TARGET_THEME_ICON"
  cp -f "$SOURCE_ICON" "$TARGET_PIXMAP_ICON"
else
  echo "Warning: $SOURCE_ICON not found, icon will be missing."
fi

cat > "$APPS_DIR/domenico.desktop" <<EOF
[Desktop Entry]
Version=1.0
Type=Application
Name=Domenico
Comment=Create animated GIFs from short video clips
Exec=$TARGET_APPIMAGE
Icon=Domenico
Categories=AudioVideo;Video;Graphics;
Terminal=false
StartupNotify=true
EOF

chmod 644 "$APPS_DIR/domenico.desktop"

command -v update-desktop-database >/dev/null 2>&1 && update-desktop-database "$APPS_DIR" || true
command -v gtk-update-icon-cache >/dev/null 2>&1 && gtk-update-icon-cache -f -t "$HOME/.local/share/icons/hicolor" >/dev/null 2>&1 || true

echo
echo "Install complete."
echo "Installed AppImage: $TARGET_APPIMAGE"
echo "Desktop file: $APPS_DIR/domenico.desktop"
ls -l "$TARGET_APPIMAGE"
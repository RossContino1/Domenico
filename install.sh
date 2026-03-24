#!/bin/bash
set -e

APPIMAGE="Domenico-x86_64.AppImage"
APP_NAME="domenico"

if [ ! -f "$APPIMAGE" ]; then
    echo "Could not find $APPIMAGE in this folder."
    exit 1
fi

mkdir -p "$HOME/Applications"
mkdir -p "$HOME/.local/share/applications"
mkdir -p "$HOME/.local/share/icons/hicolor/256x256/apps"

cp "$APPIMAGE" "$HOME/Applications/"
chmod +x "$HOME/Applications/$APPIMAGE"

cp domenico.png "$HOME/.local/share/icons/hicolor/256x256/apps/${APP_NAME}.png"

cat > "$HOME/.local/share/applications/${APP_NAME}.desktop" <<EOF
[Desktop Entry]
Type=Application
Name=Domenico
Comment=Convert videos and create looping GIFs
Exec=$HOME/Applications/$APPIMAGE
Icon=$APP_NAME
Categories=AudioVideo;Video;
Terminal=false
StartupNotify=true
EOF

echo "Domenico installed."
echo "You can launch it from your applications menu."

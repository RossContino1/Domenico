#!/usr/bin/env bash
set -euo pipefail

APP_NAME="Domenico"
APP_ID="com.bytesbreadbbq.domenico"
VERSION="1.0.1"
MAIN_JAR="Domenico.jar"
ICON_FILE="Domenico.png"
APPDIR="AppDir"
DIST_DIR="dist"
RELEASE_BASENAME="${APP_NAME}-${VERSION}-Linux"
RELEASE_DIR="${DIST_DIR}/${RELEASE_BASENAME}"
APPIMAGE_TOOL="./linuxdeploy-x86_64.AppImage"

if [[ ! -f "$MAIN_JAR" ]]; then
  echo "Error: $MAIN_JAR not found in $(pwd)"
  echo "Export your Runnable JAR first, then run this script from the packaging folder."
  exit 1
fi

if [[ ! -f "$ICON_FILE" ]]; then
  echo "Error: $ICON_FILE not found in $(pwd)"
  exit 1
fi

if [[ ! -f "$APPIMAGE_TOOL" ]]; then
  echo "Error: linuxdeploy-x86_64.AppImage not found in $(pwd)"
  echo "Download it and place it next to this script."
  exit 1
fi

if [[ ! -f "install.sh" ]]; then
  echo "Error: install.sh not found in $(pwd)"
  exit 1
fi

if [[ ! -f "uninstall.sh" ]]; then
  echo "Error: uninstall.sh not found in $(pwd)"
  exit 1
fi

if [[ ! -f "README.txt" ]]; then
  echo "Error: README.txt not found in $(pwd)"
  exit 1
fi

chmod +x "$APPIMAGE_TOOL"

# Clean only build artifacts. Do NOT delete linuxdeploy itself.
rm -rf "$APPDIR" "$DIST_DIR"
rm -f ./"${APP_NAME}"-*.AppImage

mkdir -p "$APPDIR/usr/bin"
mkdir -p "$APPDIR/usr/lib/$APP_NAME"
mkdir -p "$APPDIR/usr/share/applications"
mkdir -p "$APPDIR/usr/share/icons/hicolor/256x256/apps"
mkdir -p "$DIST_DIR"
mkdir -p "$RELEASE_DIR"

cp "$MAIN_JAR" "$APPDIR/usr/lib/$APP_NAME/$MAIN_JAR"
cp "$ICON_FILE" "$APPDIR/usr/share/icons/hicolor/256x256/apps/${APP_NAME}.png"

cat > "$APPDIR/AppRun" <<'APP_RUN'
#!/usr/bin/env bash
set -euo pipefail

HERE="$(dirname "$(readlink -f "$0")")"
JAR="$HERE/usr/lib/Domenico/Domenico.jar"

if command -v java >/dev/null 2>&1; then
  exec java -jar "$JAR" "$@"
fi

if [[ -x /usr/bin/java ]]; then
  exec /usr/bin/java -jar "$JAR" "$@"
fi

if command -v xmessage >/dev/null 2>&1; then
  xmessage "Domenico requires Java 17 or newer to be installed on this system."
else
  echo "Domenico requires Java 17 or newer to be installed on this system." >&2
fi

exit 1
APP_RUN
chmod +x "$APPDIR/AppRun"

cat > "$APPDIR/usr/share/applications/${APP_ID}.desktop" <<EOF_DESKTOP
[Desktop Entry]
Type=Application
Name=${APP_NAME}
Comment=Create animated GIFs from short video clips
Exec=AppRun
Icon=${APP_NAME}
Categories=AudioVideo;Video;Graphics;
Terminal=false
StartupNotify=true
EOF_DESKTOP

export ARCH="$(uname -m)"
"$APPIMAGE_TOOL" --appdir "$APPDIR" --output appimage

APPIMAGE_OUTPUT=""
shopt -s nullglob
for f in ./*.AppImage; do
  if [[ "$(basename "$f")" != "$(basename "$APPIMAGE_TOOL")" ]]; then
    APPIMAGE_OUTPUT="$f"
    break
  fi
done
shopt -u nullglob

if [[ -z "$APPIMAGE_OUTPUT" ]]; then
  echo "Error: linuxdeploy did not produce an AppImage."
  exit 1
fi

FINAL_APPIMAGE="$DIST_DIR/${APP_NAME}-${VERSION}-${ARCH}.AppImage"
mv -f "$APPIMAGE_OUTPUT" "$FINAL_APPIMAGE"

cp "$FINAL_APPIMAGE" "$RELEASE_DIR/${APP_NAME}.AppImage"
cp "$MAIN_JAR" "$RELEASE_DIR/"
cp "install.sh" "$RELEASE_DIR/"
cp "uninstall.sh" "$RELEASE_DIR/"
cp "README.txt" "$RELEASE_DIR/"
cp "$ICON_FILE" "$RELEASE_DIR/"

(
  cd "$DIST_DIR"
  rm -f "${RELEASE_BASENAME}.zip"
  zip -r "${RELEASE_BASENAME}.zip" "${RELEASE_BASENAME}"
)

echo
echo "Build complete."
echo "AppImage: $FINAL_APPIMAGE"
echo "Release folder: $RELEASE_DIR"
echo "ZIP: $DIST_DIR/${RELEASE_BASENAME}.zip"
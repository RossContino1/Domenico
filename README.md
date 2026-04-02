# Domenico 🎞️

**Create GIFs from Video — Instantly. No FFmpeg commands required.**
*Linux-first, with optional cross-platform JAR support.*
![GitHub release](https://img.shields.io/github/v/release/RossContino1/Domenico)
![License](https://img.shields.io/github/license/RossContino1/Domenico)
![Platform](https://img.shields.io/badge/platform-Linux-blue)

Domenico is a simple FFmpeg GUI for creating high-quality looping GIFs from MP4 and MOV video files.

---

<p align="center">
  <img src="assets/PrepVideo.gif" height="400"/>
</p>

---

## 📥 Download

<p align="center">
  <a href="https://github.com/RossContino1/Domenico/releases/latest">
    <img src="https://img.shields.io/badge/Download-AppImage-blue?style=for-the-badge&logo=linux"/>
  </a>
  <a href="https://github.com/RossContino1/Domenico/releases/latest">
    <img src="https://img.shields.io/badge/Download-JAR-orange?style=for-the-badge&logo=java"/>
  </a>
</p>
Choose **AppImage (Linux)** or **JAR (Windows/macOS/Linux)** from the release assets.

👉 **Latest release (AppImage + JAR):**
https://github.com/RossContino1/Domenico/releases/latest

👉 **Project page:**
https://bytesbreadbbq.com/domenico/

---

## 🚀 Features

* 🎬 Convert video clips to animated GIFs
* ⚡ Optimized for short clips (typically under ~10 seconds for best results)
* 🖱 Simple GUI — no command-line knowledge needed
* 🔁 Perfect for tutorials, demos, and social media
* 🐧 Built for Linux creators

---

## 🧠 Why Domenico?

Creating GIFs with FFmpeg is powerful — but remembering the right commands can be frustrating.

Domenico removes that barrier.

Just select your video, choose your clip, and click convert.

No FFmpeg experience required — Domenico handles everything behind the scenes.

---

## 🎯 Use Cases

* GitHub README animations
* YouTube Shorts previews
* App demonstrations
* Social media content
* Quick tutorials

---

## ⚙️ Requirements

* **FFmpeg must be installed on your system**

Domenico does not bundle FFmpeg — it uses your system installation.

* FFmpeg must be installed and accessible from your system PATH (`ffmpeg` command works in terminal)

---

## 📦 Installation

### 🐧 Linux (Recommended)

Download the AppImage from the link above.

Make it executable and run:

```bash
chmod +x Domenico.AppImage
./Domenico.AppImage
```
⚠️ Having Trouble Launching?

If the AppImage doesn’t start, your system may be missing FUSE.

Fix (quick):

**Fedora:**

sudo dnf install fuse fuse-libs

**Ubuntu / Mint:**

sudo apt install libfuse2

**Arch:**

sudo pacman -S fuse2

👉 After installing, try launching again.
---

### ☕ Portable JAR (Advanced / Cross-Platform)

Run using:

```bash
java -jar Domenico.jar
```

#### Requirements:

* Java 17 or newer installed
* FFmpeg installed and available in system PATH

#### Notes:

* No installer — runs as a standalone application
* Double-click may not work on all systems (run from terminal if needed)
* Performance and behavior may vary depending on OS and Java setup

---

## 🐧 Linux Desktop Integration (Optional)

You can install Domenico into your system menu using:

```bash
./install.sh
```

To remove:

```bash
./uninstall.sh
```

Supports KDE, GNOME, Cinnamon, and other desktop environments.

---

## 🛠 Built With

* Java (Swing)
* FFmpeg (external dependency)

---

## 💡 Tips for Best Results

- Keep clips under **10 seconds** for optimal GIF size and performance
- Shorter clips load faster and loop more smoothly
- Ideal for GitHub, social media, and quick demos

---

## 🤝 Contributing

Contributions, ideas, and feedback are welcome!

## ☕ Support Domenico

Domenico is free to use. If it saves you time (or brisket), consider supporting development:

[![Support via PayPal](https://img.shields.io/badge/Support-PayPal-blue?style=for-the-badge&logo=paypal)](https://www.paypal.com/donate/?hosted_button_id=XS9MXN5AE5P3S)

Your support helps keep the code crispy and the files smokin’ hot.

Feel free to open an issue or submit a pull request.

---

## 📜 License

This project is licensed under the MIT License.

---

## ⭐ Support the Project

If you find Domenico useful:

* ⭐ Star the repo
* 🔁 Share it with others
* 🧠 Use it in your projects

---

## 🔗 Related Projects

* **Leonardo** — Convert video for DaVinci Resolve on Linux
* **RepoRover** — Update all Linux package managers in one tool

---

## 💡 Final Note

Domenico is built to make powerful FFmpeg workflows accessible to everyone —
especially creators who just want results without memorizing commands.


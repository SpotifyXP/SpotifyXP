# 🎵 SpotifyXP

<p style="text-align: center;">
  <a href="https://github.com/SpotifyXP/SpotifyXP">
    <img src="https://raw.githubusercontent.com/SpotifyXP/SpotifyXP/main/src/main/resources/spotifyxp.png" alt="SpotifyXP Logo" width="100" height="100">
  </a>
</p>

<p style="text-align: center;">
  <b>A real Spotify client for Windows XP</b>
  <br/><br/>
  🔹 <a href="https://github.com/SpotifyXP/SpotifyXP/wiki">SpotifyXP Wiki</a>  
  🔹 <a href="https://github.com/SpotifyXP/SpotifyXP/issues">Report a Bug</a>  
  🔹 <a href="https://github.com/SpotifyXP/SpotifyXP/issues">Request a Feature</a>
</p>

![Contributors](https://img.shields.io/github/contributors/SpotifyXP/SpotifyXP?color=dark-green) ![Issues](https://img.shields.io/github/issues/SpotifyXP/SpotifyXP)

## 📌 Table of Contents

- [📖 About the Project](#-about-the-project)
- [📚 The SpotifyXP Wiki](#-the-spotifyxp-wiki)
- [⚙️ System Requirements](#-system-requirements)
- [🌍 Translating](#-translating)
- [🔧 Built With](#-built-with)
- [🚀 Getting Started](#-getting-started)
- [🎶 Usage](#-usage)
- [🔑 New Login Methods](#-new-login-methods)
- [🛠️ Compiling](#-compiling)
- [🤝 Contributing](#-contributing)
- [📜 License](#-license)
- [👨‍💻 Authors](#-authors)
- [🙏 Special Thanks](#-special-thanks)

## 📖 About the Project

**SpotifyXP** was developed to restore Spotify support on Windows XP after the official service was discontinued in December 2022. Thanks to this application, users can continue to enjoy Spotify on legacy systems.

### 🖥️ Stable Version Screenshot
![Screen Shot](SpotifyXPShowStable.png)

## 📚 The SpotifyXP Wiki

For assistance and troubleshooting, check the [SpotifyXP Wiki](https://github.com/SpotifyXP/SpotifyXP/wiki) before reporting an issue.

## ⚙️ System Requirements

### 🔹 Recommended
- **OS:** Windows XP or later
- **Processor:** Any CPU capable of running SNES emulators
- **RAM:** 512MB
- **Storage Requirements:**
    - Without cache: 70MB
    - With cache: 700MB

### 🔹 Minimum
- **OS:** Windows 98 (with KernelEx)
- **Processor:** Any CPU capable of running SNES emulators
- **RAM:** 256MB
- **Storage Requirements:**
    - Without cache: 70MB
    - With cache: 700MB

## 🌍 Translating

To contribute a translation:
1. Modify `src/main/resources/lang/skeleton.json`
2. Ensure you run:
   ```bash
   python3 i18nhelper.py -skeleton
   ```
3. Rename `skeleton.json` to the corresponding language code (e.g., `en.json` for English).

## 🔧 Built With

SpotifyXP utilizes various third-party libraries. The complete list is available [here](https://github.com/SpotifyXP/SpotifyXP/blob/main/src/main/resources/setup/thirdparty.html).

## 🚀 Getting Started

### 📌 Prerequisites
- **VLC Media Player** version 3.0.20 or later
- **Java 8 Update 151** (Ensure VLC matches the architecture of Java: x64 or x86)

### 📌 Installation
1. Download the latest version from the [Actions tab](https://github.com/SpotifyXP/SpotifyXP/actions)
2. Run `SpotifyXP.jar` by double-clicking or via terminal:
   ```bash
   java -jar SpotifyXP.jar
   ```

## 🎶 Usage

Follow the **Getting Started** section, then select a login method from **New Login Methods**, and enjoy your music.

## 🔑 New Login Methods

1. **Zeroconf**: Open a modern Spotify client and select SpotifyXP under available devices for automatic authentication. *(Requires a Spotify Premium account)*
2. **OAuth**: Log in via the default browser window that opens. *(Requires a modern browser with HTML5 support)*

## 🛠️ Compiling

1. Clone the repository with `--recursive`
2. Run `init.py`
3. Run `build.py`
4. The executable file will be `target/SpotifyXP.jar`

## 🤝 Contributing

If you would like to contribute, submit a pull request with your modifications.

### 📌 Creating a Pull Request
1. Fork the project
2. Make the necessary changes and commit them (`git commit -m 'Added AmazingFeature'`)
3. Push the changes (`git push origin main`)
4. Open a pull request

## 📜 License

© 2025 Gianluca Beil - Licensed under Apache 2.0. See the [LICENSE](http://www.apache.org/licenses/LICENSE-2.0) file for details.

## 👨‍💻 Authors

- [Werwolf2303](https://github.com/Werwolf2303/)

## 🙏 Special Thanks

- [StapleBacon5037](https://github.com/StapleBacon5037) - For improving the documentation
- [skippster1337](https://github.com/skipster1337) - For listing SpotifyXP on his website
- [Jri-creator](https://github.com/Jri-creator) - For the new setup and documentation improvements

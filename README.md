<p align="center">
  <a href="https://github.com/SpotifyXP/SpotifyXP">
    <img src="https://raw.githubusercontent.com/SpotifyXP/SpotifyXP/main/src/main/resources/spotifyxp.png" alt="Logo" width="80" height="80">
  </a>
<h3 align="center">SpotifyXP</h3>
 <p align="center">
    A real Spotify Player for Windows XP
    <br/>
<center><a href="https://www.buymeacoffee.com/werwolf2303" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png" alt="Buy Me A Coffee" style="height: 41px !important;width: 174px !important;box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;-webkit-box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;" ></a></center>
    <br/>
    <a href="https://github.com/SpotifyXP/SpotifyXP/issues">Report Bug</a>
    .
    <a href="https://github.com/SpotifyXP/SpotifyXP/issues">Request Feature</a>
  </p>
</p>

![Downloads](https://img.shields.io/github/downloads/SpotifyXP/SpotifyXP/total) ![Contributors](https://img.shields.io/github/contributors/SpotifyXP/SpotifyXP?color=dark-green) ![Issues](https://img.shields.io/github/issues/SpotifyXP/SpotifyXP)

## Table Of Contents

* [About the Project](#about-the-project)
* [System Requirements](#system-requirements)
* [Translating](#translating)
* [Built With](#built-with)
* [Getting Started](#getting-started)
* [Usage](#usage)
* [SpotifyXP stuck on "Creating API..."?](#stuck-on-"creating-api..."?)
* [Contributing](#contributing)
* [License](#license)
* [Authors](#authors)
* [Special thanks](#Special-thanks)

## About The Project

Stable version
![Screen Shot](SpotifyXPShowStable.png)


I developed SpotifyXP for my old daily driver that runs Windows XP. My daily driver is now an MacBook Pro (2022)

<h5>Why use SpotifyXP:</h5>

On december 2022 Spotify shutdown it's last version for Windows XP so this application is the only way to listen to Spotify

## System Requirements

<h4>Recommended</h4>

* OS: Windows XP or above
* Processor: Anything that can handle SNES Emulation
* RAM: 512MB
* Storage (Without cache): 60MB
* Storage (With cache): My folder is 700MB

<h4>Minimum</h4>

* OS: Windows 98 (KernelEx)
* Processor: Anything that can handle SNES Emulation
* RAM: 256MB
* Storage (Without cache): 60MB
* Storage (With cache): My folder is 700MB

## Translating

<p>If you want to translate this project look into src/main/resources/lang/skeleton.json</p>
<p>Make sure you have run 'python3 i18nhelper.py -skeleton' to make sure the skeleton is up to date</p>
<p>Rename skeleton.json to [2DigitLanguageCode].json</p>


## Built With

SpotifyXP is build with
<br><h4><a href="https://github.com/SpotifyXP/SpotifyXP/blob/main/src/main/resources/setup/thirdparty.html">Thirdparty.html</a></h4>

## Getting Started

### Prerequisites:

- VLC Media Player version 3.0.20 (Stable versions)
- Java 8 151 (Click ok on the dialog)

### ***!! Important !!***
- VLC needs to be the same architecture as the java version e.g. **x64** or **x86**

### Install instructions

1. Download the Stable version under Releases or download the nightly version under the Actions tab
2. Double click **SpotifyXP.jar** or run this in the directory where **SpotifyXP.jar** is downloaded: <pre>java -jar SpotifyXP.jar</pre>

## Usage

Follow the steps under 'Getting Started' and then login with your Email and Password. Then just listen to music

## Stuck on "Creating API..."?

First, Delete all contents of SpotifyXP, which is the SpotifyXP folder
Second, re-install SpotifyXP. You may need to re-download the **SpotifyXP.jar** file. (#install)
After re-installing SpotifyXP, you should now be able to listen to music.

## Compiling

Just run the 'build.bat' inside the root of the repository. It will print some useful information

## Contributing

Just make your desired changes and open a pull request

### Creating A Pull Request

1. Fork the Project
2. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
3. Push to the Branch (`git push origin main`)
4. Open a Pull Request

## License

Copyright [2024] [Gianluca Beil]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

## Authors

* **Werwolf2303** - ** - [Werwolf2303](https://github.com/Werwolf2303/) - **

## Special thanks

* [StapleBacon5037](https://github.com/StapleBacon5037) - For improving the readme
* [skippster1337](https://github.com/skipster1337) - For listing SpotifyXP on his site
* <a href="https://www.yourkit.com/"><img src="https://www.yourkit.com/images/yklogo.png" height="20"></a> that provided a free license for their [Java Profiler](https://www.yourkit.com/java/profiler/)

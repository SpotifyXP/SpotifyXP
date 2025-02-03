<p align="center">
  <a href="https://github.com/SpotifyXP/SpotifyXP">
    <img src="https://raw.githubusercontent.com/SpotifyXP/SpotifyXP/main/src/main/resources/spotifyxp.png" alt="Logo" width="80" height="80">
  </a>
<h3 align="center">SpotifyXP</h3>
 <p align="center">
    A real Spotify Player for Windows XP
    <br/>
    <br/>
    <a href="https://github.com/SpotifyXP/SpotifyXP/wiki">SpotifyXP Wiki</a>
    .
    <a href="https://github.com/SpotifyXP/SpotifyXP/issues">Report Bug</a>
    .
    <a href="https://github.com/SpotifyXP/SpotifyXP/issues">Request Feature</a>
  </p>
</p>

![Contributors](https://img.shields.io/github/contributors/SpotifyXP/SpotifyXP?color=dark-green) ![Issues](https://img.shields.io/github/issues/SpotifyXP/SpotifyXP)

<!--![Downloads](https://img.shields.io/github/downloads/SpotifyXP/SpotifyXP/total)-->

## Table Of Contents

* [About the Project](#about-the-project)
* [The SpotifyXP Wiki](#the-spotifyxp-wiki)
* [System Requirements](#system-requirements)
* [Translating](#translating)
* [Built With](#built-with)
* [Getting Started](#getting-started)
* [Usage](#usage)
* [New Login Methods](#new-login-methods)
* [Contributing](#contributing)
* [License](#license)
* [Authors](#authors)
* [Special thanks](#Special-thanks)

## About The Project

Stable version
![Screen Shot](SpotifyXPShowStable.png)


I developed SpotifyXP for my old daily driver that runs Windows XP. My daily driver is now an MacBook Pro (2022)

<h3>Why use SpotifyXP:</h3>

On December 2022, Spotify shutdown it's last version for Windows XP, making this application the only way to listen to Spotify.

## The SpotifyXP Wiki

The SpotifyXP Wiki is available to help fix any bugs or problems that may be found when using SpotifyXP.
When you have an issue, check the Wiki before creating an Issue to see if that issue had already been resolved.


## System Requirements

<h4>Recommended</h4>

* OS: Windows XP or above
* Processor: Anything that can handle SNES Emulation
* RAM: 512MB
* Storage (Without cache): 70MB
* Storage (With cache): My folder is 700MB

<h4>Minimum</h4>

* OS: Windows 98 (KernelEx)
* Processor: Anything that can handle SNES Emulation
* RAM: 256MB
* Storage (Without cache): 70MB
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

1. Download the nightly version under the Actions tab
2. Double click **SpotifyXP.jar** or run this in the directory where **SpotifyXP.jar** is downloaded: <pre>java -jar SpotifyXP.jar</pre>

## Usage

Follow the instructions under the header **Getting Started**. Then, select one of the options under the header **New Login Methods**. 
Then, enjoy the music.

## New login methods
1. Zeroconf: open a modern Spotify client and select SpotifyXP under devices and SpotifyXP should authenticate with Spotify. **Works only with Spotify Premium account**.
2. OAuth: in the default browser's window which will open automatically, login with your credentials on Spotify website. Then confirm connecting SpotifyXP to your Spotify account and close the browser. **You need to have a modern browser that supports HTML5**.


## Compiling

1. Git clone with '--recursive'
2. Run the init.py
3. Run the build.py
4. Executable is target/SpotifyXP.jar

## Contributing

Just make your desired changes and open a pull request

### Creating A Pull Request

1. Fork the Project
2. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
3. Push to the Branch (`git push origin main`)
4. Open a Pull Request

## License

Copyright [2025] [Gianluca Beil]

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

* [Werwolf2303](https://github.com/Werwolf2303/) 

## Special thanks

* [StapleBacon5037](https://github.com/StapleBacon5037) - For improving the readme
* [skippster1337](https://github.com/skipster1337) - For listing SpotifyXP on his site
* [Jri-creator](https://github.com/Jri-creator) - For the new setup image and improving the readme

IF NOT EXIST "%cd%/scripts/bin/" MKDIR "%cd%/scripts/bin"
IF %1 == "--refresh-nightly" "%cd%/scripts/utils/reporefresh.bat"
IF %1 == "--build" "%cd%/scripts/build.bat"
IF %1 == "--buildandrun" "%cd%/scripts/buildandrun.bat"
echo SpotifyXP Build Script v1.0
echo.
echo build.bat [OPTION] [ADDITIONAL]
echo.
echo Additional
echo.
echo --debug               Enable debugging
echo.
echo Options
echo.
echo --refresh-nightly     Downloads the newest changes
echo --build               Builds SpotifyXP
echo --buildandrun         Builds SpotifyXP and runs it
EXIT /b
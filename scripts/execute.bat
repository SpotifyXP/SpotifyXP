IF "%1" == "--refresh-nightly" scripts/utils/reporefresh.bat
IF "%1" == "--build" scripts/build.bat
echo SpotifyXP Build Script v1.0
echo.
echo build.bat [OPTION] [ADDITIONAL]
echo.
echo Additional
echo.
echo --debug               Enable debugging
echo.
echo.
echo Options
echo.
echo --refresh-nightly     Downloads the newest changes
echo --build               Builds SpotifyXP
EXIT /b
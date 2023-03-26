CALL :color 0A "Build successfull! You can find it in the 'build' folder"
DEL X
IF %1 == "run" "%cd%/scripts/bin/jdk/bin/java.exe" -jar "%cd%/build/SpotifyXP.jar"
EXIT /B

:color
set "param=^%~2" !
set "param=!param:"=\"!"
findstr /p /A:%1 "." "!param!\..\X" nul
<nul set /p ".=%DEL%%DEL%%DEL%%DEL%%DEL%%DEL%%DEL%"
exit /b
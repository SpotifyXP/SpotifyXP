echo Building SpotifyXP-Updater
cd "Modules\SpotifyXP-Updater"
IF %1 == "--debug" CALL "..\..\scripts\bin\maven\bin\mvn" clean package
IF NOT %1 == "--debug" CALL "..\..\scripts\bin\maven\bin\mvn" -q clean package
copy "target\Updater-Stable.jar" "..\..\src\main\resources\SpotifyXP-Updater.jar" > nul
cd "..\..\"
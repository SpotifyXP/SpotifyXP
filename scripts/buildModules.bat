set BEFORE = "%cd%"

del "%BEFORE%\src\main\resources\SpotifyXP-Updater.jar"

cd "%cd%\Modules\SpotifyXP-Updater"
"%BEFORE%\scripts\bin\maven\bin\mvn" clean package
copy /y "target\Updater-Stable.jar"
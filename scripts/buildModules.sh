#Only SpotifyXP-Updater at the moment

BEFORE=$(pwd)
rm "$BEFORE/src/main/resources/SpotifyXP-Updater.jar"

cd "$(pwd)/Modules/SpotifyXP-Updater"
mvn clean package
cp "target/Updater-Stable.jar" "$BEFORE/src/main/resources/SpotifyXP-Updater.jar"
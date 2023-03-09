@echo off

cd deps/librespot-java

echo Building librespot-java

::call mvn clean package

cd ../spotify-web-api-java

echo Building spotify-web-api-java

::call mvn clean package

cd ../../

call mvn clean package -Dfile=deps/librespot-java/player/target/librespot-player-1.6.3-SNAPSHOT.jar
echo "\033[0;32mBuild successfull! You can find it in the 'build' folder\033[0m"
if [ "$1" = "execute" ]; then java -jar "$(pwd)/build/SpotifyXP.jar"; fi;
exit
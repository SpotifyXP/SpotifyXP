sh "$(pwd)/scripts/getModules.sh"
sh "$(pwd)/scripts/buildModules.sh"

echo "\033[0;36mBuilding SpotifyXP\033[0m"
echo ""
echo "Please wait..."

sh "$(pwd)/scripts/utils/clean.sh"

if [ "$2" == "--debug" ]; then mvn clean package; fi
if [ ! "$2" == "--debug" ]; then mvn -q clean package; fi

mkdir build

if [ "$1" = "--buildandrun" ];
then
  if [ -f "$(pwd)/target/SpotifyXP.jar" ];
  then mv "$(pwd)/target/SpotifyXP.jar" "$(pwd)/build"; "$(pwd)/scripts/utils/success.sh" "execute";
  exit;
  fi;
else
  if [ -f "$(pwd)/target/SpotifyXP.jar" ];
  then mv "$(pwd)/target/SpotifyXP.jar" "$(pwd)/build"; "$(pwd)/scripts/utils/success.sh";
  exit;
  fi;
fi

"$(pwd)/scripts/utils/failed.sh"



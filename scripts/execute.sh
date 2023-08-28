if [ "$1" = "--refresh-nightly" ]; then "$(pwd)/scripts/utils/reporefresh.sh"; exit; fi
if [ "$1" = "--build" ]; then "$(pwd)/scripts/build.sh"; exit; fi
if [ "$1" = "--buildandrun" ]; then "$(pwd)/scripts/buildandrun.sh"; exit; fi
echo "SpotifyXP Build Script v1.0"
echo ""
echo "build.sh [OPTION] [ADDITIONAL]"
echo ""
echo "Additional"
echo ""
echo "--debug               Enable debugging"
echo ""
echo "Options"
echo ""
echo "--refresh-nightly     Downloads the newest changes"
echo "--build               Builds SpotifyXP"
echo "--buildandrun         Builds SpotifyXP and runs it"
exit
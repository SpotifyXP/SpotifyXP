echo "Executing sudo to allow execution"
sudo chmod -R +x "$(pwd)/scripts"
sh scripts/execute.sh "$1" "$2"
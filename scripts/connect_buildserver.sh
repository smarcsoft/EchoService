SCRIPTDIR=`dirname "$0"`
AZUREDIR=$SCRIPTDIR/../azure
KEYS=$HOME/keys/smarcsoft.pem

#checking for key existence
if [ ! -f "$KEYS" ]; then
    echo "Setting up connection keys..."
    . setup.sh
fi

echo "Starting Azure build server..."
az vm start --resource-group grpc --name buildserver
BUILDSERVER_IP=$(az vm show -d --resource-group grpc --name buildserver --query "publicIps" -o tsv)
echo "Connecting to Azure with build server IP $BUILDSERVER_IP..."
ssh -i $KEYS smarcsoft@$BUILDSERVER_IP

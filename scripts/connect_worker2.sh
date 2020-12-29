SCRIPTDIR=`dirname "$0"`
AZUREDIR=$SCRIPTDIR/../azure
KEYS=$HOME/keys/smarcsoft.pem

#checking for key existence
if [ ! -f "$KEYS" ]; then
    echo "Setting up connection keys..."
    . setup.sh
fi

echo "Starting Azure build worker..."
az vm start --resource-group grpc --name runworker2
SERVER_IP=$(az vm show -d --resource-group grpc --name runworker2 --query "publicIps" -o tsv)
echo "Connecting to Azure with build server IP $SERVER_IP..."
ssh -o StrictHostKeyChecking=no -i $KEYS smarcsoft@$SERVER_IP

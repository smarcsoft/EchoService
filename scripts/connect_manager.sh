SCRIPTDIR=`dirname "$0"`
AZUREDIR=$SCRIPTDIR/../azure
KEYS=$HOME/keys/smarcsoft.pem

#checking for key existence
if [ ! -f "$KEYS" ]; then
    echo "Setting up connection keys..."
    . setup.sh
fi

NAME=manager

echo "Starting Azure docker swarm manager..."
az vm start --resource-group grpc --name $NAME
SERVER_IP=$(az vm show -d --resource-group grpc --name $NAME --query "publicIps" -o tsv)
echo "Connecting to Azure with build server IP $SERVER_IP..."
ssh -o StrictHostKeyChecking=no -i $KEYS smarcsoft@$SERVER_IP

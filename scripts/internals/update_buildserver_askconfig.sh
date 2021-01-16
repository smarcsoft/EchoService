SCRIPTDIR=`dirname "$0"`
AZUREDIR=$SCRIPTDIR/../azure
KEYS=$HOME/keys/smarcsoft.pem

#checking for key existence
if [ ! -f "$KEYS" ]; then
    echo "Setting up connection keys..."
    . setup.sh
fi

BUILDSERVER_IP=$(az vm show -d --resource-group grpc --name buildserver --query "publicIps" -o tsv)
echo "Setting up AKS credentials on build server $BUILDSERVER_IP..."
ssh -i $KEYS -o "StrictHostKeyChecking no" jenkins@$BUILDSERVER_IP "az aks get-credentials --resource-group sebkube --name seb-cluster --overwrite-existing"


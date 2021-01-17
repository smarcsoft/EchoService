./internals/create_buildserver.sh
KEYS=$HOME/keys/smarcsoft.pem
BUILDSERVER_IP=$(az vm show -d --resource-group grpc --name buildserver --query "publicIps" -o tsv)
echo "Connecting to Azure with build server IP $BUILDSERVER_IP..."
ssh -o "StrictHostKeyChecking no" -i $KEYS smarcsoft@$BUILDSERVER_IP

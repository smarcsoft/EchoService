KEYS=$HOME/keys/smarcsoft.pem
echo "Updating cluster with latest image version"
echo "Connecting to manager..."
SERVER_IP=$(az vm show -d --resource-group grpc --name manager --query "publicIps" -o tsv)
echo "Connecting to Azure with server IP $SERVER_IP..."
ssh -o StrictHostKeyChecking=no -i $KEYS smarcsoft@$SERVER_IP "cd src/EchoService;git pull;scripts/jenkins/perform_update.sh" 


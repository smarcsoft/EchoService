#usage: azcopy servername file
echo -n "Getting destination server IP..."
SERVER_IP=$(az vm show -d --resource-group grpc --name $1 --query "publicIps" -o tsv)
echo $SERVER_IP
echo -n "Starting copy..."
scp -i ~/.ssh/smarcsoft.pem $2 smarcsoft@$SERVER_IP:$2
echo "Done"

SCRIPTDIR=`dirname "$0"`
AZUREDIR=$SCRIPTDIR/../../azure
KEYS=$AZUREDIR/keys/smarcsoft.pem

#checking for key existence
if [ ! -f "$KEYS" ]; then
    echo "Missing connection keys..."
    exit -1
fi


echo "Starting Azure docker swarm manager..."
az vm start --resource-group grpc --name manager
if [ $? -ne 0 ]; then
    echo "Could not start Azure VM manager!"
    exit -1
fi

echo "Starting Azure docker swarm worker 1..."
az vm start --resource-group grpc --name runworker1
if [ $? -ne 0 ]; then
    echo "Could not start Azure VM worker #1!"
    exit -1
fi

echo "Starting Azure docker swarm worker 2..."
az vm start --resource-group grpc --name runworker2
if [ $? -ne 0 ]; then
    echo "Could not start Azure VM worker #2!"
    exit -1
fi

echo "Cluster started successfully"

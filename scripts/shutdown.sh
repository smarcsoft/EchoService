echo "Shutting down docker infrastructure..."
echo "Shutting down manager..."
az vm deallocate --resource-group grpc --name manager
echo "Shutting down workers..."
az vm deallocate --resource-group grpc --name runworker1
az vm deallocate --resource-group grpc --name runworker2
echo "Shutting down build server..."
az vm deallocate --resource-group grpc --name buildserver

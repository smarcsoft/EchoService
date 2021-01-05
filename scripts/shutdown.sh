echo "Shutting down azure infrastructure..."
echo "Shutting down build server..."
az vm deallocate --resource-group grpc --name buildserver
echo "Removing AKS deployment..."
kubectl delete deployments echoserver
kubectl delete services echoserver
echo "Deleting the cluster itself..."
az aks delete --name seb-cluster --resource-group sebkube --yes

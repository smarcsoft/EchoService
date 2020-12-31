echo "Creating resource group..."
az group create --name=sebkube --location=westeurope
echo "Creating AKS cluster..."
az aks create --resource-group=sebkube --name=seb-cluster
echo "Getting cluster credentials..."
az aks get-credentials --resource-group=sebkube --name=seb-cluster



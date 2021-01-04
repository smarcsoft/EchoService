echo "Updating cluster with latest image version"
echo -n "  Getting latest deployment artifact..."
rm -rf EchoService
git clone https://github.com/smarcsoft/EchoService
cp EchoService/scripts/aks/echo-deployment.yaml $HOME/kube
rm -rf EchoService
echo "done."
echo "Applying deployment..."
kubectl apply -f $HOME/kube/echo-deployment.yaml


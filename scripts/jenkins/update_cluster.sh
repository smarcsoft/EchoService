echo "Updating cluster with latest image version"
echo -n "  Getting latest deployment artifact..."
rm -rf EchoService
rm -rf $HOME/kube
mkdir $HOME/kube
git clone https://github.com/smarcsoft/EchoService
cp EchoService/scripts/aks/echo-deployment.yaml $HOME/kube
rm -rf EchoService
echo "done."
echo "Applying deployment..."
export ECHO_VERSION=$1
kubectl apply -f $HOME/kube/echo-deployment.yaml


#Usage: update_cluster.sh VERSION BUILD
echo "Updating cluster with latest image version"
echo -n "  Getting latest deployment artifact..."
rm -rf EchoService
rm -rf $HOME/kube
mkdir $HOME/kube
git clone https://github.com/smarcsoft/EchoService
cp EchoService/scripts/aks/echo-deployment-template.yaml $HOME/kube
rm -rf EchoService
echo "done."
echo "Applying deployment..."
cat "$HOME/kube/echo-deployment-template.yaml" | sed "s/{ECHO_VERSION}/$1/g" | sed "s/{BUILD_VERSION}/$2/g">$HOME/kube/echo-deployment.yaml
export ECHO_VERSION=$1
kubectl apply -f $HOME/kube/echo-deployment.yaml


VERSION=v2.1.2
echo "Deploying version $VERSION"
./internals/create_cluster.sh
./internals/create_buildserver.sh
./internals/update_buildserver_askconfig.sh
./internals/install_linkerd_oncluster.sh
./deploy_service.sh $VERSION
echo "Deploying flagger..."
kubectl apply -k github.com/weaveworks/flagger/kustomize/linkerd

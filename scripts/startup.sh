VERSION=v2.1.5 #Note v2.1.6 has 10% fault and should not be promoted by Flagger once the grpc metric is working
echo "Deploying version $VERSION"
./internals/create_cluster.sh
./internals/create_buildserver.sh
./internals/update_buildserver_askconfig.sh
./internals/install_linkerd_oncluster.sh
./deploy_service.sh $VERSION
echo "Deploying flagger..."
kubectl apply -k github.com/weaveworks/flagger/kustomize/linkerd
kubectl apply -f aks/grpc-metric.yaml
kubectl apply -f aks/echo-canary.yaml

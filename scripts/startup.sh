VERSION=2.1.2
echo "Deploying version $VERSION"
./create_cluster.sh
./create_buildserver.sh
./update_buildserver_askconfig.sh
./install_linkerd_oncluster.sh
./deploy_service.sh $VERSION

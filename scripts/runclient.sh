#!/bin/bash
print_help() {
    echo "runclient.sh [-l | -a {-c <seonds>]"
    echo "  -l to run the client to target a local server"
    echo "  -a to run the client to target the AKS Kubernetes cluster"
    echo "  -c run CPU for <seconds> seconds"
    exit 1
}


# Runs the client to access the service deployed on the kubernetes cluster
# The IP address is the IP address of the cluster
echo "Running client: "
#Getting the IP address of the service load balancer
IP=127.0.0.1
CPU=0
CPUJOB=0
while getopts ="ac:j:hl" opt; do
    case $opt in
	l) IP=127.0.0.1 ;;
	a) IP=$(kubectl get services echoserver -o=jsonpath='{.status.loadBalancer.ingress[0].ip}') ;;
	c) CPU=${OPTARG} ;;
    j) CPUJOB=${OPTARG} ;;
	h) print_help ;;
    esac
done
echo "Contacting server at $IP..."
docker run --network host smarcsoft/echoclient:latest com.smarcsoft.EchoClient echo hello $IP:50051
if [ $CPU -gt 0 ]
then
  docker run --network host smarcsoft/echoclient:latest com.smarcsoft.EchoClient cpu $CPU $IP:50051
fi
if [ $CPUJOB -gt 0 ]
then
  docker run --network host smarcsoft/echoclient:latest com.smarcsoft.EchoClient cpujob $CPU $IP:50051
fi
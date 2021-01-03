#!/bin/bash
print_help() {
    echo "runclient.sh [-l | -a]"
    echo "  -l to run the client to target a local server"
    echo "  -a to run the client to target the AKS Kubernetes cluster"
    exit 1
}


# Runs the client to access the service deployed on the kubernetes cluster
# The IP address is the IP address of the cluster
echo "Running client: "
#Getting the IP address of the service load balancer
IP=127.0.0.1
while getopts ="ahl" opt; do
    case $opt in
	l) IP=127.0.0.1 ;;
	a) IP=$(kubectl get services echoserver -o=jsonpath='{.status.loadBalancer.ingress[0].ip}') ;;
	h) print_help ;;
    esac
done
echo "Contacting server at $IP..."
docker run --network host smarcsoft/echoclient:latest com.smarcsoft.EchoClient echo hello $IP:50051
docker run --network host smarcsoft/echoclient:latest com.smarcsoft.EchoClient cpu 30 $IP:50051

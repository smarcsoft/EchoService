#!/bin/bash
print_help() {
    echo "runclient.sh [-l | -a ] [-c <seconds>] [-b <batch_size>] [-t <threads>]"
    echo "  -l to run the client to target a local server"
    echo "  -a to run the client to target the AKS Kubernetes cluster"
    echo "  -c run CPU for <seconds> seconds"
    echo "  -j run CPU batch job for <seconds> seconds"
    echo "  -b [batch_size] number of requests send to the service"
    echo ""
    echo "Examples:"
    echo "./runclient.sh -a -c 10 -b 15: runs a batch of 15 cpu requests each taking 10 seconds."
    exit 1
}


# Runs the client to access the service deployed on the kubernetes cluster
# The IP address is the IP address of the cluster
echo "Running client: "
#Getting the IP address of the service load balancer
IP=127.0.0.1
CPU=0
CPUJOB=0
REQUESTS=1
THREADS=1
while getopts ="ab:c:t:j:hl" opt; do
    case $opt in
	l) IP=127.0.0.1 ;;
	a) IP=$(kubectl get services echoserver -o=jsonpath='{.status.loadBalancer.ingress[0].ip}') ;;
	c) CPU=${OPTARG} ;;
	j) CPUJOB=${OPTARG} ;;
	b) REQUESTS=${OPTARG} ;;
        t) THREADS=${OPTARG} ;;
	h) print_help ;;
    esac
done
echo "Contacting server at $IP..."
docker run --network host sebmarc/echoclient:latest com.smarcsoft.EchoClient echo hello batch $REQUESTS thread $THREADS $IP:50051
if [ $CPU -gt 0 ]
then
  docker run --network host sebmarc/echoclient:latest com.smarcsoft.EchoClient cpu $CPU batch $REQUESTS thread $THREADS $IP:50051
fi
if [ $CPUJOB -gt 0 ]
then
  docker run --network host sebmarc/echoclient:latest com.smarcsoft.EchoClient cpujob $CPU batch $REQUESTS thread $THREADS $IP:50051
fi

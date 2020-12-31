# Runs the client to access the service deployed on the kubernetes cluster
# The IP address is the IP address of the cluster
echo "Running client: "
#Getting the P address of the service load balancer
IP=$(kubectl get services echoserver -o=jsonpath='{.status.loadBalancer.ingress[0].ip}')
docker container run --network host sebmarc/echoclient:latest seb $IP:50051

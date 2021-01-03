# Runs the client to access the service deployed on the kubernetes cluster
# The IP address is the IP address of the cluster
echo "Running client: "
#Getting the P address of the service load balancer
IP=$(kubectl get services echoserver -o=jsonpath='{.status.loadBalancer.ingress[0].ip}')
docker run --network host smarcsoft/echoclient:latest com.smarcsoft.EchoClient echo hello $IP:50051
docker run --network host smarcsoft/echoclient:latest com.smarcsoft.EchoClient cpu 30 $IP:50051

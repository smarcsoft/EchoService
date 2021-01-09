# Runs the client to access the service deployed on the kubernetes cluster
# The IP address is the IP address of the cluster
echo "Running local server: "
docker container run --network host sebmarc/echoserver:$1

echo "Creating network..."
docker network create --driver=overlay --attachable echonet
echo "Creating and deploying the stack..."
docker stack deploy -c echostack.yml echostack
echo "Listing deployed services..."
docker stack services echostack

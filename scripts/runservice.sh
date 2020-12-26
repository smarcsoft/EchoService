echo "Creating and deploying the stack..."
docker stack deploy -c echostack.yml echostack
echo "Listing deployed services..."
docker stack services echostack

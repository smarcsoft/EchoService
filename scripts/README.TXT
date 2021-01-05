. setup.sh should only be ran once. It sets up the connection keys
. startup.sh creates the AKS cluster and deploy the services on it
. connect_buildserver.sh is used to connect to the Azure build server.
. shutdown.sh shuts down everything to reduce costs

To run the service on the manager:
----------------------------------
deploy-service.sh

To run the client:
------------------
runclient.sh

To run the server locally (not using AKS):
------------------------------------------
runserver.sh

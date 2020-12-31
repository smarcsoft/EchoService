. setup.sh should only be ran once. It sets up the connection keys
. connect_buildserver.sh is used to connect to the Azure build server.
. create_cluster.sh creates the kubernetes cluster (run it if not already created).

To run the service on the manager:
----------------------------------
deploy-service.sh

On the workers:
---------------
docker swarm join ...


To run the client:
------------------
runclient.sh



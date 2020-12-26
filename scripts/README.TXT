. setup.sh should only be ran once. It sets up the connection keys
. connect_buildserver.sh is used to connect to the Azure build server.
. connect_manager.sg is used to connect to the manager
. connect_worker1.sh is used to connect to the runworker number 1
. connect_worker1.sh is used to connect to the runworker number 2


To run the service on the manager:
----------------------------------
runservice.sh


If i want to run the service manually on the manager (without using docker swarm):
If not already done, enter swarm mode
swam init

We need to create the network overlay
. docker network create --driver overlay --attachable echo-net
Then we need to create the service on top of the network overlay
. docker service create --name echoserver --network echo-net --publish 50051:50051 --replicas 3 sebmarc/echoservice:v1.01


On the workers:
---------------
docker swarm join ...


To run the client:
------------------
runclient.sh



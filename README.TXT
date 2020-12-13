The objective of this project is to deploy on Azure:
. A docker image containing the grpc server on server A
. Run a docker image containing the grpc client on server B

To start the client (and the service if the service is not running): run "docker-compose up" from the client repository
To start the server only, run "docker-compose up" from the server repository.

To connect to the various Azure servers (build server, docker swarm manager, docker swarm worker 1, docker swarm worker 2), go the the scripts directory and use the scripts accordingly.

This is a sample grpc service.
It is being created as a docker container and exposed as a service on azure.

The next evolution of this service will be the creation of a service mesh on Azure.

To compile:
-----------
mvn package

To build the docker container of the server:
--------------------------------------------
docker build -t echoservice .

Seb.

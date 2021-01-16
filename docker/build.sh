#Script to build the docker containers of client and server
#Example: ./build.sh v2.1.3
./buildclient.sh $1
./buildserver.sh $1

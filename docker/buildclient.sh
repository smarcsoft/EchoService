#Script to build the docker container of the grpc server client
#Example: ./buildclient.sh v2.1.3
cat "client/Dockerfile" | sed "s/\${VERSION}/$1/g" >client/Dockerfile_with_ver
docker build -f client/Dockerfile_with_ver -t sebmarc/echoclient:latest .
rm client/Dockerfile_with_ver

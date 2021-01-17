#Script to build the docker container of the grpc server
#Example: ./buildserver.sh v2.1.3
#Temporality copy the AKS credentials to copy them on the image
echo "Building server version $1"
cp ~/.kube/config ../build
#Make sure we have the right version in the Dockerfile
cat "server/Dockerfile" | sed "s/\${VERSION}/$1/g" >server/Dockerfile_with_ver
docker build --build-arg VERSION=$1 -f server/Dockerfile_with_ver -t sebmarc/echoserver:$1 -t sebmarc/echoserver:latest ../build
rm ../build/config
rm server/Dockerfile_with_ver

#Temporality copy the AKS credentials to copy them on the image
cp ~/.kube/config ../build
docker build --build-arg VERSION=$1 -f server/Dockerfile -t sebmarc/echoserver:$1 ../build
rm ../build/config

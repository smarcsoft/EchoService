#Temporality copy the AKS credentials to copy them on the image
echo "Building server version $1"
cp ~/.kube/config ../build
docker build --build-arg VERSION=$1 -f server/Dockerfile -t sebmarc/echoserver:$1 ../build
rm ../build/config

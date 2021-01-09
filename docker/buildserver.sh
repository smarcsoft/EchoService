#Temporality copy the AKS credentials to copy them on the image
cp ~/.kube/config ../build
docker build -f server/Dockerfile -t sebmarc/echoserver:$1 ../build
rm ../build/config

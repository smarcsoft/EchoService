echo "Running deployment..."
BUILD_VERSION_STRING="Manually deployed on `date`"
cat "aks/echo-deployment-template.yaml" | sed "s/{ECHO_VERSION}/$1/g" | sed "s/{BUILD_VERSION}/$BUILD_VERSION_STRING/g" >aks/echo-deployment.yaml
kubectl apply -f aks/echo-deployment.yaml
rm aks/echo-deployment.yaml
echo "Creating the service"
kubectl apply -f aks/echo-service.yaml
kubectl apply -f aks/echo-autoscaling.yaml
kubectl get services -o wide

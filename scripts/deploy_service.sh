echo "Running deployment..."
kubectl apply -f aks/echo-deployment.yaml
echo "Creating the service"
kubectl apply -f aks/echo-service.yaml
kubectl apply -f aks/echo-scaler.yaml
kubectl get services -o wide

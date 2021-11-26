minikube start --driver=virtualbox --kubernetes-version=v1.20.2 --cpus 4 --memory "8192mb" --network-plugin=cni
eval $(minikube -p minikube docker-env)
kubectl create secret generic sqlstore-credentials --from-literal=username=infinispan --from-literal=password=secret
kubectl apply deploy/postgres.yaml

#--> deploy and run infinispan, with the postgresql driver inside

cd retail-catalogue
./mvnw clean package -Dquarkus.kubernetes.deploy=true -DskipTests=true
cd ..
cd inmemory-catalogue
./mvnw clean package -Dquarkus.kubernetes.deploy=true -DskipTests=true
cd ..

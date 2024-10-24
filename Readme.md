# Weather Microservice

## This Weather Microservice is built Using SpringBoot . It aims to provide users with accurate and insightful weather information for the next 3 days. Utilizes Open Weather API to get accurate weather prediction for any city, processes it, and return the forecast along with additional conditions like rain, high winds, and thunderstorms.


## Swagger Doc
![Screenshot 2024-01-23 at 2.15.20 PM.png](Screenshot%202024-01-23%20at%202.15.20%E2%80%AFPM.png)
![Screenshot 2024-01-23 at 1.56.55 PM.png](Screenshot%202024-01-23%20at%201.56.55%E2%80%AFPM.png)
![Screenshot 2024-01-23 at 1.57.51 PM.png](Screenshot%202024-01-23%20at%201.57.51%E2%80%AFPM.png)

## Deployment

This Repo Contain Jenkins Script which run build, test, create docker image, push to docker hub and deploy on minikube kubernetes cluster


## Jenkins Dashboard
![Screenshot 2024-01-23 at 1.55.09 PM.png](Screenshot%202024-01-23%20at%201.55.09%E2%80%AFPM.png)


## Minikube (Kubernetes) : Deployment, Service and Pod
![Screenshot 2024-01-23 at 2.09.57 PM.png](Screenshot%202024-01-23%20at%202.09.57%E2%80%AFPM.png)


## Code Coverage

![Screenshot 2024-01-23 at 2.21.46 PM.png](Screenshot%202024-01-23%20at%202.21.46%E2%80%AFPM.png)

## API Used
OpenWeather API https://openweathermap.org/



## Commands Used

**Start Jenkins server**

brew services start jenkins-lts

**ReStart Jenkins server**

brew services restart jenkins-lts

**Stop Jenkins server**

brew services stop jenkins-lts

**start minikube**

minikube start  

**Get nodes**

kubectl get nodes

**Get pods**

kubectl get pods

**Delete pod**

kubectl delete pod <pod_name>

**Get cluster ip**

minikube ip

**Get all services**

minikube service --all

**stop minikube**

minikube stop

**Delete minikube cluster**

minikube delete

**Access Service**
get service ip and node port

http://<minikube-ip>:<node-port>

**Get Deployment**
kubectl get deployments 

**Get Replicaset**
kubectl get replicasets

**Get service**

kubectl get services <service_name>

**To check log of service**

kubectl logs <pod_name>

**List all service**

kubectl get svc

**Get url**
minikube service <service-name> --url
minikube service weather-service-svc --url

**Accessing from kubernetes cluster**
**PUT  secrets OPENWEATHERMAP_API_KEY in secrets**

kubectl create secret generic weather-api-credentials \
--from-literal=OPENWEATHERMAP_API_KEY=<put_key_here>  
--namespace=default --dry-run=client -o yaml | kubectl apply -f -
secret/weather-api-credentials created
zsh: command not found: --namespace=default

**Access Secret**

kubectl get secret weather-api-credentials -o yaml

Deploy application using jenkins

**Port forward to access it**
kubectl port-forward -n <namespace-name> pods/<pod-name> <source_pod>:<destination_pod>
kubectl port-forward -n default pods/<pod-name>8080:8080

**Run locally**
export OPENWEATHERMAP_API_KEY=<api-key>
./mvnw spring-boot:run

**To start tunnel to service**
minikube service weather-service-svc


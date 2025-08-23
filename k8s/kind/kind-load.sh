docker pull mongo:7.0.5
docker pull mysql:8.3.0
docker pull confluentinc/cp-zookeeper:7.5.0
docker pull confluentinc/cp-kafka:7.5.0
docker pull confluentinc/cp-schema-registry:7.5.0
docker pull provectuslabs/kafka-ui:latest
docker pull mysql:8
docker pull quay.io/keycloak/keycloak:24.0.1
docker pull grafana/loki:main
docker pull prom/prometheus:v2.46.0
docker pull grafana/tempo:2.2.2
docker pull grafana/grafana:10.1.0
docker pull salindadocker/project-api-gateway:latest
docker pull salindadocker/project-product-service:latest
docker pull salindadocker/project-order-service:latest
docker pull salindadocker/project-inventory-service:latest
docker pull salindadocker/project-notification-service:latest
docker pull salindadocker/project-frontend:latest
docker pull registry.k8s.io/ingress-nginx/controller:v1.13.1
docker pull registry.k8s.io/ingress-nginx/kube-webhook-certgen:v1.6.0


kind load docker-image -n microservices-cloud-project mongo:7.0.5
kind load docker-image -n microservices-cloud-project mysql:8.3.0
kind load docker-image -n microservices-cloud-project confluentinc/cp-zookeeper:7.5.0
kind load docker-image -n microservices-cloud-project confluentinc/cp-kafka:7.5.0
kind load docker-image -n microservices-cloud-project confluentinc/cp-schema-registry:7.5.0
kind load docker-image -n microservices-cloud-project provectuslabs/kafka-ui:latest
kind load docker-image -n microservices-cloud-project mysql:8
kind load docker-image -n microservices-cloud-project quay.io/keycloak/keycloak:24.0.1
kind load docker-image -n microservices-cloud-project grafana/loki:main
kind load docker-image -n microservices-cloud-project prom/prometheus:v2.46.0
kind load docker-image -n microservices-cloud-project grafana/tempo:2.2.2
kind load docker-image -n microservices-cloud-project grafana/grafana:10.1.0
# Load into Kind
kind load docker-image --name microservices-cloud-project registry.k8s.io/ingress-nginx/controller:v1.13.1
kind load docker-image --name microservices-cloud-project registry.k8s.io/ingress-nginx/kube-webhook-certgen:v1.6.0

kubectl get ns ingress-nginx || kubectl create namespace ingress-nginx
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml
kubectl get secret -n ingress-nginx ingress-nginx-admission -o yaml >/tmp/webhook-secret.yaml

kind load docker-image -n microservices-cloud-project salindadocker/project-api-gateway:latest
kind load docker-image -n microservices-cloud-project salindadocker/project-product-service:latest
kind load docker-image -n microservices-cloud-project salindadocker/project-order-service:latest
kind load docker-image -n microservices-cloud-project salindadocker/project-inventory-service:latest
kind load docker-image -n microservices-cloud-project salindadocker/project-notification-service:latest
kind load docker-image -n microservices-cloud-project salindadocker/project-frontend:latest
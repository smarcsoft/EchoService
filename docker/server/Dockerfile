FROM maven as build
WORKDIR /build
COPY src/src src
COPY src/pom.xml .
ADD src/config config
RUN mvn package
FROM adoptopenjdk:11-jre-hotspot as deploy
WORKDIR /app
COPY --from=build /build/target/echo-1.1.0-jar-with-dependencies.jar /app
COPY --from=build /build/config/logging.properties /app

#Install kubeclt to be able to spawn processes
RUN curl -LO "https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl"
RUN chmod +x ./kubectl
RUN mv ./kubectl /usr/local/bin/kubectl
#Now install my configuration to access AKS
RUN mkdir /root/.kube
COPY config /root/.kube

ENTRYPOINT [ "java", "-Djava.util.logging.config.file=/app/logging.properties -DECHO_VERSION=${VERSION}", "-jar", "/app/echo-1.1.0-jar-with-dependencies.jar"]
VOLUME /home/docker_volume/logs
EXPOSE 50051

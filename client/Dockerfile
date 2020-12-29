FROM maven as build
WORKDIR /build
#First build the server
RUN git clone https://github.com/smarcsoft/EchoService
WORKDIR /build/EchoService/server/src
RUN mvn install

#Then build the client
WORKDIR /build
COPY src src
COPY pom.xml .
RUN mvn package
FROM adoptopenjdk:11-jre-hotspot as deploy
WORKDIR /app
COPY --from=build /build/target/grpcclient-1.1.0-jar-with-dependencies.jar /app
ENTRYPOINT [ "java", "-jar", "/app/grpcclient-1.1.0-jar-with-dependencies.jar" ]
CMD ["name" , "echoserver:50051"]

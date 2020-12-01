FROM maven as build
WORKDIR /build
COPY src src
COPY pom.xml .
COPY target/grpcserver-1.0.0.jar .
#Adding server jar file as a local maven dependency
RUN mvn install:install-file -Dfile=grpcserver-1.0.0.jar -DgroupId=com.smarcsoft.grpc -DartifactId=grpcserver -Dversion=1.0.0 -Dpackaging=jar
RUN mvn package
FROM adoptopenjdk:11-jre-hotspot as deploy
WORKDIR /app
COPY --from=build /build/target/grpcclient-1.0.0-jar-with-dependencies.jar /app
ENTRYPOINT [ "java", "-jar", "/app/grpcclient-1.0.0-jar-with-dependencies.jar", "name" , "echoserver:50051"]

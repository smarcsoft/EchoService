FROM smarcsoft/echoserver:latest
ENTRYPOINT [ "java", "-Djava.util.logging.config.file=/app/logging.properties", "-cp", "/app/echo-1.1.0-jar-with-dependencies.jar"]
CMD ["com.smarcsoft.EchoClient", "echo", "hello world" , "echoserver:50051"]

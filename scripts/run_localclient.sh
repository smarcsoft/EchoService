#!/bin/bash
while getopts ="ec" opt; do
    case $opt in
	e) java -Djava.util.logging.config.file=../build/src/config/logging.properties -cp ../build/src/target/echo-1.1.0-jar-with-dependencies.jar com.smarcsoft.EchoClient echo hello localhost:50051 ;;
	c) java -Djava.util.logging.config.file=../build/src/config/logging.properties -cp ../build/src/target/echo-1.1.0-jar-with-dependencies.jar com.smarcsoft.EchoClient cpu 30 localhost:50051 ;;
    esac
done



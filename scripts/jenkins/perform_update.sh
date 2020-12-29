docker service update --image $(docker inspect --type image --format '{{index .RepoDigests 0}}' sebmarc/echoservice:latest) echostack_echoserver

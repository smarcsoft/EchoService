This is directory contains instructions to containerize client and server

To build the docker container of the server (in server directory):
------------------------------------------------------------------
docker build -t sebmarc/echoservice:latest .

To push it to the docker repo:
------------------------------
docker push sebmarc/echoservice:latest

Seb.

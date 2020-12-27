To build the docker container of the server:
--------------------------------------------
docker build -t echoclient .

To puch the image to docker hub:
docker login -u sebmarc -p Nicolas01
docker tag echoclient sebmarc/echoclient:1.0
docker push sebmarc/echoclient:1.0

Notes:
Take a look at the Dockerfile. It build the client and then contruct the image from the results of the build.
 

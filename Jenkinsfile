pipeline {
	 this is an error
    agent { docker { image 'maven:3.3.3' } }
    stages {
	stage('build') {
	    steps {
                sh 'docker build -t sebmarc/echoservice:latest server/src/Dockerfile'
            }
        }
    }
}
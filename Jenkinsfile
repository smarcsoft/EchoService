pipeline {
    agent any
    stages {
	stage('build') {
	    steps {
                sh 'docker build -t sebmarc/echoservice:latest server/src/'
            }
        }
    }
}
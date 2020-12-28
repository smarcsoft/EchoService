pipeline 
{
  agent any
  stages {
	stage('build') {
      steps {
          echo 'Building image...'
          sh 'docker build -t sebmarc/echoservice:latest server/src/'
      }
    }
    stage('test') {
        steps {
            echo 'Testing.... TODO'
        }
    }
    stage('pushdockerhub') {
        steps {
            echo 'Pushing to docker hub repository...'
            input "Confirm docker repository update ?"
            sh 'docker push sebmarc/echoservice:latest'
        }
    }
  }
}
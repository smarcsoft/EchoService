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
            echo 'Testing: Starting echo service'
            dir("server")
            {
                sh 'docker-compose up -d'
            }
            dir('client')
            {
                echo 'Testing: Running the client'
                sh 'docker-compose up'
            }
            dir('server')
            {
                echo 'Testing: Shutting down service'
                sh 'docker-compose down'
            }
        }
    }
    stage('pushdockerhub') {
        steps {
            echo 'Pushing to docker hub repository...'
            sh 'docker push sebmarc/echoservice:latest'
        }
    }
  }
}
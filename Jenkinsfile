pipeline 
{
  agent any
  stages {
	stage('build') {
      steps {
          echo 'Building server image...'
          sh 'docker build -t sebmarc/echoservice:latest server/src/'
          echo 'Building client image'
          sh 'docker build -t sebmarc/echoclient:latest client/ --no-cache'
      }
    }
    stage('test') {
        steps {
            echo 'Testing: Starting echo service'
            dir("server")
            {
                sh 'docker-compose up -d'
                sh 'sleep 5'
            }
            dir('client')
            {
                echo 'Testing: Running the client'
                sh 'docker-compose up --force-recreate'
            }
        }
        post { 
            always { 
             echo 'cleaning up...'
             dir('server')
             {
                 echo 'Testing: Shutting down service'
                 sh 'docker-compose down'
             }
            }
        }
    }
    stage('pushdockerhub') {
        steps {
            echo 'Pushing to docker hub repository...'
            sh 'docker push sebmarc/echoservice:latest'
            sh 'docker push sebmarc/echoclient:latest'
        }
    }
    stage('deploycluster') {
        steps {
            echo 'Deploying to swarm cluster...'
            echo 'Starting cluster up...'
            dir('scripts/jenkins')
            {
                sh './start_cluster.sh'
                sh './update_cluster.sh'
            }
        }
    }
  }
}
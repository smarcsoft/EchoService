pipeline 
{
  agent any
  stages {
	stage('build') {
      steps {
          dir('docker')
          {
            echo 'Building server image...'
            sh './buildserver.sh'
            echo 'Building client image'
            sh './buildclient.sh'
         }
      }
    }
    stage('test') {
        steps {
            echo 'Testing: Starting echo service'
            dir("scripts")
            {
                sh 'timeout 15s ./runserver.sh'
                sh 'sleep 5'
                sh './runclient.sh'
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
            echo 'Deploying to aks cluster...'
            dir('scripts/jenkins')
            {
                sh './update_cluster.sh'
            }
        }
  }
}
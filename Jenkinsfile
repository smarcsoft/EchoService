pipeline 
{
  agent any
  environment { 
        VERSION = 'v2.2.1'
        $VERSION_STRING=${env.VERSION}_build:${env.BUILD_NUMBER}
    }
  stages {
	stage('build') {
      steps {
          dir('docker')
          {
            echo 'Building server image version ${env.VERSION_STRING}...'
            sh './buildserver.sh ${env.VERSION_STRING}'
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
                sh 'timeout 15s ./runserver.sh ${env.VERSION_STRING}&'
                sh 'sleep 5'
                sh './runclient.sh -l'
            }
        }

    }
    stage('pushdockerhub') {
        steps {
            echo 'Pushing to docker hub repository...'
            sh 'docker push sebmarc/echoserver:${env.VERSION_STRING}'
            sh 'docker push sebmarc/echoclient:latest'
        }
    }
    stage('deploycluster') {
        steps {
            echo 'Deploying to aks cluster...'
            dir('scripts/jenkins')
            {
                sh './update_cluster.sh ${env.VERSION_STRING}'
            }
        }
    }
  }
}
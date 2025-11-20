pipeline {
  agent any

  stages {
    stage('Checkout') {
      steps {
        git url: 'https://github.com/deepthipulicherla/ValidateDB.git', branch: 'main'
      }
    }
    stage('Run Tests in Docker Compose') {
      agent {
        docker {
          image 'depul/maven-docker:latest'
          args '-v /var/run/docker.sock:/var/run/docker.sock -u root'
        }
      }
      steps {
        sh 'docker-compose down -v || true'
        sh 'docker-compose -p $(echo $BUILD_TAG | tr "[:upper:]" "[:lower:]") up --abort-on-container-exit --build'
      }
      post {
        always {
          sh 'docker-compose -p $(echo $BUILD_TAG | tr "[:upper:]" "[:lower:]") down -v || true'
        }
      }
    }
  }

  post {
    always {
      script {
        if (fileExists('target/surefire-reports')) {
          junit 'target/surefire-reports/*.xml'
        }
      }
      archiveArtifacts artifacts: 'allure-results/**', fingerprint: true
    }
  }
}

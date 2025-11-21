pipeline {
  agent any

  stages {
    stage('Run Tests in Docker Compose') {
      steps {
        // Clean up any leftovers
        sh 'docker-compose down -v || true'

        // Run tests with lowercase project name
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

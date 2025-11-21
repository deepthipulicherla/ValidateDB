pipeline {
  agent any

  stages {
    stage('Verify Allure Installation') {
      steps {
        // Sanity check to confirm Allure is available
        sh 'allure --version'
      }
    }

    stage('Run Tests in Docker Compose') {
      steps {
        // Clean up any leftovers
        sh 'docker-compose down -v || true'

        // Run tests with lowercase project name
        sh 'docker-compose -p $(echo $BUILD_TAG | tr "[:upper:]" "[:lower:]") up --abort-on-container-exit --build'
      }
      post {
        always {
          // Ensure containers are stopped/removed even if tests fail
          sh 'docker-compose -p $(echo $BUILD_TAG | tr "[:upper:]" "[:lower:]") down -v || true'
        }
      }
    }
  }

  post {
    always {
      script {
        // Publish JUnit results if they exist
        if (fileExists('target/surefire-reports')) {
          junit 'target/surefire-reports/*.xml'
        }
      }

      // Archive raw Allure results for debugging
      archiveArtifacts artifacts: 'target/allure-results/**', fingerprint: true

      // Generate Allure report inside Jenkins
      allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
    }
  }
}

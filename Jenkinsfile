pipeline {
  agent any

  stages {
    stage('Verify Allure Installation') {
      steps {
        sh 'allure --version'
      }
    }

    stage('Run Tests in Docker Compose') {
      steps {
        sh 'docker-compose down -v || true'
        sh '''
          docker-compose \
            -p $(echo $BUILD_TAG | tr "[:upper:]" "[:lower:]") \
            up --abort-on-container-exit --build
        '''
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
      archiveArtifacts artifacts: 'target/allure-results/**', fingerprint: true
      allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
    }
  }
}

pipeline {
  agent any

  options {
    // Avoid default SCM checkout twice
    skipDefaultCheckout(true)
    // Fail faster if a stage hangs
    timeout(time: 30, unit: 'MINUTES')
  }

  stages {
    stage('Checkout') {
      steps {
        checkout([
          $class: 'GitSCM',
          branches: [[name: '*/main']],
          userRemoteConfigs: [[url: 'https://github.com/deepthipulicherla/ValidateDB.git']],
          extensions: [[$class: 'CloneOption', depth: 1, noTags: true, shallow: true]]
        ])
      }
    }

    stage('Verify Allure Installation') {
      steps {
        sh 'allure --version'
      }
    }

    stage('Run Tests in Docker Compose') {
      steps {
        // Clean up any leftover containers/volumes
        sh 'docker-compose down --remove-orphans -v || true'
        // Run Compose with unique project name
        sh '''
          docker-compose \
            -p $(echo $BUILD_TAG | tr "[:upper:]" "[:lower:]") \
            up --abort-on-container-exit --build
        '''
      }
      post {
        always {
          sh 'docker-compose -p $(echo $BUILD_TAG | tr "[:upper:]" "[:lower:]") down --remove-orphans -v || true'
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
      // Retry non-resumable steps in case Jenkins restarts
      retry(2) {
        archiveArtifacts artifacts: 'target/allure-results/**', fingerprint: true
        allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
      }
    }
  }
}

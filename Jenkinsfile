// Jenkinsfile
pipeline {
  agent { docker { image 'maven:3.9.9-eclipse-temurin-21' } }
  stages {
    stage('Checkout') {
      steps { checkout scm }
    }
    stage('Run Tests in Docker Compose') {
      steps {
        sh 'docker compose up --abort-on-container-exit --build'
      }
      post {
        always {
          sh 'docker compose down -v'
        }
      }
    }
  }
  post {
    always {
      junit 'target/surefire-reports/*.xml'
    }
  }
}

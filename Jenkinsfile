pipeline {
  agent {
    docker {
      image 'depul/maven-docker:latest'
      args '-v /var/run/docker.sock:/var/run/docker.sock'
      label 'docker'
    }
  }
  stages {
    stage('Checkout') {
      steps {
        git url: 'https://github.com/deepthipulicherla/ValidateDB.git', branch: 'main'
      }
    }
    stage('Run Tests in Docker Compose') {
      steps {
        sh 'docker-compose up --abort-on-container-exit --build'
      }
      post {
        always {
          sh 'docker-compose down -v'
        }
      }
    }
  }
  post {
    always {
      node {
        junit 'target/surefire-reports/*.xml'
        archiveArtifacts artifacts: 'allure-results/**', fingerprint: true
      }
    }
  }
}

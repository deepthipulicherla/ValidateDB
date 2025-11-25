pipeline {
  agent any

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

      stage('Verify DB Init') {
        steps {
          script {
            def result = sh(
              script: '''
                docker exec mysqldatabasevalidation-mysql-1 \
                  mysql -uappuser -papppass -D appdb -N -e "SELECT COUNT(*) FROM users;"
              ''',
              returnStdout: true
            ).trim()
            if (result == "0" || result == "") {
              error "Database initialization failed: users table is empty or missing."
            } else {
              echo "Database initialized successfully with ${result} rows."
            }
          }
        }
      }

      stage('Run Tests in Docker Compose') {
        steps {
          sh 'docker-compose down --remove-orphans -v || true'
          sh '''
            docker-compose \
              -p mysqldatabasevalidation \
              up --abort-on-container-exit --build
          '''
        }
        post {
          always {
            sh 'docker-compose -p mysqldatabasevalidation down --remove-orphans -v || true'
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
        retry(2) {
          archiveArtifacts artifacts: 'target/allure-results/**', fingerprint: true
          allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
        }
      }
    }
  }
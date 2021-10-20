pipeline {
    agent any

    environment {
        NEW_VERSION = "1.2.3"
        CREDS = credentials('github-creds')
    }
    stages {

        stage('build') {
            steps {
                echo 'building app'
                echo 'building ${NEW_VERSION} - var is not installed'
                echo "building ${NEW_VERSION} - !var is installed!"
                echo "Creds: ${CREDS}, user: ${CREDS_USR}, passwd: ${CREDS_PSW}"
            }
        }
        stage('test') {
            when {
                expression {
                    env.BRANCH_NAME == 'dev' || env.BRANCH_NAME == 'master'
                }
            }
            steps {
                echo 'testing app'
            }
        }
        stage('deploy') {
            steps {
                echo 'deploying app'
                withCredentials([
                    usernamePassword(credentials: 'github-creds', usernameVariable: USER, passwordVariable: PASSWORD)
                ]){
                    sh "echo user"
                }
            }
        }
    }
}

pipeline {
    agent any

    tools {
        maven 'Maven 3.8.3'
    }
    parameters {
        string(name: 'VERSION', defaultValue: '', description: 'version to deploy on prod')
        choice(name: 'VERSION2', choices: ['1.1.0', '1.1.1', '1.1.2'], description: 'some descr')
        booleanParam(name: 'executeTest', defaultValue: true, description: 'bool param')
    }
    environment {
        NEW_VERSION = "1.2.3"
        CREDS = credentials('github-creds')
    }
    stages {

        stage('build') {
            steps {
                echo 'building app'
                sh 'mvn --version'
                echo 'building ${NEW_VERSION} - var is not installed'
                echo "building ${NEW_VERSION} - !var is installed!"
                echo "Creds: ${CREDS}, user: ${CREDS_USR}, passwd: ${CREDS_PSW}"
            }
        }
        stage('test') {
            when {
                expression {
                    env.BRANCH_NAME == 'dev' || params.executeTest
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
                    usernamePassword(credentialsId: 'github-creds', usernameVariable: 'USER', passwordVariable: 'PASSWORD')
                ]){
                    sh "echo user"
                    sh 'echo user: $USER, pswd: $PASSWORD'
                }
            }
        }
    }
}

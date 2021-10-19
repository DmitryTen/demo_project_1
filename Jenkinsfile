pipeline {
    agent any

    environment {
        NEW_VERSION = "1.2.3"
    }
    stages {

        stage('build') {
            steps {
                echo 'building app'
                echo 'building ${NEW_VERSION} - var is not installed'
                echo "building ${NEW_VERSION} - !var is installed!"
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
            }
        }
    }
}

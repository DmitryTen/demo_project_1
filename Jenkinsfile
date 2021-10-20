def gv

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
        stage('init'){
            steps {
                script {
                    gv = load 'external_script.groovy'
                }
            }
        }
        stage('build') {
            steps {
                script {
                    gv.buildApp()
                }
            }
        }
        stage('test') {
            when {
                expression {
                    env.BRANCH_NAME == 'dev' || params.executeTest
                }
            }
            steps {
                script {
                    env.ENV_VAR = input message: "Other way make selection. Inside script",
                            ok: "this is a btn name",
                            parameters: [
                                    choice(name: 'ONE', choices: ['dev', 'staging', 'prod'], description: 'some descr')
                            ]
                    gv.testApp()
                }
            }
        }
        stage('deploy') {
            input {
                message "Select the environment to deploy to"
                ok "done"
                parameters {
                    choice(name: 'var_name', choices: ['dev', 'staging', 'prod'], description: 'some descr')
                }
            }
            steps {
                script {
                    gv.deployApp()
                    echo "deploying to $var_name"
                }
            }
        }
    }
}

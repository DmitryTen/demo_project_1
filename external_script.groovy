def buildApp() {
    echo 'building app'
    sh 'mvn --version'
    echo 'building ${NEW_VERSION} - var is not installed'
    echo "building ${NEW_VERSION} - !var is installed!"
    echo "Creds: ${CREDS}, user: ${CREDS_USR}, passwd: ${CREDS_PSW}"}

def testApp() {
    echo 'testing the application ...'
}

def deployApp() {
    echo "deploying app ${params.VERSION2}"
    withCredentials([
            usernamePassword(credentialsId: 'github-creds', usernameVariable: 'USER', passwordVariable: 'PASSWORD')
    ]){
        sh "echo user"
        sh 'echo user: $USER, pswd: $PASSWORD'
        sh "echo user: $USER, pswd: $PASSWORD"
        sh "echo user: $var_name"
    }
}

return this
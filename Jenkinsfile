pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Deploy') {
            steps {
                sshagent(['id_rsa']) {
                    sh 'scp /tmp/root.env root@172.233.195.51:~/root.env'
                    sh 'scp /tmp/gcp/ root@172.233.195.51 ~/gcp/'
                    sh 'ssh root@172.233.195.51 ~/compose.sh'
                }
            }
        }
    }
}

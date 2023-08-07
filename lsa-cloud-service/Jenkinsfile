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
                sh 'scp /tmp/root.env root@172.233.195.51:~/root.env'
                sh 'scp /tmp/gcs/ root@172.233.195.51 ~/gcs/'
                sh 'ssh root@172.233.195.51 ~/compose.sh'
            }
        }
    }
}

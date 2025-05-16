def imageName = ''
def branchName = env.BRANCH_NAME ?: 'local'

def buildImage() { 
    def sanitizedBranch = branchName.replaceAll(/[^a-zA-Z0-9.-]/, '-')
    def timestamp = sh(script: 'date +"%Y%m%d%H%M%S"', returnStdout: true).trim()
    imageName = "softunium/static-site:${sanitizedBranch}-${timestamp}"

    echo "Building the docker image..."
    echo "Image name: ${imageName}"
    
    withCredentials([usernamePassword(credentialsId: 'DockerHub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh "docker build -t ${imageName} ."
        sh 'echo $PASS | docker login -u $USER --password-stdin'
        sh "docker push ${imageName}"
    }
}

def deployApp() {
    echo 'Deploying the application...'
    withCredentials([usernamePassword(credentialsId: 'DockerHub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh """
            echo \$PASS | docker login -u \$USER --password-stdin
            docker pull ${imageName}
            docker rm -f static-site || true
            docker run -d --name static-site -p 8080:80 ${imageName}
        """
    }
}

return this

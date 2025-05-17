def imageName = ''

def buildImage() {
    def branchName = env.BRANCH_NAME ?: 'local'
    def sanitizedBranch = branchName.replaceAll(/[^a-zA-Z0-9.-]/, '-')
    echo "Building the docker image for ${env.ENV_TYPE} environment..."

    withCredentials([usernamePassword(credentialsId: 'DockerHub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        def timestamp = sh(script: 'date +"%Y%m%d%H%M%S"', returnStdout: true).trim()
        imageName = "softunium/static-site:${env.ENV_TYPE}-${sanitizedBranch}-${timestamp}"

        echo "Image name: ${imageName}"

        sh "docker build -t ${imageName} ."
        sh 'echo $PASS | docker login -u $USER --password-stdin'
        sh "docker push ${imageName}"
    }
}


def deployApp() {
    echo "Deploying to ${envType} environment..."
    withCredentials([usernamePassword(credentialsId: 'DockerHub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh """
            echo \$PASS | docker login -u \$USER --password-stdin
            docker pull ${imageName}
            docker rm -f static-site-${env.ENV_TYPE} || true
            docker run -d --name static-site-${env.ENV_TYPE} -p 8080:80 ${imageName}
        """
    }
}


return this
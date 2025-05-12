def imageName = ''

def buildImage() {
    echo "Building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'DockerHub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        def timestamp = sh(script: 'date +"%Y%m%d%H%M%S"', returnStdout: true).trim()
        imageName = "softunium/static-site:rightshift.bg-${timestamp}"

        echo "Image name: ${imageName}"
        
        sh "docker build -t ${imageName} ."
        sh 'echo $PASS | docker login -u $USER --password-stdin'
        sh "docker push ${imageName}"
    }
}


def deployApp() {
    echo 'Deploying the application...'
    // Deployment logic here
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
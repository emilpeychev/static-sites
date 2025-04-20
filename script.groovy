def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'DockerHub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        def timestamp = sh(script: 'date +"%Y%m%d%H%M%S"', returnStdout: true).trim()
        def imageName = "softunium/static-site:rightshift.bg${timestamp}"

        echo "Image name: ${imageName}"
        
        sh "docker build -t ${imageName} ."
        sh 'echo $PASS | docker login -u $USER --password-stdin'
        sh "docker push ${imageName}"
    }
}

def deployApp() {
    echo 'Deploying the application...'
    // Add your deployment logic here
}
return this
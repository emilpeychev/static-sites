def imageName = ''

def buildImage(envType) {
    def branchName = env.BRANCH_NAME ?: 'local'
    def sanitizedBranch = branchName.replaceAll(/[^a-zA-Z0-9.-]/, '-')
    echo "Building the docker image for ${envType} environment..."

    withCredentials([usernamePassword(credentialsId: 'DockerHub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        def timestamp = sh(script: 'date +"%Y%m%d%H%M%S"', returnStdout: true).trim()
        imageName = "softunium/static-site:${envType}-${sanitizedBranch}-${timestamp}"

        echo "Image name: ${imageName}"

        sh "docker build -t ${imageName} ."
        sh 'echo $PASS | docker login -u $USER --password-stdin'
        sh "docker push ${imageName}"
    }
}

def deployApp(envType) {
    echo "Deploying to ${envType} environment..."
    withCredentials([usernamePassword(credentialsId: 'DockerHub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh """
            echo \$PASS | docker login -u \$USER --password-stdin
            docker pull ${imageName}
            docker rm -f static-site-${envType} || true
            docker run -d --name static-site-${envType} -p 8080:80 ${imageName}
        """
    }
}

def postAnalyses() {
    echo "Collecting last 500 lines of Jenkins build logs for AI summary..."

    def logText = currentBuild.getRawBuild().getLog(500).join("\n")

    def promptText = """Please provide a concise summary highlighting errors, warnings, and important information from these Jenkins pipeline logs:

${logText}
"""

    def payload = [
        model : 'llama3',
        prompt: promptText,
        stream: false
    ]

    def jsonPayload = groovy.json.JsonOutput.toJson(payload)

    echo "Sending logs summary request to Ollama AI..."

    def response = httpRequest(
        httpMode: 'POST',
        contentType: 'APPLICATION_JSON',
        requestBody: jsonPayload,
        url: 'http://localhost:11434/generate',
        validResponseCodes: '200:299',
        consoleLogResponseBody: true
    )

    echo "Ollama AI Summary Response:\n${response.content}"
}



return this
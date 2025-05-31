import jenkins.plugins.http_request.HttpRequestStep
import jenkins.plugins.http_request.util.MimeType

def call() {
    echo "Collecting last 500 lines of Jenkins build logs for AI summary..."

    def logText = currentBuild.getRawBuild().getLog(500).join("\n")

    def promptText = """Please provide a concise summary highlighting errors and important information from these Jenkins pipeline logs:

${logText}
"""

    def payload = [
        model: 'llama3',
        prompt: promptText,
        stream: false
    ]

    def jsonPayload = groovy.json.JsonOutput.toJson(payload)

    echo "Sending logs summary request to Ollama AI..."
    echo "JSON Payload: ${jsonPayload}"

    try {
        def response = httpRequest(
            httpMode: 'POST',
            contentType: MimeType.JSON, // ✅ Correct enum usage
            requestBody: jsonPayload,
            url: "${env.OLLAMA_API}/api/generate",
            validResponseCodes: '200:299',
            consoleLogResponseBody: true
        )
        def parsed = new groovy.json.JsonSlurper().parseText(response.content)
        echo "AI Model: ${parsed.model}"
        echo "Created At: ${parsed.created_at}"
        echo "Summary:\n${parsed.response}"

    } catch (Exception e) {
        echo "Failed to get AI summary: ${e.message}"
        currentBuild.result = 'UNSTABLE'
    }
}

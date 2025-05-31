def call() {
    echo "Collecting last 500 lines of Jenkins build logs for AI summary..."

    def logText = currentBuild.getRawBuild().getLog(500).join("\n")

    def maxLength = 15000
    if (logText.length() > maxLength) {
        logText = logText.substring(logText.length() - maxLength)
        echo "Log truncated to last ${maxLength} characters."
    }

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
    echo "JSON Payload (truncated): ${jsonPayload.take(500)}..."

    def apiUrl = env.OLLAMA_API ?: error("OLLAMA_API environment variable not set")
    def fullUrl = "${apiUrl}/api/generate"

    try {
        def response = httpRequest(
            httpMode: 'POST',
            contentType: 'application/json',
            requestBody: jsonPayload,
            url: fullUrl,
            validResponseCodes: '200:299',
            consoleLogResponseBody: true
        )

        echo "Response status: ${response.status}"

        def parsed = new groovy.json.JsonSlurper().parseText(response.content)

        echo "AI Model: ${parsed.model ?: 'N/A'}"
        echo "Created At: ${parsed.created_at ?: 'N/A'}"
        echo "Summary:\n${parsed.response ?: 'No summary received.'}"

    } catch (Exception e) {
        echo "Failed to get AI summary: ${e.message}"
        currentBuild.result = 'UNSTABLE'
    }
}

def call() {
    def branch = env.BRANCH_NAME ?: sh(script: "git rev-parse --abbrev-ref HEAD", returnStdout: true).trim()
    def envType = "dev"

    if (branch == "main" || branch == "master") {
        envType = "prod"
    } else if (branch == "dev") {
        envType = "staging"
    }

    echo "Branch: ${branch}"
    echo "Target environment: ${envType}"
    
    // Save globally
    env.ENV_TYPE = envType
}
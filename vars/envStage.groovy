def call() {
    def branch = env.BRANCH_NAME ?: sh(script: "git rev-parse --abbrev-ref HEAD", returnStdout: true).trim().toLowerCase()
    def isProd = branch in ['master', 'main']
    def isStaging = branch in ['staging']
    def envType = isProd ? 'prod' : (isStaging ? 'staging' : 'dev')

    echo "Environment: ${envType.toUpperCase()} (Branch: ${branch})"

    // Return it so the pipeline can capture and use it
    return envType
}

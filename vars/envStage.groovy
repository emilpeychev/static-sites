def call() {
    def branch = env.BRANCH_NAME ?: sh(script: "git rev-parse --abbrev-ref HEAD", returnStdout: true).trim()
    def isProd = branch in ['master', 'main']
    def envType = isProd ? 'prod' : 'dev'

    echo "Environment: ${envType.toUpperCase()} (Branch: ${branch})"

    // Return it so the pipeline can capture and use it
    return envType
}
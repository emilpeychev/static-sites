def call() {
    def branch = env.BRANCH_NAME ?: sh(script: "git rev-parse --abbrev-ref HEAD", returnStdout: true).trim()
    def isProd = (branch == 'main')

    echo "Environment: ${isProd ? 'Production' : 'Development'} (Branch: ${branch})"

    if (isProd) {
        // Run prod-only checks
        sh "echo 'Running production tasks...'"
    } else {
        // Run dev-only scans
        sh "echo 'Running dev tasks...'"
    }
}

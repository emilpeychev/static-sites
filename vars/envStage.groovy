def call() {
    def branch = env.BRANCH_NAME ?: sh(script: "git rev-parse --abbrev-ref HEAD", returnStdout: true).trim()
    def isProd = branch in ['master', 'main']

    echo "Environment: ${isProd ? 'Production' : 'Development'} (Branch: ${branch})"

    if (isProd) {
        sh "echo 'Running production tasks...'"
        sh 'printenv'
    } else {
        sh "echo 'Running dev tasks...'"
        sh 'printenv'
    }
}

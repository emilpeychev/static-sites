def call() {
    echo "Checking for End-of-Life packages using xeol..."

    // Install xeol if needed
    sh '''
        if ! command -v /tmp/bin/xeol > /dev/null; then
            echo "Installing xeol..."
            mkdir -p /tmp/bin
            curl -sSfL https://raw.githubusercontent.com/xeol-io/xeol/main/install.sh | sh -s -- -b /tmp/bin
        fi
    '''

    // Run the xeol scan
    sh "/tmp/bin/xeol . --output table"
}

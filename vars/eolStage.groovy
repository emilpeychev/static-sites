def call() {
    echo "Checking for End-of-Life packages using xeol..."

    // Install xeol if needed
    sh '''
        if ! command -v /tmp/xeol/bin/xeol > /dev/null; then
            echo "Installing xeol..."
            mkdir -p /tmp/xeol/bin
            curl -sSfL https://raw.githubusercontent.com/xeol-io/xeol/main/install.sh | sh -s -- -b /tmp/xeol/bin
        fi
    '''

    // Run the xeol scan
    sh "/tmp/xeol/bin/xeol . --output table"
}

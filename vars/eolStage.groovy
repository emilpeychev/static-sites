// vars/eolStage.groovy
def call() {
        echo "Checking for End-of-Life packages using xeol..."
        
    // Install xeol if needed
    sh '''
        if ! command -v xeol > /dev/null; then
            echo "Installing xeol..."
            mkdir -p ~/tmp/bin
            curl -sSfL https://raw.githubusercontent.com/xeol-io/xeol/main/install.sh | sh -s -- -b ~/tmp/bin
            export PATH=~/tmp/bin:$PATH
        fi
    '''
    
    // Run the xeol scan
    sh "~/tmp/bin/xeol . --output table"  // or json, sarif, etc.
}
// vars/eolStage.groovy

def call() {
    echo "Checking for End-of-Life packages using xeol..."

    sh '''
        if ! command -v xeol > /dev/null; then
            echo "Installing xeol..."
            mkdir -p $WORKSPACE/bin
            curl -sSfL https://raw.githubusercontent.com/anchore/xeol/main/install.sh | sh -s -- -b $WORKSPACE/bin
            export PATH=$WORKSPACE/bin:$PATH
        fi

        # Run the xeol scan
        $WORKSPACE/bin/xeol . --output table
    '''
}

def call(){
        //Clean-up Stage
        echo 'Removing scan tools...'
        sh "rm -fr /tmp/bin/trivy/*"
        sh "rm -fr /tmp/bin/xeol/*"
}
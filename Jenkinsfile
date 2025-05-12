@Library('shared-lib') _

def gv

pipeline {
    agent 'linode-agent'

    stages {
        stage("init") {
            steps {
                script {
                    echo "Initialising groovy script...."
                    gv = load "script.groovy"
                }
            }
        }

        stage("build") {
            steps {
                script {
                    echo "Building the application...."
                    gv.buildImage()
                }
            }
        }

        stage("test for EOL") {
            steps {
                script {
                    echo "Scanning for End-of-Life packages..."
                    eolStage()
                }
            }
        }

        stage("deploy") {
            steps {
                script {
                    echo "Deploying the application...."
                    gv.deployApp()
                }
            }
        }
    }
}

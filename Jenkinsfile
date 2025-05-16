@Library('shared-lib@rs-dev') _

def gv

pipeline {
    agent {
        label 'linode-agent'
    }
    stages {
        stage("detect environment") {
            steps {
                script {
                    // You mentioned envStage() - make sure this is defined somewhere
                    envStage()
                }
            }
        }

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
                    echo "Building the application and Docker image...."
                    gv.buildImage()
                }
            }
        }

        stage("test for EOL packages") {
            steps {
                script {
                    echo "Scanning for End-of-Life packages..."
                    eolStage()
                }
            }
        }

        stage("deploy") {
            when {
                branch 'master'
            }
            steps {
                script {
                    echo "Deploying the application...."
                    gv.deployApp()
                }
            }
        }
    }
}

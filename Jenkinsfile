@Library('shared-lib') _

pipeline {
    agent any

    stages {
        stage("init") {
            steps {
                script {
                    echo "Initialising groovy script...."
                    gv = load "script.groovy"
                }
            }
            description: "Initialising groovy script"
        }

        stage("build") {
            steps {
                script {
                    echo "Building the application...."
                    gv.buildImage()
                }
            }
            description: "Building the Docker image"
        }

        stage("test for EOL") {
            steps {
                script {
                    echo "Scanning for End-of-Life packages..."
                    eolStage()
                }
            }
            description: "Scanning for End-of-Life packages"
        }

        stage("deploy") {
            steps {
                script {
                    echo "Deploying the application...."
                    gv.deployApp()
                }
            }
            description: "Deploying the application"
        }
    }
}

@Library('shared-lib@clean-up') _

def gv

def ENV_TYPE = params.ENV_TYPE ?: envStage() ?: 'dev'

pipeline {
    agent {
        label 'linode-agent'
        }

    parameters {
        choice(name: 'ENV_TYPE', choices: ['dev', 'staging', 'prod'], description: 'Choose the deployment environment')
    }

    stages {
        stage("detect environment") {
            steps {
                script {
                    ENV_TYPE = params.ENV_TYPE ?: envStage()
                    env.ENV_TYPE = ENV_TYPE
                    echo "ENV_TYPE is ${ENV_TYPE}"
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
                    echo "Building the application...."
                    gv.buildImage(ENV_TYPE)
                }
            }
        }

        stage("test for EOL packages") {
            steps {
                script {
                    echo "Scanning for End-of-Life packages..."
                    eolStage(imageName)
                }
            }
        }

        stage("deploy") {

            when{
                branch 'master'
            }

            steps {
                script {
                    echo "Deploying the application...."
                    gv.deployApp(ENV_TYPE)
                }
            }
        }
        stage("cleanup pipeline") {
            steps {
                script {
                    cleanupStage()
                }
            }
        }
    }
}

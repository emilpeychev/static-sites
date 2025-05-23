def targetEnv = params.ENV_TYPE ?: envStage()

if (targetEnv == 'prod') {
    deployToProd()
} else {
    deployToDev()
}

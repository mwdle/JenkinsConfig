@Library("JenkinsPipelines") _ // See https://github.com/mwdle/JenkinsPipelines

// Disable index triggers on branches that are not main/master
boolean isMainBranch = (env.BRANCH_NAME == 'main' || env.BRANCH_NAME == 'master')
boolean disableIndexTriggers = !isMainBranch

quadletPipeline(
    agentLabel: 'persistent',
    disableIndexTriggers: disableIndexTriggers,
    podmanSecretMappings: [
        [
            type: 'usernamePassword',
            credentialsId: 'Jenkins',
            podmanSecretUsernameName: 'jenkins_admin_username',
            podmanSecretPasswordName: 'jenkins_admin_password'
        ],
        [
            type: 'usernamePassword',
            credentialsId: 'Forgejo PAT',
            podmanSecretUsernameName: 'jenkins_git_username',
            podmanSecretPasswordName: 'jenkins_git_token'
        ],
        [
            type: 'usernamePassword',
            credentialsId: 'Email noreply',
            podmanSecretUsernameName: 'jenkins_smtp_username',
            podmanSecretPasswordName: 'jenkins_smtp_password'
        ],
        [
            type: 'string',
            credentialsId: 'jenkins_alert_email'
        ]
    ]
)

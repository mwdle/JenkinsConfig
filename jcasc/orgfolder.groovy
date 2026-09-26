organizationFolder('git.mwdle.com') {
    displayName('mwdle Hosting')
    triggers {
        periodicFolderTrigger {
            interval('1h')
        }
    }
    organizations {
        gitea { // Token requires repository:read/write, user:read, and organization:read permissions
            serverUrl('https://git.mwdle.com')
            repoOwner('lab')
            credentialsId('git-creds') // Credential ID for git server credentials -- see gitcreds seed job (`jenkins/gitcreds.groovy`)
            traits {
                giteaExcludeArchivedRepositories {}
                giteaTagDiscovery {}
                giteaBranchDiscovery {
                    strategyId(3) // Discover all branches
                }
                giteaWebhookRegistration {
                    mode('ITEM')
                }
            }
        }
    }
    orphanedItemStrategy {
        discardOldItems {}
    }
    projectFactories {
        workflowMultiBranchProjectFactory {
            scriptPath('Jenkinsfile')
        }
        inlineDefinitionMultiBranchProjectFactory {
            markerFile('compose.yaml')
            sandbox(true)
            script("""
// A standalone Jenkinsfile would typically use `@Library(...) _` instead of `library(...)`
library("JenkinsPipelines") // See https://github.com/mwdle/JenkinsPipelines

// Disable index triggers on branches that are not main/master
boolean isMainBranch = (env.BRANCH_NAME == 'main' || env.BRANCH_NAME == 'master')
boolean disableIndexTriggers = !isMainBranch
dockerComposePipeline(
    disableIndexTriggers: disableIndexTriggers,
    envFileCredentialIds: ["common.env", env.JOB_NAME.split('/')[1] + ".env"],
    persistentWorkspace: "\${env.DOCKER_VOLUMES}/deployments",
    alertEmail: "\${env.ALERT_EMAIL}"
)
            """)
        }
    }
}

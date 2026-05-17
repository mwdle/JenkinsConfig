// All environment variables used in this file are defined in `compose.yaml` in the root of this repository
organizationFolder(System.getenv('ORGFOLDER_NAME')) {
    displayName(System.getenv('ORGFOLDER_DISPLAY_NAME'))
    description(System.getenv('ORGFOLDER_DESCRIPTION'))
    triggers { // Rescan organization daily
        periodicFolderTrigger {
            interval('1h')
        }
    }
    organizations {
        gitea { // Token requires repository:read/write, user:read, and organization:read permissions
            serverUrl(System.getenv('GIT_SERVER_URL'))
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

    // Detect markerfiles for certain projects and associate either a shared library or fall back to a Jenkinsfile within the SCM repository
    projectFactories {
        workflowMultiBranchProjectFactory {
            scriptPath('Jenkinsfile')
        }
        inlineDefinitionMultiBranchProjectFactory { // Requires inline pipeline plugin -- see `Dockerfile`
            markerFile('compose.yaml') // Load any repository that has a `compose.yaml` file
            sandbox(true) // Enable Groovy sandbox for security
            // This inline script is the default pipeline for any repository containing a 'compose.yaml' file that does NOT have its own Jenkinsfile.
            // To override this default, create a 'Jenkinsfile' in the target repository which will always take precedence over this inline definition.
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
    alertEmail: "\${env.ALERT_EMAIL}",
    defaultComposeBuild: true
)
            """)
        }
    }
}

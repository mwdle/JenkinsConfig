import jenkins.model.Jenkins

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
            repoOwner("mwdle") // Git username to get repositories for
            credentialsId('Gitea PAT') // Credential ID for git server credentials
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
library("JenkinsPipelines")
/*
 * This Docker Compose deployment is managed by the `dockerComposePipeline` defined in the
 * Jenkins Pipelines shared library (https://github.com/mwdle/JenkinsPipelines).
 *
 * Configuration:
 * - envFileCredentialIds:
 * Injects secrets from a Jenkins 'Secret file' credential. It expects the credential ID
 * to match the name of this repository, suffixed with '.env'.
 *
 * - persistentWorkspace:
 * Deploys to a stable directory on the host to preserve data between builds. The path is
 * dynamically set using the DOCKER_VOLUMES environment variable.
 *
 * Note: This script uses the `library()` step. A standalone Jenkinsfile would
 * typically use `@Library("JenkinsPipelines") _` at the top of the file.
 */
dockerComposePipeline(envFileCredentialIds: [env.JOB_NAME.split('/')[1] + ".env"], persistentWorkspace: "${System.getenv('DOCKER_VOLUMES')}/deployments")
            """)
        }
    }
}
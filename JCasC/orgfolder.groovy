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
            repoOwner(System.getenv('GIT_USERNAME'))
            credentialsId('git-creds') // Credential ID for git server credentials -- see JCasC (`jenkins/jenkins.yaml`)
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
 * This pipeline uses the 'dockerComposePipeline' to manage the application's deployment with a default configuration.
 *
 * Default Configuration:
 * - defaultUseSecrets: true
 * Enables secret .env file injection by default.
 *
 * - persistentWorkspace: "${System.getenv('DOCKER_VOLUMES')}/deployments"
 * Enables the persistent workspace mode to support relative bind mounts in docker-compose.yml.
 * The path points to this application's dedicated deployment directory on the host.
 *
 * Requirements:
 * - JenkinsPipelines Library: https://github.com/mwdle/JenkinsPipelines
 *
 * (Note: The `library()` step is used here. A standalone Jenkinsfile would
 * typically use `@Library("JenkinsPipelines") _` at the top.)
 */
dockerComposePipeline(envFileCredentialIds: [env.JOB_NAME.split('/')[1] + ".env"], persistentWorkspace: "${System.getenv('DOCKER_VOLUMES')}/deployments")
            """)
        }
    }
}
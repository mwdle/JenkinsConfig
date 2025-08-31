import jenkins.model.Jenkins

organizationFolder(System.getenv('ORGFOLDER_NAME')) {
    displayName(System.getenv('ORGFOLDER_DISPLAY_NAME'))
    description(System.getenv('ORGFOLDER_DESCRIPTION'))
    triggers { // Rescan organization daily
        periodicFolderTrigger {
            interval('24h')
        }
    }
    organizations {
        gitea { // Token requires repository:read/write, user:read, and organization:read permissions
            serverUrl(System.getenv('GIT_SERVER_URL'))
            repoOwner(System.getenv('GIT_USERNAME'))
            credentialsId('git-creds')
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
        discardOldItems {
            daysToKeep(730)
            numToKeep(10000)
        }
    }

    // Detect markerfiles for certain projects and associate a shared library or fall back to a Jenkinsfile within the SCM repository
    projectFactories {
        workflowMultiBranchProjectFactory {
            scriptPath('Jenkinsfile')
        }
        inlineDefinitionMultiBranchProjectFactory {
            markerFile('compose.yaml') // Load any repository that has a `compose.yaml` file
            sandbox(true) // Enable Groovy sandbox for security
            // Inline pipeline script means a Jenkinsfile is not required in every repository/branch -- in this case the Docker Compose pipeline is used for any repository containing a `compose.yaml` file (if Jenkinsfile doesn't exist for that repository)
            script('''
                library("JenkinsPipelines") // See https://github.com/mwdle/JenkinsPipelines -- defined in compose.yaml
                dockerComposePipeline()
            ''')
        }
    }
}
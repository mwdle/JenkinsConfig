import jenkins.model.Jenkins

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
        inlineDefinitionMultiBranchProjectFactory { // Requires inline pipeline plugin -- see `Dockerfile.jenkins`
            markerFile('compose.yaml') // Load any repository that has a `compose.yaml` file
            sandbox(true) // Enable Groovy sandbox for security
            // Inline pipeline script means a Jenkinsfile is not required in every repository/branch -- in this case the Docker Compose pipeline is used for any repository containing a `compose.yaml` file (if Jenkinsfile doesn't exist for that repository)
            script('''
                library("JenkinsPipelines") // See https://github.com/mwdle/JenkinsPipelines -- see compose.yaml
                // `useBitwardenDefault: true` means this pipeline will always pull in a .env file from Bitwarden secure note that has the same name as the repository.
                // If this behavior is not desired, simply create a Jenkinsfile in the repository that is the same as this one, but with `useBitwardenDefault: false`.
                dockerComposePipeline(useBitwardenDefault: true) // This specific pipeline is dependent on the JenkinsBitwardenUtils shared library (https://github.com/mwdle/JenkinsBitwardenUtils) -- see compose.yaml
            ''')
        }
    }
}
import jenkins.model.Jenkins

def gitServerUrl = System.getenv('GIT_SERVER_URL')

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
            serverUrl(gitServerUrl)
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
        discardOldItems {
            daysToKeep(730)
            numToKeep(10000)
        }
    }

    // Detect markerfiles for certain projects and associate either a remote Jenkinsfile or fall back to a Jenkinsfile within the SCM repository
    projectFactories {
        workflowMultiBranchProjectFactory {
            scriptPath('Jenkinsfile')
        }
        remoteJenkinsFileWorkflowBranchProjectFactory { // https://plugins.jenkins.io/remote-file/
            localMarker("compose.yaml")
            matchBranches(false)
            remoteJenkinsFile("pipelines/dockerCompose/Jenkinsfile")
            remoteJenkinsFileSCM {
                gitSCM {
                    userRemoteConfigs {
                        userRemoteConfig {
                            name("JenkinsPipelines") // Custom Repository Name or ID
                            url("${gitServerUrl}/mwdle/JenkinsPipelines") // See https://github.com/mwdle/JenkinsPipelines
                            refspec("main")
                        }
                    }
                }
            }
        }
    }
}
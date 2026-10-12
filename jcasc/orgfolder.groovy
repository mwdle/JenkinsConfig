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
            credentialsId('git-creds') // see gitcreds seed job (`jcasc/gitcreds.groovy`)
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
    }
}

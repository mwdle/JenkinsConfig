import jenkins.model.Jenkins

organizationFolder(System.getenv('ORGFOLDER_NAME')) {
    displayName(System.getenv('ORGFOLDER_DISPLAY_NAME'))
    description(System.getenv('ORGFOLDER_DESCRIPTION'))
    triggers {
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
}
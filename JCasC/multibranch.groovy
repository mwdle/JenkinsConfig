multibranchPipelineJob('DinoGlue') {
    displayName('DinoGlue')
    description('Multibranch Pipeline Job for DinoGlue by Dino3Harris')
    branchSources {
        gitea {
            id('DinoGlue')
            serverUrl(System.getenv('GIT_SERVER_URL'))
            credentialsId('git-creds')
            repoOwner('Dino3Harris')
            repository('Deployment')
            traits {
                giteaTagDiscovery()
                giteaBranchDiscovery {
                    strategyId(3) // Discover all branches
                }
            }
        }
    }
    orphanedItemStrategy {
        discardOldItems()
    }
}
multibranchPipelineJob('DinoGlue') {
    displayName('DinoGlue')
    description('Multibranch Pipeline Job for DinoGlue by Dino3Harris')
    branchSources {
        git {
            remote("${System.getenv('GIT_SERVER_URL')}/Dino3Harris/DinoGlue.git")
            credentialsId('git-creds')
            traits {
                gitTagDiscovery()
                gitBranchDiscovery()
            }
        }
    }
    orphanedItemStrategy {
        discardOldItems()
    }
}
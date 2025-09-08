multibranchPipelineJob('DinoGlue') {
    displayName('DinoGlue')
    description('Multibranch Pipeline Job for DinoGlue by Dino3Harris')
    branchSources {
        git {
            id("DinoGlue Repository")
            remote("${System.getenv('GIT_SERVER_URL')}/dino3harris/dinoglue.git")
            credentialsId('git-creds')
        }
    }
    orphanedItemStrategy {
        discardOldItems()
    }
}
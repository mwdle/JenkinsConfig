multibranchPipelineJob('DinoGlue') {
    displayName('DinoGlue')
    description('Multibranch Pipeline Job for DinoGlue by Dino3Harris')
    branchSources {
        git {
            id("DinoGlue Repository")
            remote("${System.getenv('GIT_SERVER_URL')}/Dino3Harris/Deployment.git") // https://github.com/Dino3Harris
            credentialsId('git-creds')
        }
    }
    orphanedItemStrategy {
        discardOldItems()
    }
}
// All environment variables used in this file are defined in `compose.yaml` in the root of this repository
multibranchPipelineJob('DinoGlue') {
    displayName('DinoGlue')
    description('Multibranch Pipeline Job for DinoGlue by Dino3Harris')
    branchSources {
        git {
            id("DinoGlue Repository")
            remote("${System.getenv('GIT_SERVER_URL')}/Dino3Harris/Deployment.git") // https://github.com/Dino3Harris
            credentialsId('Gitea PAT')
        }
    }
    orphanedItemStrategy {
        discardOldItems()
    }
}
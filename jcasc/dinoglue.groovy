multibranchPipelineJob('DinoGlue') {
    displayName('DinoGlue')
    branchSources {
        git {
            id("DinoGlue")
            remote('https://git.mwdle.com/Dino3Harris/Deployment.git') // https://github.com/Dino3Harris
            credentialsId('git-creds')
        }
    }
    orphanedItemStrategy {
        discardOldItems()
    }
}
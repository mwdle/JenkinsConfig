// Configure the git credentials (Always perform this in Groovy DSL instead of JCasC to prevent JCasC from wiping user created credentials on Jenkins restart)
def credentialId = "git-creds"
def credentialDescription = "Git credentials for organization folder"
def username = System.getenv("GIT_USERNAME")
def password = System.getenv("GIT_TOKEN")
def store = SystemCredentialsProvider.instance.store
def domain = Domain.global()
def existingCredentials = store.getCredentials(domain).collect { it.id }
if (existingCredentials.contains(credentialId)) {
  println "'${credentialId}' already exists! Skipping creation."
}
else {
    if (username && password) {        
        def newCredential = new UsernamePasswordCredentialsImpl(
            CredentialsScope.GLOBAL,
            credentialId,
            credentialDescription,
            username,
            password
        )
        store.addCredentials(domain, newCredential)
        println "Successfully created credential '${credentialId}'."
    } else {
        println "!!! WARNING: GIT_USERNAME or GIT_TOKEN environment variables not set. Cannot create '${credentialId}'."
    }
}
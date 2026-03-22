import jenkins.model.Jenkins
import com.cloudbees.plugins.credentials.SystemCredentialsProvider
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.CredentialsScope
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl

// Configure the git credentials
def credentialId = "git-creds"
def credentialDescription = "Git credentials for organization folder"
def username = System.getenv("GIT_USERNAME")
def password = System.getenv("GIT_TOKEN")
def store = SystemCredentialsProvider.instance.store
def domain = Domain.global()

if (username && password) {
    def newCredential = new UsernamePasswordCredentialsImpl(
        CredentialsScope.GLOBAL,
        credentialId,
        credentialDescription,
        username,
        password
    )

    // Find the specific credential object if it exists
    def existing = store.getCredentials(domain).find { it.id == credentialId }

    if (existing) {
        store.updateCredentials(domain, existing, newCredential)
        println "Successfully updated/overwrote credential '${credentialId}'."
    } else {
        store.addCredentials(domain, newCredential)
        println "Successfully created credential '${credentialId}'."
    }
} else {
    println "!!! WARNING: GIT_USERNAME or GIT_TOKEN environment variables not set. Skipping."
}

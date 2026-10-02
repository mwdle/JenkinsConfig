import com.cloudbees.plugins.credentials.SystemCredentialsProvider
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.CredentialsScope
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl
import java.io.File

def credentialId = 'git-creds'
def credentialDescription = 'Git credentials for organization folder'
def usernameFile = new File('/run/secrets/git_username')
def tokenFile = new File('/run/secrets/git_token')

def domain = Domain.global()
def store = SystemCredentialsProvider.instance.store

if (usernameFile.length() && tokenFile.length()) {
    def newCredential = new UsernamePasswordCredentialsImpl(
        CredentialsScope.GLOBAL,
        credentialId,
        credentialDescription,
        usernameFile.text.trim(),
        tokenFile.text.trim()
    )
    def existingCredential = store.getCredentials(domain).find { it.id == credentialId }
    if (existingCredential) {
        store.updateCredentials(domain, existingCredential, newCredential)
        println "Successfully updated/overwrote credential '${credentialId}'"
    } else {
        store.addCredentials(domain, newCredential)
        println "Successfully created credential '${credentialId}'"
    }
} else {
    println 'WARNING: Git username and/or token secret missing or empty. Retaining any existing credentials'
}

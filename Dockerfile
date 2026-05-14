FROM jenkins/jenkins:2.555.2-lts-jdk25@sha256:8dcac4632bf2f5618ead336100f34866470a03b864a941c0ee9bd15cd56d8bf9

USER root

# Pin the Bitwarden CLI version for the Bitwarden Credentials Provider Plugin
ENV BW_CLI_VERSION="2026.4.1"

# Download the x86 BW CLI zip file directly from GitHub releases
RUN curl -Lso bw.zip "https://github.com/bitwarden/clients/releases/download/cli-v${BW_CLI_VERSION}/bw-oss-linux-${BW_CLI_VERSION}.zip" \
    && unzip bw.zip -d /usr/local/bin/ \
    && rm bw.zip \
    && chmod +x /usr/local/bin/bw

USER jenkins

COPY --chown=jenkins:jenkins plugins.txt /usr/share/jenkins/ref/plugins.txt

RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt
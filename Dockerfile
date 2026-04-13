FROM jenkins/jenkins:2.541.3-lts-jdk25@sha256:cec8050ffd7c7cd1bd4c838ffa04a04e43e5be648cfc9c970d3776584a8f3543

USER root

# Pin the Bitwarden CLI version for the Bitwarden Credentials Provider Plugin
ENV BW_CLI_VERSION="2026.3.0"

# Download the x86 BW CLI zip file directly from GitHub releases
RUN curl -Lso bw.zip "https://github.com/bitwarden/clients/releases/download/cli-v${BW_CLI_VERSION}/bw-oss-linux-${BW_CLI_VERSION}.zip" \
    && unzip bw.zip -d /usr/local/bin/ \
    && rm bw.zip \
    && chmod +x /usr/local/bin/bw

USER jenkins

COPY --chown=jenkins:jenkins plugins.txt /usr/share/jenkins/ref/plugins.txt

RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt
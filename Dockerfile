FROM jenkins/jenkins:2.568.2-lts-jdk25@sha256:731295021178803629eed771b57cbb4809a0bf76b1b2ef4d7497305a1aa80cef

USER root

# Pin the Bitwarden CLI version for the Bitwarden Credentials Provider Plugin
ARG BW_CLI_VERSION="2026.7.0"

# Download the x86 BW CLI zip file directly from GitHub releases
RUN curl -Lso bw.zip "https://github.com/bitwarden/clients/releases/download/cli-v${BW_CLI_VERSION}/bw-oss-linux-${BW_CLI_VERSION}.zip" \
    && unzip bw.zip -d /usr/local/bin/ \
    && rm bw.zip \
    && chmod +x /usr/local/bin/bw

USER jenkins

COPY --chown=jenkins:jenkins plugins.txt /usr/share/jenkins/ref/plugins.txt

RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt
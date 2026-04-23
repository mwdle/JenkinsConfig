FROM jenkins/jenkins:2.555.1-lts-jdk25@sha256:13940d71270188321522b240f16864ba931ed66696632c882a6b42536b1ded2f

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
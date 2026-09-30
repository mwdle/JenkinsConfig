FROM jenkins/jenkins:2.580.1-lts-jdk25@sha256:42fc298cdafa2683dddde9a7ccdce4058f771b1ec56c2423dbfca82f010a455f

USER root

# Pin the Bitwarden CLI version for the Bitwarden Credentials Provider Plugin
ARG BW_CLI_VERSION="2026.8.0"

# Download the x86 BW CLI zip file directly from GitHub releases
RUN curl -Lso bw.zip "https://github.com/bitwarden/clients/releases/download/cli-v${BW_CLI_VERSION}/bw-oss-linux-${BW_CLI_VERSION}.zip" \
    && unzip bw.zip -d /usr/local/bin/ \
    && rm bw.zip \
    && chmod +x /usr/local/bin/bw

USER jenkins

COPY --chown=jenkins:jenkins plugins.txt /usr/share/jenkins/ref/plugins.txt

RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt

HEALTHCHECK --interval=1m --timeout=5s --retries=3 --start-period=30s CMD curl -fSs -o /dev/null http://localhost:8080/health || exit 1
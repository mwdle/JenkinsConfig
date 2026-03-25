FROM jenkins/jenkins:2.541.3-lts-jdk25@sha256:cec8050ffd7c7cd1bd4c838ffa04a04e43e5be648cfc9c970d3776584a8f3543

COPY --chown=jenkins:jenkins plugins.txt /usr/share/jenkins/ref/plugins.txt

RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt
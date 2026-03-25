FROM jenkins/jenkins:2.541.3-lts-jdk21@sha256:c4098086090ca98491d4bf66182f5e3b015a8232f2acf2df209a212a5801aa8e

COPY --chown=jenkins:jenkins plugins.txt /usr/share/jenkins/ref/plugins.txt

RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt
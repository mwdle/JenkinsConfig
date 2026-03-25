FROM jenkins/jenkins:2.541.3-lts-jdk25

COPY --chown=jenkins:jenkins plugins.txt /usr/share/jenkins/ref/plugins.txt

RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt
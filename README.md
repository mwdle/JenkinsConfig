# Jenkins Configuration

This repository builds and configures Jenkins using JCasC and rootless Podman Quadlets.

Jenkins provisions agents through the isolated PinP Unix socket and stores its data in `~/containers/jenkins`. The `jenkins.network` Quadlet from the `podman-networks` repository is required.

```console
loginctl enable-linger "$USER"
```

## Podman secrets

Create these secrets as the rootless Quadlet user:

```text
jenkins_admin_username
jenkins_admin_password
jenkins_git_username
jenkins_git_token
jenkins_smtp_username
jenkins_smtp_password
jenkins_alert_email
```

## Local workstation test

```console
mkdir -p /home/mwdle/.config/containers/systemd
ln -sfnT /home/mwdle/Nextcloud/Server/jenkins-config /home/mwdle/.config/containers/systemd/jenkins-config
ln -sfnT /home/mwdle/Nextcloud/Server/podman-networks /home/mwdle/.config/containers/systemd/podman-networks
systemctl --user daemon-reload
systemctl --user start jenkins.service
```

Jenkins is available at <http://localhost:8080>.

```console
systemctl --user status jenkins.service
podman logs --follow jenkins
```

Rebuild after changing image inputs:

```console
systemctl --user restart jenkins-build.service
systemctl --user restart jenkins.service
```

Reload and restart after changing Quadlets, JCasC, or secrets.

# Jenkins Configuration

This repository builds and configures Jenkins using JCasC. `jenkins.container` runs it as a rootless Podman Quadlet; `compose.yaml` remains temporarily during the migration.

## Requirements

Jenkins uses the rootless Podman socket to provision dynamic agents through the Docker-compatible API. Enable the socket and allow the user service manager to start at boot:

```console
systemctl --user enable --now podman.socket
loginctl enable-linger "$USER"
```

Socket access gives Jenkins control over every Podman resource owned by the host user. SELinux label separation is disabled only for the Jenkins controller because the standard container policy blocks access to the socket. The controller remains rootless with a read-only root filesystem, dropped capabilities, `NoNewPrivileges`, resource limits, and no socket access from agent containers.

Jenkins also requires the centrally managed `jenkins.network` Quadlet from the `podman-networks` repository.

Mutable Jenkins data is stored in `~/containers/jenkins`. JCasC is mounted read-only from this repository.

## Environment credential

Use `.env.example` as the list of required variables. Podman does not remove quotes or expand references inside environment files, so enter final, unquoted values. Jenkins-specific derived values are composed in JCasC.

The complete environment is encrypted as a systemd credential named `environment` and stored as `~/.config/credstore.encrypted/JenkinsConfig.env.cred`.

## Local workstation test

Link the development repositories into Quadlet's rootless search path:

```console
mkdir -p /home/mwdle/.config/containers/systemd
mkdir -p /home/mwdle/.config/credstore.encrypted
ln -sfnT /home/mwdle/Nextcloud/Server/JenkinsConfig /home/mwdle/.config/containers/systemd/JenkinsConfig
ln -sfnT /home/mwdle/Nextcloud/Server/podman-networks /home/mwdle/.config/containers/systemd/podman-networks
```

Encrypt the environment directly from standard input:

```console
stty -echo
systemd-creds encrypt --user --name=environment - /home/mwdle/.config/credstore.encrypted/JenkinsConfig.env.cred
stty echo
```

Paste the complete environment, press Enter after its final line, and then press `Ctrl+D`. If the command is interrupted while terminal echo is disabled, run `stty echo`.

Build and start Jenkins:

```console
systemctl --user enable --now podman.socket
podman build --tag localhost/jenkins:latest /home/mwdle/Nextcloud/Server/JenkinsConfig
systemctl --user daemon-reload
systemctl --user start jenkins.service
```

Jenkins is available at <http://localhost:8080>. Check it with:

```console
systemctl --user status jenkins.service
podman logs --follow jenkins
```

After changing the image, Quadlet, JCasC, or encrypted environment, rebuild or replace the affected input and restart the service:

```console
podman build --tag localhost/jenkins:latest /home/mwdle/Nextcloud/Server/JenkinsConfig
systemctl --user daemon-reload
systemctl --user restart jenkins.service
```

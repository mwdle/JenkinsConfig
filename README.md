# Jenkins Configuration

This repository builds and configures Jenkins using JCasC and rootless Podman Quadlets. `compose.yaml` remains temporarily during the migration.

Jenkins uses the Docker-compatible API provided through the isolated `pinp.container` Unix socket to provision agents. The dedicated `pinp-socket.volume` Quadlet declares the named volume shared only by PinP and Jenkins; Jenkins does not access the host Podman socket. The centrally managed `jenkins.network` Quadlet from the `podman-networks` repository is required.

Jenkins data is stored in `~/containers/jenkins`, while JCasC is mounted read-only from this repository. Enable lingering if the user services must start before login:

```console
loginctl enable-linger "$USER"
```

## Environment credential

Use `.env.example` as the list of required variables. Enter final, unquoted values because Podman environment files do not remove quotes or expand references.

The complete environment is encrypted as the systemd credential `~/.config/credstore.encrypted/jenkins-config.env.cred`.

## Local workstation test

Link the repositories into Quadlet's rootless search path:

```console
mkdir -p /home/mwdle/.config/containers/systemd
mkdir -p /home/mwdle/.config/credstore.encrypted
ln -sfnT /home/mwdle/Nextcloud/Server/jenkins-config /home/mwdle/.config/containers/systemd/jenkins-config
ln -sfnT /home/mwdle/Nextcloud/Server/podman-networks /home/mwdle/.config/containers/systemd/podman-networks
```

Encrypt the environment directly from standard input:

```console
stty -echo
systemd-creds encrypt --user --name=environment - /home/mwdle/.config/credstore.encrypted/jenkins-config.env.cred
stty echo
```

Paste the complete environment, press Enter after its final line, and then press `Ctrl+D`. If the command is interrupted while terminal echo is disabled, run `stty echo`.

Load the Quadlets and start Jenkins. Its image, PinP service, and networks start automatically through systemd dependencies:

```console
systemctl --user daemon-reload
systemctl --user start jenkins.service
```

Jenkins is available at <http://localhost:8080>.

```console
systemctl --user status jenkins.service
podman logs --follow jenkins
```

After changing the Dockerfile or another image input, rebuild and restart Jenkins:

```console
systemctl --user restart jenkins-build.service
systemctl --user restart jenkins.service
```

After changing a Quadlet, JCasC, or the encrypted environment, reload systemd and restart the affected service.

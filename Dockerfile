FROM jenkins/jenkins:lts

RUN jenkins-plugin-cli --plugins \
    bitwarden-credentials-provider:254.vd33c35d1b_fb_5 \
    configuration-as-code:2037.v8e5349845172 \
    dark-theme:574.va_19f05d54df5 \
    docker-plugin:1308.vff6e33248305 \
    job-dsl:3654.vdf58f53e2d15 \
    git:5.10.0 \
    gitea:268.v75e47974c01d \
    pipeline-model-definition:2.2277.v00573e73ddf1 \
    pipeline-stage-view:2.39 \
    pipeline-utility-steps:2.20.0 \
    inline-pipeline:1.0.32.vf433f2d57630 \
FROM jenkins/jenkins:lts

RUN jenkins-plugin-cli --plugins \
    configuration-as-code:1998.v3e50e6e9d9d3 \
    job-dsl:1.93 \
    git:5.8.0 \
    gitea:250.v76a_0b_d4fef5b_ \
    docker-plugin:1274.vc0203fdf2e74 \
    pipeline-model-definition:2.2273.v643f36ed9e94 \
    pipeline-stage-view:2.38 \
    pipeline-utility-steps:2.20.0 \
    inline-pipeline:1.0.32.vf433f2d57630 \
    dark-theme:574.va_19f05d54df5 \
    bitwarden-credentials-provider:215.vff8230a_06dd8
FROM jenkins/jenkins:lts

RUN jenkins-plugin-cli --plugins \
    configuration-as-code:1985.vdda_32d0c4ea_b_ \
    job-dsl:1.93 \
    git:5.7.0 \
    gitea:250.v76a_0b_d4fef5b_ \
    docker-plugin:1274.vc0203fdf2e74 \
    pipeline-model-definition:2.2273.v643f36ed9e94 \
    pipeline-stage-view:2.38 \
    pipeline-utility-steps:2.19.0 \
    inline-pipeline:1.0.32.vf433f2d57630 \
    dark-theme:574.va_19f05d54df5 \
    bitwarden-credentials-provider:latest
FROM mcr.microsoft.com/devcontainers/java:1-21-bullseye

# Install Maven
ARG MAVEN_VERSION=3.9.5
RUN su vscode -c "umask 0002 && . /usr/local/sdkman/bin/sdkman-init.sh && sdk install maven ${MAVEN_VERSION}"

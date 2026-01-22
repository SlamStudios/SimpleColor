#!/bin/sh
# Simple Maven Wrapper Lite
set -e
export MAVEN_VERSION="4.0.0-rc-1"
export MAVEN_URL="https://repo.maven.apache.org"

if [ ! -d "$HOME/.m2/wrapper/dists/maven-$MAVEN_VERSION" ]; then
    echo "Downloading Maven $MAVEN_VERSION..."
    mkdir -p "$HOME/.m2/wrapper/dists"
    curl -L "$MAVEN_URL" -o maven.zip
    unzip -q maven.zip -d "$HOME/.m2/wrapper/dists"
    mv "$HOME/.m2/wrapper/dists/apache-maven-$MAVEN_VERSION" "$HOME/.m2/wrapper/dists/maven-$MAVEN_VERSION"
    rm maven.zip
fi

exec "$HOME/.m2/wrapper/dists/maven-$MAVEN_VERSION/bin/mvn" "$@"
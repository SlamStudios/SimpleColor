#!/bin/sh
# Simple Maven Wrapper
set -e
MAVEN_VERSION="3.9.6"
MAVEN_URL="https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.zip"

if [ ! -d "$HOME/.m2/wrapper/dists/maven-$MAVEN_VERSION" ]; then
    echo "Downloading Maven $MAVEN_VERSION..."
    mkdir -p "$HOME/.m2/wrapper/dists"
    curl -fsSL "$MAVEN_URL" -o maven.zip
    unzip -q maven.zip -d "$HOME/.m2/wrapper/dists"
    mv "$HOME/.m2/wrapper/dists/apache-maven-$MAVEN_VERSION" "$HOME/.m2/wrapper/dists/maven-$MAVEN_VERSION"
    rm maven.zip
fi

exec "$HOME/.m2/wrapper/dists/maven-$MAVEN_VERSION/bin/mvn" "$@"
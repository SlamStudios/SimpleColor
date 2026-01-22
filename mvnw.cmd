@echo off
set MAVEN_VERSION=4.0.0-rc-1
set MAVEN_URL=https://repo.maven.apache.org
set MAVEN_HOME=%USERPROFILE%\.m2\wrapper\dists\maven-%MAVEN_VERSION%

if not exist "%MAVEN_HOME%" (
    echo Downloading Maven %MAVEN_VERSION%...
    powershell -Command "Invoke-WebRequest -Uri '%MAVEN_URL%' -OutFile 'maven.zip'"
    powershell -Command "Expand-Archive -Path 'maven.zip' -DestinationPath '%USERPROFILE%\.m2\wrapper\dists'"
    move "%USERPROFILE%\.m2\wrapper\dists\apache-maven-%MAVEN_VERSION%" "%MAVEN_HOME%"
    del maven.zip
)

"%MAVEN_HOME%\bin\mvn.cmd" %*
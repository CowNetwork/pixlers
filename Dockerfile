FROM maven:3.8.1-jdk-11 AS builder
ARG MAVEN_PASS
ARG MAVEN_USER
WORKDIR /build
COPY . .
RUN --mount=type=cache,target=/root/.m2 mvn clean install -Dbuild.mvn.user=$MAVEN_USER -Dbuild.mvn.pass=$MAVEN_PASS --settings deploy/settings.xml

# TODO: minigame-base
FROM ghcr.io/cownetwork/spigot-base:v0.27.1
COPY --from=builder /build/pixlers-game/target/pixlers-*.jar /opt/spigot/plugins/pixlers.jar
COPY --from=builder /build/deploy/ /opt/spigot

FROM maven:3.8.7-eclipse-temurin-11 as builder

WORKDIR /app
COPY pom.xml .
COPY src ./src
# Use ARG values in environment variables to ensure they're available in mvnw command
ENV GH_USER=$GITHUB_ACTOR
ENV GH_TOKEN=$GITHUB_TOKEN

# Generate settings.xml dynamically to include GitHub credentials
RUN echo "<settings xmlns='http://maven.apache.org/SETTINGS/1.0.0' \
      xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' \
      xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 \
                          https://maven.apache.org/xsd/settings-1.0.0.xsd'>\
      <servers>\
        <server>\
          <id>github</id>\
          <username>${GH_USER}</username>\
          <password>${GH_TOKEN}</password>\
        </server>\
      </servers>\
    </settings>" > /opt/app/settings.xml

RUN mvn clean install -DskipTests --settings /opt/app/settings.xml

FROM eclipse-temurin:11-jdk

WORKDIR /opt/app
COPY --from=builder /app/target/gnx-gcn-connector-*.jar /opt/app/gnx-gcn-connector.jar
COPY --chmod=755 entrypoint.sh /opt/app/

CMD ["./entrypoint.sh"]
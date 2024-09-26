FROM maven:3.8.7-eclipse-temurin-11 as builder

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean install -DskipTests

FROM eclipse-temurin:11-jdk

WORKDIR /opt/app
COPY --from=builder /app/target/gnx-gcn-connector-*.jar /opt/app/gnx-gcn-connector.jar
COPY --chmod=755 entrypoint.sh /opt/app/

CMD ["./entrypoint.sh"]
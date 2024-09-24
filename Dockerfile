FROM eclipse-temurin:11-jdk
COPY target/gnx-gcn-connector-testapp-java-*.jar gnx-gcn-connector-testapp-java.jar

ENTRYPOINT ["java", "-jar", "/gnx-gcn-connector-testapp-java.jar"]
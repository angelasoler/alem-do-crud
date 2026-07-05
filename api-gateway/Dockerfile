FROM eclipse-temurin:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djdk.management.container.cgroup.disabled=true","-jar","/app.jar"]

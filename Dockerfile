FROM maven:3.9-eclipse-temurin-8 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn -B clean package -DskipTests

FROM tomcat:9.0-jdk8-temurin

# Deploy the WAR as the root application so the public URL is /index.html,
# not /Project_Vaseis/index.html.
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/Project_Vaseis-1.0-SNAPSHOT.war \
     /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]

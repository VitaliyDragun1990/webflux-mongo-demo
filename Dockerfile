# Stage 1
# Start with a base image containing JDK
FROM adoptopenjdk/openjdk11:jdk-11.0.10_9-slim as build

# Add Maintainer Info
LABEL mainteiner="Vitalii Drahun <vdrag00n90@gmail.com>"

# The application's jar file, will be set by maven docker plugin
ARG JAR_FILE

# Add the application's jar to the container
COPY ${JAR_FILE} app.jar

# Unpackage the jar file
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf /app.jar)

# Stage 2
# Same Java runtime
FROM adoptopenjdk/openjdk11:jre-11.0.10_9-alpine

# Add volume pointing to /tmp
VOLUME /tmp

# Copy unpackaged application to new container
ARG DEPENDENCY=/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Execute the application on container startup
ENTRYPOINT ["java","-cp","app:app/lib/*","org.vdragun.webfluxmongo.RestApiWithWebFluxAndMongoApplication"]
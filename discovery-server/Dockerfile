FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

# Forme shell (pas la forme exec) - necessaire pour que $JAVA_OPTS soit
# interprete par le shell et transmis a la JVM. Sans JAVA_OPTS defini,
# ${JAVA_OPTS} est vide, sans consequence (comportement par defaut de la JVM).
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

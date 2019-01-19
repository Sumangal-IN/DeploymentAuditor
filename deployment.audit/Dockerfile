FROM openjdk:8-jdk-slim
ENV PORT 9090
EXPOSE 9090

COPY deployment.audit/build/libs/*.jar /opt/app.jar
WORKDIR /opt
CMD ["java", "-jar", "app.jar"]
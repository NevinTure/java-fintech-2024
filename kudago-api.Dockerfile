FROM openjdk:21
COPY ./kudago-api/build/libs/kudago-api-0.0.1-SNAPSHOT.jar /kudago-api.jar
EXPOSE 8443
ENTRYPOINT ["java","-jar","/kudago-api.jar"]
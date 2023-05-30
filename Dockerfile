FROM openjdk:17

RUN echo "Africa/South Africa" > /etc/timezone

RUN date

RUN mkdir home/resources

VOLUME src/main/resources /home/resources

COPY target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]

EXPOSE 8080

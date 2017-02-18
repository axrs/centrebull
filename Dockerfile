FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/centrebull.jar /centrebull/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/centrebull/app.jar"]

FROM adoptopenjdk/openjdk8:alpine
MAINTAINER Jesse Wu <wyyxwzx@163.com>

# Add the service itself
ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/blog-service.jar

ENTRYPOINT ["java", "-jar", "/usr/share/blog-service.jar"]

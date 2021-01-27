# 镜像基础
FROM java:8

# 默认使用/tmp作为工作目录
VOLUME /tmp

# 作者信息
MAINTAINER "Jesse Wu <wyyxwzx@163.com>"

# 暴露端口
EXPOSE 8081

ARG JAR_FILE=blog-0.0.1-SNAPSHOT.jar

# 往容器中添加jar包
ADD ${JAR_FILE} blog-service.jar

# 启动镜像自动运行程序
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=pro", "-jar", "/blog-service.jar"]

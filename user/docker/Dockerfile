# Docker 镜像构建
FROM java:8
# 作者
MAINTAINER HYZ
ADD user-0.0.1-SNAPSHOT.jar user.jar
# 端口号
EXPOSE 9090
ENTRYPOINT ["java","-jar","user.jar"]
# Docker 镜像构建
FROM java:8
# 作者
MAINTAINER HYZ
ADD social-0.0.1-SNAPSHOT.jar social.jar
# 端口号
EXPOSE 9092
ENTRYPOINT ["java","-jar","social.jar"]
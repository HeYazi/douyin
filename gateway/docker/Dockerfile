# Docker 镜像构建
FROM java:8
# 作者
MAINTAINER HYZ
ADD gateway-0.0.1-SNAPSHOT.jar gateway.jar
# 端口号
EXPOSE 10010
ENTRYPOINT ["java","-jar","gateway.jar"]
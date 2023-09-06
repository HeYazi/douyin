# Docker 镜像构建
FROM java:8
# 作者
MAINTAINER HYZ
ADD interaction-0.0.1-SNAPSHOT.jar interaction.jar
# 端口号
EXPOSE 9091
ENTRYPOINT ["java","-jar","interaction.jar"]
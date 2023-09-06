# Docker 镜像构建
FROM maven:3.6.3-jdk-8-slim as builder

# 指定工作目录，改项目将存放与对应工作目录下
WORKDIR /common
#
COPY pom.xml .
COPY src ./src

# Build a release artifact.
RUN mvn package -DskipTests
# Sử dụng image Java 8 từ Docker Hub
FROM openjdk:17-jdk-alpine
# Đặt thư mục làm việc trong container
WORKDIR /opt
# Sao chép tất cả các tệp từ thư mục hiện tại vào thư mục /app trong container
COPY target/*.jar /opt/app.jar
# Biên dịch và đóng gói ứng dụng Spring Boot
# RUN ./mvnw package
# Chạy ứng dụng Spring Boot khi khởi động container
ENV PORT 8080
EXPOSE 8080
ENTRYPOINT exec java $JAVA_OPTS -jar /opt/app.jar
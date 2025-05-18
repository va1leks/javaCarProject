FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .

# Даем права на выполнение mvnw
RUN chmod +x mvnw

# Теперь можно запускать
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "--enable-preview", "-jar", "app.jar"]
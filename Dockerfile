# Prosty Dockerfile
FROM eclipse-temurin:17-jdk-alpine

# Instalujemy Maven
RUN apk add --no-cache maven

# Ustawiamy katalog roboczy
WORKDIR /app

# Kopiujemy wszystko
COPY . .

# Budujemy aplikację
RUN mvn clean package -DskipTests

# Otwieramy port
EXPOSE 9090

# Uruchamiamy aplikację (znajdź JAR automatycznie)
CMD ["sh", "-c", "java -jar target/*.jar"]
# Étape 1 : Construire l'application avec Maven et OpenJDK 17
FROM maven:3.8-openjdk-17 AS builder
WORKDIR /app

# Copier le fichier pom.xml et télécharger les dépendances Maven
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copier le code source de l'application
COPY src ./src

# Construire l'application
RUN mvn clean package -DskipTests

# Étape 2 : Exécuter l'application avec OpenJDK 17
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copier le fichier JAR généré par Maven
COPY --from=builder /app/target/*.jar app.jar

# Exposer le port utilisé par Spring Boot
EXPOSE 9197

# Définir la commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]


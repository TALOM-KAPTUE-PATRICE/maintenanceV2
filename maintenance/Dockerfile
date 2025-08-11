# =================================================================
# STAGE 1: Build the application using Maven
# Utilise une image officielle Maven avec JDK 21 pour la compilation
# =================================================================
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Définit le répertoire de travail à l'intérieur du conteneur
WORKDIR /app

# Copie le fichier pom.xml pour télécharger les dépendances en premier.
# Cela tire parti du cache de Docker : si les dépendances ne changent pas, cette couche n'est pas ré-exécutée.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copie le reste du code source de l'application
COPY src ./src

# Compile l'application et crée le fichier JAR exécutable.
# -DskipTests accélère le build en production.
RUN mvn package -DskipTests


# =================================================================
# STAGE 2: Run the application in a lightweight JRE image
# Utilise une image JRE officielle beaucoup plus légère pour l'exécution
# =================================================================
FROM eclipse-temurin:21-jre-jammy

# Définit le répertoire de travail
WORKDIR /app

# Crée un utilisateur non-root pour une meilleure sécurité
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# Copie le fichier JAR compilé depuis le stage 'build'
COPY --from=build /app/target/maintenance-0.0.1-SNAPSHOT.jar app.jar

# Expose le port sur lequel l'application va tourner.
# Render utilisera la variable d'environnement PORT, mais c'est une bonne pratique de le documenter.
EXPOSE 8081

# Commande pour lancer l'application au démarrage du conteneur
ENTRYPOINT ["java","-jar","/app/app.jar"]
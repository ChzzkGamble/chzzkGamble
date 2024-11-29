# Use an official OpenJDK image as a base 
FROM openjdk:17-jdk-slim 

# Set the working directory in the container 
WORKDIR /app 

# Copy your JAR file into the container (adjust the path to your JAR file) 
COPY ./**/*.jar /app/chzonme-application.jar 

# Expose any ports the application might need (optional) 
EXPOSE 8080 

# Run the application 
CMD ["java", "-jar", "-Duser.timezone=Asia/Seoul", "chzonme-application.jar"]
FROM openjdk:17.0.1 as builder

WORKDIR /app/msvc-clients

COPY ./pom.xml /app
COPY ./msvc-clients/.mvn ./.mvn
COPY ./msvc-clients/mvnw .
COPY ./msvc-clients/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./msvc-clients/src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17.0.1

WORKDIR /app

COPY --from=builder /app/msvc-clients/target/msvc-clients-0.0.1-SNAPSHOT.jar .

EXPOSE 8002

CMD ["java","-jar","msvc-clients-0.0.1-SNAPSHOT.jar"]


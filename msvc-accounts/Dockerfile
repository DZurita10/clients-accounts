FROM openjdk:17.0.1 as builder

WORKDIR /app/msvc-accounts

COPY ./pom.xml /app
COPY ./msvc-accounts/.mvn ./.mvn
COPY ./msvc-accounts/mvnw .
COPY ./msvc-accounts/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./msvc-accounts/src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17.0.1

WORKDIR /app

COPY --from=builder /app/msvc-accounts/target/msvc-accounts-0.0.1-SNAPSHOT.jar .

EXPOSE 8003

CMD ["java","-jar","msvc-accounts-0.0.1-SNAPSHOT.jar"]


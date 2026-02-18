FROM openjdk:21-jdk
COPY ../build/libs/ms.book-store.jar /app/
WORKDIR /app/
ENTRYPOINT ["java"]
CMD ["-jar", "ms.book-store.jar"]
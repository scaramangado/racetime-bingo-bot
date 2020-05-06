FROM adoptopenjdk/openjdk14:alpine

COPY build/libs/bingobot.jar /usr/bin

RUN mkdir /etc/bingobot
VOLUME ["/etc/bingobot"]
WORKDIR /etc/bingobot

CMD ["java", "-jar", "/usr/bin/bingobot.jar"]

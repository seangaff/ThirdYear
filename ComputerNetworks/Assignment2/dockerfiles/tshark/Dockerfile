#Tshark comtainer
FROM arm64v8/ubuntu:latest
#WORKDIR /src
#COPY /src /src
EXPOSE 51510
RUN apt-get update -y && apt-get install tshark -y && apt-get install openjdk-17-jdk -y
CMD [ "/bin/bash" ]
#tshark -i any -f "udp" -w "result.pcap"
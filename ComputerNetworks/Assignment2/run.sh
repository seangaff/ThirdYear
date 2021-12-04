#Run Simulated Network

docker build -t flowctrl ./dockerfiles/flowctrl
docker build -t tshark ./dockerfiles/tshark

docker-compose up


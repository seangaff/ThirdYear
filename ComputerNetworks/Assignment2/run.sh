#Run Simulated Network

docker build -t flowctrl ./dockerfiles/flowctrl
docker build -t tshark ./dockerfiles/tshark

docker-compose up

#Each of the follow must be done in a seperate window


#You can open the cli for this container seperately to record traffic
#you must quit the process before running docker compose down
    # docker exec -it Router 0 bash
    # cd /src && tshark -i any -f "udp" -w "result.pcap"



#To Interact with Application0
    #  docker exec -it Endpoint0 bash
    #  java -cp /src Application Bill


#To Interact with Application1
    #  docker exec -it Endpoint1 bash
    #  java -cp /src Application Ted

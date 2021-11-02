//Sean Gaffney
//id: 19304695

import java.net.DatagramPacket;
import java.net.DatagramSocket;
//import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;

public class Broker extends Node {

    ArrayList<InetSocketAddress> temperature;
    ArrayList<InetSocketAddress> humidity;
    int packetCount;

    Broker(int port) {
        try {
            socket = new DatagramSocket(port);
            listener.go();
            temperature = new ArrayList<InetSocketAddress>();
            humidity = new ArrayList<InetSocketAddress>();
            packetCount = 0;
            temperature.add(subscriberAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void onReceipt(DatagramPacket packet) {
        try {
            packetCount++;
            System.out.println("Received packet: " + packetCount);

            DatagramPacket response;
            response = new AckPacketContent("OK - Received this").toDatagramPacket();
            response.setSocketAddress(packet.getSocketAddress());
            socket.send(response);

			PacketContent content= PacketContent.fromDatagramPacket(packet);
            int packetType = content.getType();
            int packetTopic = content.getTopic();
            switch(packetType) { 
                case PacketContent.ACKPACKET:
                    System.out.print("Received Ack packet");
                    break;
                case PacketContent.MESSAGE:
                    System.out.println("Received Message packet, forwarding to subscribers");
                    DatagramPacket copyPacket = packet;
                    switch(packetTopic) {
                        case PacketContent.TEMP:
                            System.out.println("Forwarded to TEMP");
                            for(InetSocketAddress i : temperature) {
                                copyPacket.setSocketAddress(i);
                                socket.send(copyPacket);
                            }
                            break;
                        case PacketContent.HUMIDITY:
                            System.out.println("Forwarded to HUMIDITY");
                            for(InetSocketAddress i : humidity) {
                                copyPacket.setSocketAddress(i);
                                socket.send(copyPacket);
                            }
                            break;
                        case PacketContent.NOTOP:
                        default:
                            System.err.println("Error: Unexpected packet received (topic error)");
                            break;
                    }
                    break;
                case PacketContent.SUBPACKET:
                    System.out.println("Received  Sub packet, adding sender to subscribers");
                    if(packetTopic == PacketContent.TEMP) {
                        temperature.add((InetSocketAddress) packet.getSocketAddress());
                        System.out.print(""+temperature.size());
                    } else if (packetTopic == PacketContent.HUMIDITY) {
                        temperature.add((InetSocketAddress) packet.getSocketAddress());
                        System.out.print(""+humidity.size());
                    }
                    else {
                        System.err.println("Error: Unexpected packet received (invalid topic)");
                    }
                    break;
                default:
                   System.err.println("Error: Unexpected packet received (invalid type)");
                   break;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public synchronized void start() throws Exception {
		System.out.println("Waiting for contact");
		this.wait();
	}

    public static void main(String[] args) {
        try {
            (new Broker(BROKER_PORT)).start();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}

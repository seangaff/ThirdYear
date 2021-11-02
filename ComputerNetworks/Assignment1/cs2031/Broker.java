//Sean Gaffney
//id: 19304695

import java.net.DatagramPacket;
import java.net.DatagramSocket;
//import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;

public class Broker extends Node {

    ArrayList<InetSocketAddress> TopicA;
    ArrayList<InetSocketAddress> TopicB;
    int packetCount;

    Broker(int port) {
        try {
            socket = new DatagramSocket(port);
            listener.go();
            TopicA = new ArrayList<InetSocketAddress>();
            TopicB = new ArrayList<InetSocketAddress>();
            packetCount = 0;
            TopicA.add(serverAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void onReceipt(DatagramPacket packet) {
        try {
            System.out.println("Received packet: " + packetCount);

            DatagramPacket response;
            response = new AckPacketContent("OK - Received this").toDatagramPacket();
            response.setSocketAddress(packet.getSocketAddress());
            socket.send(response);

			PacketContent content= PacketContent.fromDatagramPacket(packet);
            int packetType = content.getType();
            int packetTopic = PacketContent.TEMP;
            switch(packetType) { 
                case PacketContent.ACKPACKET:
                    System.out.print("Received Ack packet");
                    break;
                case PacketContent.MESSAGE:
                    System.out.println("Received FileInfo packet, sending to subscribers");
                    DatagramPacket copyPacket = packet;
                    switch(packetTopic) {
                        case PacketContent.TEMP:
                            copyPacket.setSocketAddress(serverAddress);
                            socket.send(copyPacket);
                            System.out.println("Forwarded to Subscriber");
                            break;
                        case PacketContent.HUMIDITY:
                            for(InetSocketAddress i : TopicB) {
                                copyPacket.setSocketAddress(i);
                                socket.send(copyPacket);
                            }
                            break;
                        case PacketContent.NOTOP:
                        default:
                            System.err.println("Error: Unexpected packet received");
                            break;
                    }
                    break;
                case PacketContent.SUBPACKET:
                    System.out.println("Received  Sub packet, adding sender to subscribers");
                    break;
                case PacketContent.FILEINFO:
                default:
                   System.err.println("Error: Unexpected packet received");
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

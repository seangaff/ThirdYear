import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;

public class Broker extends Node {

    ArrayList<InetSocketAddress> TopicA;
    ArrayList<InetSocketAddress> TopicB;

    private InetSocketAddress serverAddress = new InetSocketAddress("server", SERVER_PORT);
    private InetSocketAddress clientAddress = new InetSocketAddress("client", SUBSCRIBER_PORT);

    Broker(int port) {
        try {
            socket = new DatagramSocket(port);
            listener.go();
            TopicA = new ArrayList<InetSocketAddress>();
            TopicB = new ArrayList<InetSocketAddress>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void onReceipt(DatagramPacket packet) {
        try {
            System.out.println("Received packet");
			PacketContent content= PacketContent.fromDatagramPacket(packet);
            int packetType = content.getType();
            int packetTopic = PacketContent.TOPA;//content.getTopic();
            TopicA.add(serverAddress);
            switch(packetType) { 
                case PacketContent.ACKPACKET:
                    System.out.print("Received Ack packet");
                    break;
                case PacketContent.FILEINFO:
                    System.out.println("Received FileInfo packet, sending to subscribers");
                    DatagramPacket copyPacket = packet;
                    switch(packetTopic) {
                        case PacketContent.TOPA:
                            for(InetSocketAddress i : TopicA) {
                                copyPacket.setSocketAddress(i);
                                socket.send(copyPacket);
                            }
                            break;
                        case PacketContent.TOPB:
                            for(InetSocketAddress i : TopicB) {
                                packet.setSocketAddress(i);
                                socket.send(packet);
                            }
                            break;
                        case PacketContent.NOTOP:
                        default:
                            System.err.println("Error: Unexpected packet received");
                            break;
                    }

                    DatagramPacket response;
                    response = new AckPacketContent("OK - Received this").toDatagramPacket();
                    response.setSocketAddress(packet.getSocketAddress());
                    socket.send(response);

                    
                    break;
                case PacketContent.SUBPACKET:
                    System.out.println("Received  Sub packet, adding sender to subscribers");
                    break;
                default:
                   System.err.println("Error: Unexpected packet received");
                   break;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private synchronized void start() {
        try {
            this.wait();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Broker broker = new Broker(BROKER_PORT);
            System.out.println("Waiting for Contact...");
            broker.start();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}

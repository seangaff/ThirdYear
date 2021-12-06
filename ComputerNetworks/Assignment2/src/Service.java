import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
//import java.net.SocketAddress;
import java.net.InetAddress;
import java.util.HashMap;

public class Service extends Node {

    String designation;
    InetAddress localAddress;
    InetSocketAddress application;
    InetSocketAddress router;
    HashMap<String, InetSocketAddress> flowTable;

    Service(String designation) {
		try {
            socket= new DatagramSocket(DEFAULT_PORT);
			listener.go();
            this.designation = designation;
            localAddress = InetAddress.getLocalHost();
            flowTable = new HashMap<String, InetSocketAddress>();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

    public synchronized void onReceipt(DatagramPacket packet) {
        try {
            InetSocketAddress application = new InetSocketAddress(localAddress, APP_PORT);
            PacketContent contentS = new PacketContent(packet);
            int packetType = contentS.getType();
            String destination = contentS.getDestination();
            if(APP_PORT == packet.getPort()) {
                System.out.println("Packet Recieved From Local App");
                switch(packetType) {
                    case PacketContent.MESSAGE:
                        if(flowTable.containsKey(destination)) {
                            //System.out.println(flowTable.get(destination).toString());
                            packet.setSocketAddress(flowTable.get(destination));
                            socket.send(packet);
                            System.out.println("Forwarded Message");
                        }
                        else {
                            System.out.println(""+destination);
                            for (String i : flowTable.keySet()) {
                                System.out.println(i);
                              }
                            System.out.println("Dropped Message");
                        }
                        break;
                    case PacketContent.APPREQ:
                        System.out.println("Setting App Destination");
                        if(destination.equals("Bill")) {
                            flowTable.put("Bill",application);
                            flowTable.put("Ted", R0address);
                        }
                        else if (destination.equals("Ted")) {
                            flowTable.put("Ted",application);
                            flowTable.put("Bill", R2address);
                            //flowTable.put("Bill", R3address);
                        }
                        else {
                            System.out.println("Error: "+destination);
                        }
                        break;
                    default:
                        System.out.println("Invalid message type:" + packetType);
                }
            }
            else {
                System.out.println("Packet Recieved From Network");
                switch(packetType) {
                    case PacketContent.ACK:
                        System.out.println("Recieved ACK");
                        break;
                    case PacketContent.MESSAGE:
                        //System.out.println("Recieved Message");
                        if(flowTable.containsKey(destination)) {
                            packet.setSocketAddress(application);
                            socket.send(packet);
                            System.out.println("Forwarded Message");
                        }
                        else {
                            System.out.println("Dropped Message");
                        }
                        break;
                    default:
                        System.out.println("Invalid message type");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void start() {
        try {
            System.out.println("Waiting for contact");
            this.wait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        try {
            String designation = args[0];
            designation = "Endpoint" + designation;
            (new Service(designation)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

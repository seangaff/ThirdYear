import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
//import java.net.SocketAddress;
//import java.util.Arrays;
import java.util.HashMap;

public class Router extends Node{

    HashMap<String, InetSocketAddress> flowTable;
    String designation;
    DatagramPacket temp;

    Router(String designation) {
		try {
            this.designation = designation;
			socket= new DatagramSocket(DEFAULT_PORT);
			listener.go();
            flowTable = new HashMap<String, InetSocketAddress>();
            if(designation.equals("Router0")) {
                flowTable.put("Bill", E0address);
            }
            else if(designation.equals("Router2") || designation.equals("Router2")) {
                flowTable.put("Ted",E1address);
            }
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

    public synchronized void onReceipt(DatagramPacket packet) {
        try {
            PacketContent content = new PacketContent(packet);
            int packetType = content.getType();
            String destination = content.getDestination();
            switch(packetType) {
                case PacketContent.ACK:
                    System.out.println("Recieved ACK");
                    break;
                case PacketContent.MESSAGE:
                    System.out.println("Recieved Message");
                    if(flowTable.containsKey(destination)) {
                        packet.setSocketAddress(flowTable.get(destination));
                        socket.send(packet);
                        System.out.println("Forwarded Message:" + flowTable.get(destination));
                    }                   
                    else {
                        DatagramPacket requestPac = new PacketContent(PacketContent.FLOWREQ,designation+destination,"").toDatagramPacket();
                        requestPac.setSocketAddress(controllerAddress);
                        socket.send(requestPac);
                        temp = packet;
                        System.out.println("Route unknown, contacting Controller");
                    }
                    break;
                case PacketContent.TABLEUPDATE:
                 System.out.println("Recieved update to flow Table");
                    String message = new String(content.getDestination());
                    if(message.equals("null")) {
                        System.out.println("No was route found: Dropped Message");
                        break;
                    }
                    else {
                        InetSocketAddress next = new InetSocketAddress(message, DEFAULT_PORT);
                        flowTable.put(content.getDestination(),next);
                        temp.setSocketAddress(next);
                        socket.send(temp);
                        System.out.println("Forwarded Message:" + content.getMessage());
                    }
                    break;
                default:
                    System.out.println("Invalid message type");
                    break;
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
            int num = Integer.parseInt(args[0]);
            String designation = "Router" + num;
            (new Router(designation)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

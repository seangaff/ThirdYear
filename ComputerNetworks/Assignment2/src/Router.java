import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.HashMap;

public class Router extends Node{

    HashMap<String, InetSocketAddress> flowTable;
    String designation;
    byte[] temp;

    Router(String designation) {
		try {
            this.designation = designation;
			socket= new DatagramSocket(DEFAULT_PORT);
			listener.go();
            flowTable = new HashMap<String, InetSocketAddress>();
            flowTable.put("Ted",E1address);
            flowTable.put("Bill", E0address);
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
                        System.out.println("Dropped Message");
                        //contact controller
                        //wait
                        //add destination to FlowTable
                        //send to destination
                    }
                    break;
                case PacketContent.TABLEUPDATE:
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
            String designation = args[0];
            designation = "Router" + designation;
            (new Router(designation)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

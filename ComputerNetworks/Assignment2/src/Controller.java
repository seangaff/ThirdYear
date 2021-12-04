//Sean Gaffney
//id: 19304695

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class Controller extends Node {

    String designation;
    Object[][] MasterTable;

    Controller() {
		try {
			socket= new DatagramSocket(CONTROLLER_PORT);
			listener.go();
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
                case PacketContent.FLOWREQ:
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
            (new Controller()).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

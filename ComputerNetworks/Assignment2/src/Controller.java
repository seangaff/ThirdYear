//Sean Gaffney
//id: 19304695

import java.net.DatagramPacket;
import java.net.DatagramSocket;
//import java.util.Arrays;
//import java.net.InetSocketAddress;
//import java.net.SocketAddress;
import java.util.HashMap;

public class Controller extends Node {

    String designation;
    int routerCount;
    HashMap<String, String> masterTable;

    Controller() {
		try {
			socket= new DatagramSocket(CONTROLLER_PORT);
			listener.go();
            masterTable = new HashMap<String, String>();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

    public synchronized void onReceipt(DatagramPacket packet) {
        try {
            PacketContent incoming = new PacketContent(packet);
            //System.out.println(Arrays.toString(packet.getData()));
            int packetType = incoming.getType();
            switch(packetType) {
                case PacketContent.ACK:
                    System.out.println("Recieved ACK");
                    break;
                case PacketContent.FLOWREQ:
                    System.out.println("Recieved Flow Request");
                    String response = "null";
                    String request = incoming.getDestination();
                    //System.out.println(Arrays.toString(incoming.getMessage().getBytes()));
                    //System.out.println(Arrays.toString(incoming.getDestination().getBytes()));
                    //System.out.println(Arrays.toString("Router0:Ted".getBytes()));
                    if(masterTable.containsKey(request)) {
                        response = masterTable.get(request);
                        System.out.println("Route Found:" + response);
                    }                   
                    else {
                        System.out.println("Route unknown, sending Error");
                    }
                    DatagramPacket tableUpdate = new PacketContent(PacketContent.TABLEUPDATE,response,"").toDatagramPacket();
                    tableUpdate.setSocketAddress(packet.getSocketAddress());
                    socket.send(tableUpdate);
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
            //init table
            masterTable.put("Router0Bill", "Endpoint0");
            masterTable.put("Router0Ted", "Router2");
            masterTable.put("Router1Bill", "Endpoint0");
            masterTable.put("Router1Ted", "Router0");
            masterTable.put("Router2Bill", "Router0");
            masterTable.put("Router2Ted", "Endpoint1");
            masterTable.put("Router3Bill", "Router0");
            masterTable.put("Router3Ted", "Endpoint1");

            masterTable.put("Router0:Bill", "Endpoint0");
            masterTable.put("Router0:Ted", "Router2");
            masterTable.put("Router1:Bill", "Endpoint0");
            masterTable.put("Router1:Ted", "Router0");
            masterTable.put("Router2:Bill", "Router0");
            masterTable.put("Router2:Ted", "Endpoint1");
            masterTable.put("Router3:Bill", "Router0");
            masterTable.put("Router3:Ted", "Endpoint1");

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

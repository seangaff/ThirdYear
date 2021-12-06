//Sean Gaffney
//id: 19304695

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;
import java.net.InetSocketAddress;
import java.net.InetAddress;

public class Application extends Node {

    String designation;
    InetAddress localAddress;
    InetSocketAddress service;


    Application(String designation) {
		try {
            this.designation = designation;
			socket= new DatagramSocket(APP_PORT);
			listener.go();
            localAddress = InetAddress.getLocalHost();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

    public synchronized void onReceipt(DatagramPacket packet) {
        try {
            System.out.println("New Message:"); 
            PacketContent contentA = new PacketContent(packet);
            if(designation.equals(contentA.getDestination())) {
                System.out.println("\n" + contentA.getMessage());
            }
            else {
                System.out.println("Wrong address: "+contentA.getDestination());
                System.out.println("address        "+designation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public void start(){
        try {
            InetSocketAddress service = new InetSocketAddress(localAddress, DEFAULT_PORT);
            DatagramPacket appREQ = new PacketContent(PacketContent.APPREQ,designation,"null").toDatagramPacket();
            appREQ.setSocketAddress(service);
		    socket.send(appREQ);
            System.out.println("Designation sent to Service");
            Scanner input = new Scanner(System.in);

            while(true) {
                System.out.println("Enter destination: ");
                String dest = input.nextLine().strip();
                System.out.println("Enter message: ");
                String mes = input.nextLine().strip();
                if(dest.equals("exit") || mes.equals("exit")) {
                    break;
                }
                DatagramPacket packet = new PacketContent(1,dest,mes).toDatagramPacket();
                packet.setSocketAddress(service);
                socket.send(packet);
                System.out.println("Sent");
    
            }
            System.out.println("Ended user input, will still recieve packets until quit");
            this.wait();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            String designation = args[0];
            (new Application(designation)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

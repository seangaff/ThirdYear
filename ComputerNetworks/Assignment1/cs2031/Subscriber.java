//Sean Gaffney
//id: 19304695

//docker create --name subscriber --cap-add=ALL -ti -v ~/Documents/ThirdYear/ComputerNetworks/Assignment1/cs2031:/cs2031 arm64v8/openjdk  /bin/bash

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class Subscriber extends Node{

    InetSocketAddress dstAddress;
    boolean windowOpen;

    Subscriber(int port) {
        try {
            windowOpen = false;
            dstAddress = brokerAddress;
            socket = new DatagramSocket(port);
            listener.go();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void onReceipt(DatagramPacket packet) {
        try {
            System.out.println("Packet was Recieved");

			PacketContent content= PacketContent.fromDatagramPacket(packet);

			if (content.getType()==PacketContent.MESSAGE) {
                System.out.println(content.toString());
                String[] arrOfStr = content.toString().split(":", 2);
                int tempVal = Integer.parseInt(arrOfStr[1]);
                if(tempVal > 50 && windowOpen == false) {
                    windowOpen = true;
                    System.out.println("Opened the window");
                }
                else if (tempVal < 50 && windowOpen == true) {
                    windowOpen = false;
                    System.out.println("Closed the window");
                }
                else {
                    System.out.println("Did not move the window");
                }
                
			}
            //this.notify();
		}
		catch(Exception e) {e.printStackTrace();}
        
    }

    public synchronized void start() throws Exception {
        
        DatagramPacket request;
        request= new SubContent(PacketContent.TEMP).toDatagramPacket();
        request.setSocketAddress(dstAddress);
        socket.send(request);
        System.out.println("" + new SubContent(PacketContent.TEMP).getTopic());
		System.out.println("Waiting for contact");
		this.wait();
	}

    public static void main(String[] args) {
		try {
			(new Subscriber(SUBSCRIBER_PORT)).start();
			System.out.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
    
}

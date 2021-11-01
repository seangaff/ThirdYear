//Sean Gaffney
//id: 19304695

//docker create --name subscriber --cap-add=ALL -ti -v ~/Documents/ThirdYear/ComputerNetworks/Assignment1/cs2031:/cs2031 arm64v8/openjdk  /bin/bash

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class Subscriber extends Node{

    Subscriber(int port) {
        try {
            socket = new DatagramSocket(port);
            listener.go();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void onReceipt(DatagramPacket packet) {
        try {
			System.out.println("Received packet");

			PacketContent content= PacketContent.fromDatagramPacket(packet);

			if (content.getType()==PacketContent.FILEINFO) {
                System.out.println(content.toString());
                this.notify();
			}
		}
		catch(Exception e) {e.printStackTrace();}
        
    }

    public synchronized void start() throws Exception {
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

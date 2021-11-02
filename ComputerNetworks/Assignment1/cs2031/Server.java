//Sean Gaffney
//id: 19304695

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;
//Scanner input = new Scanner(System.in);
//double income = input.nextDouble();
//input.close();

public class Server extends Node {
	//static final int DEFAULT_PORT = 50001;
	/*
	 *
	 */
	Server(int port) {
		try {
			socket= new DatagramSocket(port);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

	/**
	 * Assume that incoming packets contain a String and print the string.
	 */
	public synchronized void onReceipt(DatagramPacket packet) {
		try {
			System.out.println("Received packet");

			PacketContent content= PacketContent.fromDatagramPacket(packet);

			if (content.getType()==PacketContent.MESSAGE) {
				System.out.println("" + ((Message)content).toString());

				DatagramPacket response;
				response= new AckPacketContent("OK - Received this").toDatagramPacket();
				response.setSocketAddress(packet.getSocketAddress());
				socket.send(response);
			}
		}
		catch(Exception e) {e.printStackTrace();}
	}


	public synchronized void start() throws Exception {
		Scanner input = new Scanner(System.in);
		System.out.print("Enter '1' to subscribe to a topic\n" + 
						"Enter '2' to send a message\n");

		System.out.print("Enter '1' to subscribe to temperature readings\n" + 
						"Enter '2' to subscribe to humidity readings\n");

		System.out.print("Enter '1' to send a temperature reading\n" + 
						"Enter '2' to send a humidity reading\n");
		this.wait();
		//input.close();
	}

	/*
	 *
	 */
	public static void main(String[] args) {
		try {
			(new Server(SERVER_PORT)).start();
			System.out.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
}

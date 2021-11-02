//Sean Gaffney
//id: 19304695

/**
 *
 */
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
//import java.io.File;
//import java.io.FileInputStream;
import java.util.Random;

/**
 *
 * Client class
 *
 * An instance accepts user input
 *
 */
public class Client extends Node {
	InetSocketAddress dstAddress;

	/**
	 * Constructor
	 *
	 * Attempts to create socket at given port and create an InetSocketAddress for the destinations
	 */
	Client(InetSocketAddress dstAddress, int srcPort) {
		try {
			this.dstAddress = dstAddress;
			socket= new DatagramSocket(srcPort);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}


	/**
	 * Assume that incoming packets contain a String and print the string.
	 */
	public synchronized void onReceipt(DatagramPacket packet) {
		PacketContent content= PacketContent.fromDatagramPacket(packet);
		System.out.println(content.toString());
		this.notify();
	}


	/**
	 * Sender Method
	 *
	 */
	public synchronized void start() throws Exception {
				Random rand = new Random();
		int topic = rand.nextInt(10); //randomly picks a topic to publish to
		int value = rand.nextInt(100);

		DatagramPacket packet;
		if(topic > 5) {
			packet = new Message(PacketContent.TEMP, "Temperature:" + value).toDatagramPacket();
			System.out.println("Temperature:" + value);
		
		}
		else {
			packet = new Message(PacketContent.HUMIDITY,"Humidity:" + value).toDatagramPacket();
			System.out.println("Humidity:" + value);
		}
		packet.setSocketAddress(dstAddress);
		socket.send(packet);
		System.out.println("Packet sent");
		this.wait();
		//fin.close();
	}


	/**
	 * Test method
	 *
	 * Sends a packet to a given address
	 */
	public static void main(String[] args) {
		try {
			(new Client(brokerAddress, ClIENT_PORT)).start();
			System.out.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
}

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
		/*
		String fname;
		File file= null;
		FileInputStream fin= null;

		FileInfoContent fcontent;

		int size;
		byte[] buffer= null;
		DatagramPacket packet= null;

		fname= "message.txt";//terminal.readString("Name of file: ");

		file= new File(fname);				// Reserve buffer for length of file and read file
		buffer= new byte[(int) file.length()];
		fin= new FileInputStream(file);
		size= fin.read(buffer);
		if (size==-1) {
			fin.close();
			throw new Exception("Problem with File Access:"+fname);
		}
		System.out.println("File size: " + buffer.length);

		fcontent= new FileInfoContent(fname, size);

		System.out.println("Sending packet w/ name & length"); // Send packet with file name and length
		*/
		Random rand = new Random();
		int topic = rand.nextInt(10);
		int value = rand.nextInt(100);

		DatagramPacket packet;
		packet= new Message("Temperature: " + value).toDatagramPacket();
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

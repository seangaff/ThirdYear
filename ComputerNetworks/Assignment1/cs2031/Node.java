import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.CountDownLatch;
import java.net.InetSocketAddress;
//import java.util.ArrayList;

public abstract class Node {
	static final int PACKETSIZE = 65536;

	public static final int BROKER_PORT = 50000;
    public static final int SERVER_PORT = 50001;
	public static final int ClIENT_PORT = 50002;
    public static final int SUBSCRIBER_PORT = 50005;

	public static final String DEFAULT_DST_NODE = "broker";
	public static final int DEFAULT_DST_PORT = 50000;

	public static final InetSocketAddress brokerAddress = new InetSocketAddress("broker", BROKER_PORT);
	public static final InetSocketAddress serverAddress = new InetSocketAddress("server", SERVER_PORT);
    public static final InetSocketAddress clientAddress = new InetSocketAddress("client", ClIENT_PORT);

	DatagramSocket socket;
	Listener listener;
	CountDownLatch latch;

	//public ArrayList<String> subscribers;

	Node() {
		latch= new CountDownLatch(1);
		listener= new Listener();
		listener.setDaemon(true);
		listener.start();
	}


	public abstract void onReceipt(DatagramPacket packet);

	/**
	 *
	 * Listener thread
	 *
	 * Listens for incoming packets on a datagram socket and informs registered receivers about incoming packets.
	 */
	class Listener extends Thread {

		/*
		 *  Telling the listener that the socket has been initialized
		 */
		public void go() {
			latch.countDown();
		}

		/*
		 * Listen for incoming packets and inform receivers
		 */
		public void run() {
			try {
				latch.await();
				// Endless loop: attempt to receive packet, notify receivers, etc
				while(true) {
					DatagramPacket packet = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
					socket.receive(packet);

					onReceipt(packet);
				}
			} catch (Exception e) {if (!(e instanceof SocketException)) e.printStackTrace();}
		}
	}
}

//Sean Gaffney
//id: 19304695

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.CountDownLatch;
import java.net.InetSocketAddress;

public abstract class Node {
	static final int PACKETSIZE = 65536;

	public static final int DEFAULT_PORT = 51510;
	public static final int APP_PORT = 50500;
	public static final int CONTROLLER_PORT = 52520;

	public static final InetSocketAddress controllerAddress = new InetSocketAddress("Controller", CONTROLLER_PORT);
	public static final InetSocketAddress R0address = new InetSocketAddress("Router0", DEFAULT_PORT);
	public static final InetSocketAddress R2address = new InetSocketAddress("Router2", DEFAULT_PORT);
	public static final InetSocketAddress R3address = new InetSocketAddress("Router3", DEFAULT_PORT);
	public static final InetSocketAddress E0address = new InetSocketAddress("Endpoint0", DEFAULT_PORT);
	public static final InetSocketAddress E1address = new InetSocketAddress("Endpoint1", DEFAULT_PORT);

	// public static final InetSocketAddress client1 = new InetSocketAddress("client1", DEFAULT_PORT);
	// public static final InetSocketAddress client2 = new InetSocketAddress("client2", DEFAULT_PORT);
	// public static final InetSocketAddress router1 = new InetSocketAddress("router", DEFAULT_PORT);

	DatagramSocket socket;
	Listener listener;
	CountDownLatch latch;


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

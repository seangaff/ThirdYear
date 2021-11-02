import java.net.DatagramPacket;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * The class is the basis for packet contents of various types.
 *
 *
 */
public abstract class PacketContent {

	public static final byte ACKPACKET= 1;
	public static final byte SUBPACKET = 2;
	public static final byte MESSAGE = 3;
	public static final byte FILEINFO = 4;

	public static final byte NOTOP = 0;
	public static final byte TEMP = 10;
	public static final byte HUMIDITY = 20;

	byte type = 0;
	byte topic = 0;

	/**
	 * Constructs an object out of a datagram packet.
	 * @param packet Packet to analyse.
	 */
	public static PacketContent fromDatagramPacket(DatagramPacket packet) {
		PacketContent content= null;

		try {
			byte type;
			byte topic;

			byte[] data;
			ByteArrayInputStream bin;
			ObjectInputStream oin;

			data= packet.getData();  // use packet content as seed for stream
			bin= new ByteArrayInputStream(data);
			oin= new ObjectInputStream(bin);

			type = oin.readByte();  // read type from beginning of packet
			//topic = oin.readByte();

			switch(type) {   // depending on type create content object
			case ACKPACKET:
				content= new AckPacketContent(oin);
				break;
			case SUBPACKET:
				content = new SubContent(oin);
				break;
			case MESSAGE:
				content = new Message(oin);
				break;
			default:
				content= null;
				break;
			}
			oin.close();
			bin.close();

		}
		catch(Exception e) {e.printStackTrace();}

		return content;
	}


	/**
	 * This method is used to transform content into an output stream.
	 *
	 * @param out Stream to write the content for the packet to.
	 */
	protected abstract void toObjectOutputStream(ObjectOutputStream out);

	/**
	 * Returns the content of the object as DatagramPacket.
	 *
	 * @return Returns the content of the object as DatagramPacket.
	 */
	public DatagramPacket toDatagramPacket() {
		DatagramPacket packet= null;

		try {
			ByteArrayOutputStream bout;
			ObjectOutputStream oout;
			byte[] data;

			bout= new ByteArrayOutputStream();
			oout= new ObjectOutputStream(bout);

			oout.writeByte(type);         // write type to stream
			oout.writeByte(topic);
			toObjectOutputStream(oout);  // write content to stream depending on type

			oout.flush();
			data= bout.toByteArray(); // convert content to byte array

			packet= new DatagramPacket(data, data.length); // create packet from byte array
			oout.close();
			bout.close();
		}
		catch(Exception e) {e.printStackTrace();}

		return packet;
	}


	/**
	 * Returns the content of the packet as String.
	 *
	 * @return Returns the content of the packet as String.
	 */
	public abstract String toString();

	/**
	 * Returns the type of the packet.
	 *
	 * @return Returns the type of the packet.
	 */
	public byte getType() {
		return type;
	}
	public byte getTopic() {
		return topic;
	}

}

import java.net.DatagramPacket;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
import java.io.IOException;

public class PacketContent {
    static final int TYPE = 0;
    static final int LENGTH = 1;
    static final int VALUE = 2;


    public static final int ERROR = 0;
    public static final int MESSAGE = 1; //string message
    public static final int ACK = 2; 
    
    public static final int TABLEUPDATE = 3; //update from controller
    public static final int FLOWREQ = 4; //Request for update from controller
    public static final int APPREQ = 5;  //request to service for content

    public static final int COMBINATION = 9; //string message with subnet destination

    byte type;
    byte length;
    String value;
    String destination;
    String message;
    byte[] TLV;
    
    PacketContent(DatagramPacket packet) {
        TLV = packet.getData();
        ByteArrayInputStream input = new ByteArrayInputStream(TLV);
        try {
            type = (byte) input.read();
            length = (byte) input.read();
            value = "";
            int c;
            while((c = input.read())!= -1) {
                value = value + ((char) c);
            }
            String[] messageParts = value.split(":",2);
            destination = messageParts[0];
            message = messageParts[1];
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //String value = new String(TLV[2]);

        //byte[] data;
        //data = packet.getData(); 
        // // String message = new String(data);
        // // String[] messageParts = message.split(".");
        // type = data[type];
        // length = Integer.parseInt(messageParts[1]);
        // destination = messageParts[2];
        // contents = messageParts[3];
    }

    PacketContent(int type, String destination, String message) {
        this.type = (byte) type;
        this.destination = destination;
        this.message = message;
        value = destination + ":" + message;
        try {
            toTLV();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DatagramPacket toDatagramPacket() {
		DatagramPacket packet= null;
        try {
        // byte[] data;
        // String message = (type + "." + destination.length() + "." + destination + "." + contents);
        // data = message.getBytes();
        // packet = new DatagramPacket(data, data.length);
        packet = new DatagramPacket(TLV, TLV.length);
        }
        catch(Exception e) {e.printStackTrace();}
        return packet;
    }

    private void toTLV() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        outputStream.write(type);
        outputStream.write((byte) value.length());
        outputStream.write(value.getBytes());
        TLV = outputStream.toByteArray();
        outputStream.close();
    }

    public int getType() {
        return type;
    }
    public int getLength() {
        return length;
    }
    public String getValue() {
        return value;
    }

    public String getDestination() {
        return destination;
    }
    public String getMessage() {
        return message;
    }

    public byte[] getTLV() {
        return TLV;
    }
}

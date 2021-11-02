//Sean Gaffney
//id: 19304695

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class Message extends PacketContent{

    String theMessage;

    Message(byte topic, String theMessage) {
        type = MESSAGE;
        this.topic = topic;
        this.theMessage = theMessage;
    }

    protected Message(ObjectInputStream oin) {
		try {
			type = MESSAGE;
			topic = oin.readByte();
			theMessage = oin.readUTF();
		}
		catch(Exception e) {e.printStackTrace();}
	}

    protected void toObjectOutputStream(ObjectOutputStream oout) {
        try {
			oout.writeUTF(theMessage);
		}
		catch(Exception e) {e.printStackTrace();}
    }

    public String toString() {
        return theMessage;
    }
    
}

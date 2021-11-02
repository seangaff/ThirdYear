//Sean Gaffney
//id: 19304695

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class Message extends PacketContent{

    String theMessage;

    Message(String theMessage) {
        type= MESSAGE;
        this.theMessage = theMessage;
    }

    protected Message(ObjectInputStream oin) {
		try {
			type= MESSAGE;
			theMessage= oin.readUTF();
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

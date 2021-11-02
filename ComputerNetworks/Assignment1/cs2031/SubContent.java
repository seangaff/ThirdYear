//Sean Gaffney
//id: 19304695

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class SubContent extends PacketContent {

    byte topic;

    SubContent(byte topic) {
		type= SUBPACKET;
		this.topic = topic;
	}

    protected SubContent(ObjectInputStream oin) {
		try {
			type= SUBPACKET;
			topic = NOTOP;
		}
		catch(Exception e) {e.printStackTrace();}
	}

    protected void toObjectOutputStream(ObjectOutputStream out) {
        // TODO Auto-generated method stub
        
    }

    public String toString() {
        return "Sub Reqest for " + topic;
    }
}
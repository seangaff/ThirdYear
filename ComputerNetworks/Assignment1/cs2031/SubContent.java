//Sean Gaffney
//id: 19304695

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class SubContent extends PacketContent {

    String SubTopic;

    SubContent(String SubTopic) {
		type= SUBPACKET;
		this.SubTopic = SubTopic;
	}

    protected SubContent(ObjectInputStream oin) {
		try {
			type= SUBPACKET;
			SubTopic= oin.readUTF();
		}
		catch(Exception e) {e.printStackTrace();}
	}

    protected void toObjectOutputStream(ObjectOutputStream out) {
        // TODO Auto-generated method stub
        
    }

    public String toString() {
        return "Sub Reqest for " + SubTopic;
    }
}
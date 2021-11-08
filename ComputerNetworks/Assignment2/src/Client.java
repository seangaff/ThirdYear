import java.net.DatagramPacket;

public class Client extends Node {

    public synchronized void onReceipt(DatagramPacket packet) {
        
    }

    public synchronized void start() {

    }

    public static void main(String[] args) {
        try {
            (new Client()).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

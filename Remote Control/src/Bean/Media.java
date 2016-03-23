package Bean;

import java.io.Serializable;

public class Media implements Serializable{
    private int ID;
    private byte[] packet = new byte[1500];
    
    public Media() {
    }
    
    public Media(int _ID, byte[] _packet) {
        ID = _ID;
        packet = _packet;
    }
    
    public int getID() {
        return ID;
    }
    
    public byte[] getBytes() {
        return packet;
    }
}

import java.io.*;
import java.net.*;
import UI.RemoteControl;

public class UDPClient {
    
    public static StringBuilder readText(String source) throws Exception {
        StringBuilder sb = new StringBuilder();
        BufferedReader inFromUser = new BufferedReader(new FileReader(source));
        
	try {
            String line = inFromUser.readLine();
            while(line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = inFromUser.readLine();
            }
	} finally {
            inFromUser.close();
	}
        
        return sb; 
    }
    
    public static void main(String args[]) throws Exception {      
	DatagramSocket clientSocket = new DatagramSocket();       
	InetAddress IPAddress = InetAddress.getByName("localhost");     
        
        RemoteControl rc = new RemoteControl(clientSocket, IPAddress, 9876);
        rc.setVisible(true);
    }
}

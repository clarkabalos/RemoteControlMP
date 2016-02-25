
import java.io.*;
import java.net.*;

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
        String wholeText ="";
        String sentence ="";
        
        //BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        //System.out.println(new File("hi.txt").getAbsoluteFile());
	/*BufferedReader inFromUser = new BufferedReader(new FileReader("test.txt"));
        
	try {
            StringBuilder sb = new StringBuilder();
            String line = inFromUser.readLine();
            while(line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = inFromUser.readLine();
            }
            wholeText = sb.toString();
	} finally {
            inFromUser.close();
	} */
        
        wholeText = readText("test.txt").toString();
        
	DatagramSocket clientSocket = new DatagramSocket();       
	InetAddress IPAddress = InetAddress.getByName("localhost");     
        
	byte[] sendData = new byte[20];       
	byte[] receiveData = new byte[20];       
        
	int i = 0;
	int j = sendData.length - 1;
	int length = wholeText.length();
        
	while(length > 0) {
            if(j < wholeText.length()) {
		sentence = wholeText.substring(i, j);
            } else {
                int diff = j - wholeText.length();
                j -= diff;
                sentence = wholeText.substring(i, j);
                while(diff > 0) {
                    sentence += " ";
                    diff--;
                }
            }
    
            sendData = sentence.getBytes();   
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);       
            clientSocket.send(sendPacket);  
            i = j;
            j += 20;
            length -= 20;
	}
        
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        
        String modifiedSentence = new String(receivePacket.getData());
        System.out.println("FROM SERVER: " + modifiedSentence);
        clientSocket.close();
    }
}

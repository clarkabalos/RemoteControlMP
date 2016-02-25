import java.io.*;
import java.net.*;
import UI.MultimediaApp;
import static com.sun.xml.internal.ws.util.VersionUtil.compare;

public class UDPServer {
    public static void main(String args[]) throws Exception {
        MultimediaApp app = new MultimediaApp();
        app.setVisible(true);
        
        //System.out.println(new File(".").getAbsoluteFile());
        DatagramSocket serverSocket = new DatagramSocket(9876);
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        
        while(true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String request = new String(receivePacket.getData());
            /*System.out.println("RECEIVED: " + sentence);
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            String capitalizedSentence = sentence.toUpperCase();
            sendData = capitalizedSentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket); */
            String trimmedRequest = request.trim();
            
            if(trimmedRequest.equals("Next")) {
                app.nextImage();
            } else if(trimmedRequest.equalsIgnoreCase("Back")) {
                app.prevImage();
            } else if(trimmedRequest.equalsIgnoreCase("Slideshow")) {
                app.slideshow();
            }else if(trimmedRequest.equalsIgnoreCase("Close")) {
                app.dispose();
                serverSocket.close();
                System.exit(0);
            }
        }
    }
}

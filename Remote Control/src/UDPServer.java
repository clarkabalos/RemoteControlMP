import java.io.*;
import java.net.*;
import UI.MultimediaApp;
import static com.sun.xml.internal.ws.util.VersionUtil.compare;

public class UDPServer {
    public static void main(String args[]) throws Exception {
        DatagramSocket serverSocket = new DatagramSocket(9876);
        byte[] receiveData = new byte[1024];
        
        MultimediaApp app = new MultimediaApp(serverSocket);
        app.setVisible(true);
        
        while(true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String request = new String(receivePacket.getData(), 0, receivePacket.getLength());
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
            } else if(trimmedRequest.equalsIgnoreCase("StartSlideshow")) {
                app.slideshow(500);
            } else if(trimmedRequest.equalsIgnoreCase("StopSlideshow")) {
                app.stopSlideshow();
            } else if(trimmedRequest.contains("SetTime")) {
                int index = trimmedRequest.lastIndexOf(":");
                String time = trimmedRequest.substring(index + 1,trimmedRequest.length());
                System.out.println(time);
                app.editTimer(Integer.parseInt(time));
            } else if(trimmedRequest.equalsIgnoreCase("Initialize")) {
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                app.sendFileNames(IPAddress, port);
            } else if(trimmedRequest.equalsIgnoreCase("Play")) {
                app.play();
            } else if(trimmedRequest.equalsIgnoreCase("Stop")) {
                app.stop();
            } else if(trimmedRequest.equalsIgnoreCase("Close")) {
                app.dispose();
                serverSocket.close();
                System.exit(0);
            }
        }
    }
}

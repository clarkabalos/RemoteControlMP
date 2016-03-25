import java.net.*;
import UI.MultimediaApp;

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
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            String trimmedRequest = request.trim();
            
            if(trimmedRequest.equals("Next")) {
                app.nextImage();
                app.sendToClient(IPAddress, port);
            } else if(trimmedRequest.equalsIgnoreCase("Back")) {
                app.prevImage();
                app.sendToClient(IPAddress, port);
            } else if(trimmedRequest.equalsIgnoreCase("StartSlideshow")) {
                app.slideshow(1000);
            } else if(trimmedRequest.equalsIgnoreCase("StopSlideshow")) {
                app.stopSlideshow();
            } else if(trimmedRequest.contains("SetTime")) {
                int index = trimmedRequest.lastIndexOf(":");
                String time = trimmedRequest.substring(index + 1,trimmedRequest.length());
                app.editTimer(Integer.parseInt(time));
            } else if(trimmedRequest.equalsIgnoreCase("Initialize")) {
                app.sendFileNames(IPAddress, port);
                app.sendToClient(IPAddress, port);
            } else if(trimmedRequest.equalsIgnoreCase("Play")) {
                app.play();
            } else if(trimmedRequest.equalsIgnoreCase("Stop")) {
                app.stop();
            } else if(trimmedRequest.equalsIgnoreCase("Upload")) {
                app.receiveFromClient(IPAddress, port);
            } else if(trimmedRequest.equalsIgnoreCase("Close")) {
                app.dispose();
                serverSocket.close();
                System.exit(0);
            }
        }
    }
}

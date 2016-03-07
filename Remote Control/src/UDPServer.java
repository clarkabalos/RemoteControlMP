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
                //app.sendFileName(IPAddress, port);
                app.sendToClient(IPAddress, port);
            } else if(trimmedRequest.equalsIgnoreCase("Back")) {
                app.prevImage();
                //app.sendFileName(IPAddress, port);
                app.sendToClient(IPAddress, port);
            } else if(trimmedRequest.equalsIgnoreCase("StartSlideshow")) {
                app.slideshow(1000);
                //app.sendToClient(IPAddress, port);
            } else if(trimmedRequest.equalsIgnoreCase("StopSlideshow")) {
                app.stopSlideshow();
                //app.sendToClient(IPAddress, port);
            } else if(trimmedRequest.contains("SetTime")) {
                int index = trimmedRequest.lastIndexOf(":");
                String time = trimmedRequest.substring(index + 1,trimmedRequest.length());
                app.editTimer(Integer.parseInt(time));
                //app.sendToClient(IPAddress, port);
            } else if(trimmedRequest.equalsIgnoreCase("Initialize")) {
                //app.sendFileName(IPAddress, port);
                app.sendToClient(IPAddress, port);
                //InetAddress IPAddress = receivePacket.getAddress();
                //int port = receivePacket.getPort();
                app.sendFileNames(IPAddress, port);
            } else if(trimmedRequest.equalsIgnoreCase("Play")) {
                app.play();
                //app.sendFileName(IPAddress, port);
            } else if(trimmedRequest.equalsIgnoreCase("Stop")) {
                app.stop();
                //app.sendFileName(IPAddress, port);
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

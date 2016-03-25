package UI;

import Bean.Media;
import Bean.Multimedia;
import com.sun.jna.NativeLibrary;
import java.awt.Canvas;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class MultimediaApp extends javax.swing.JFrame {
    //list of multimedia files
    private ArrayList<Multimedia> FilesInFolder = new ArrayList<>();
    
    private int index = 0;
    private Timer time;
    
    //media player-related vars
    private MediaPlayerFactory mediaPlayerFactory;
    private EmbeddedMediaPlayer mediaPlayer;
    private Canvas canvas;
    
    private final String searchPath = new File("").getAbsolutePath();
    private DatagramSocket serverSocket; 
    
    public MultimediaApp(DatagramSocket _serverSocket) throws Exception {
        initComponents();
        serverSocket = _serverSocket;
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), searchPath + "\\Libraries\\VLC Plugins\\64-bit");
        showFilesInFolder(new File(searchPath + "\\Multimedia"));
        showImage(setImageSize(index));
        initializeMediaPlayer();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        allThumbnails = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        panel = new javax.swing.JPanel();
        imageViewer = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setBorder(null);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setMinimumSize(new java.awt.Dimension(720, 100));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(720, 100));

        allThumbnails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        allThumbnails.setForeground(new java.awt.Color(240, 240, 240));
        jScrollPane1.setViewportView(allThumbnails);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 720, 100));

        jLabel1.setBackground(new java.awt.Color(153, 153, 153));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Multimedia Application");
        jLabel1.setToolTipText("");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 4, 710, 50));

        panel.setBackground(new java.awt.Color(0, 0, 0));
        panel.setLayout(new java.awt.BorderLayout());
        getContentPane().add(panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 720, 440));
        getContentPane().add(imageViewer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 720, 440));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    
    public void showFilesInFolder(File folder) throws IOException {
        String type = "";
        File[] files = folder.listFiles();
        for(int i = 0; i < files.length; i++) {
            String path = files[i].getAbsolutePath();
            long size = files[i].length();
            int x = path.lastIndexOf('\\');
            String name = path.substring(x+1);
            if(name.endsWith("jpg") || name.endsWith(".png") || name.endsWith("mp3") || name.endsWith("mp4")) {
                if(name.endsWith("jpg") || name.endsWith(".png")) {
                    Multimedia file = new Multimedia(name, path);
                    file.setType("Image");
                    file.setLength(size);
                    file.setThumbnailPath(null);
                    FilesInFolder.add(file);
                } else {
                    String thumbnailPath;
                    int thumbnailLength;
                    if(name.endsWith("mp3")) {
                        thumbnailPath = "audio.png";
                        thumbnailLength = (int) new File(thumbnailPath).length();
                        type = "Audio";
                    } else {
                        thumbnailPath = "video.png";
                        thumbnailLength = (int) new File(thumbnailPath).length();
                        type = "Video";
                    }
                    Multimedia file = new Multimedia(name, path);
                    file.setType(type);
                    file.setLength(size);
                    file.setThumbnailPath(thumbnailPath);
                    file.setThumbnailLength(thumbnailLength);
                    FilesInFolder.add(file);
                } 
            } 
        }
    }
    
    /*public void addFileToFolder(String _path, long _size) {
        int x = _path.lastIndexOf('\\');
        String name = _path.substring(x+1);
        
        if(name.endsWith("jpg") || name.endsWith(".png")) {
            Multimedia file = new Multimedia(name, _path);
            file.setType("Image");
            file.setLength(_size);
            file.setThumbnailPath(null);
            FilesInFolder.add(file);
        }
    }*/
    
    public void showImage(ImageIcon icon) {
        imageViewer.setIcon(icon);
    }
    
    public void nextImage() {
        if(index < FilesInFolder.size() - 1) {
            index++;
            System.out.println("INDEX NUMBER: " + index);
            System.out.println("NUMBER OF FILES IN FOLDER: " + FilesInFolder.size());
            showImage(setImageSize(index));
        } 
    }
    
    public void prevImage() {
        if(index > 0) {
            index--;
            showImage(setImageSize(index));
        }
    }
    
    public void initializeMediaPlayer() { 
        canvas = new Canvas();
        panel.add(canvas);
        panel.repaint();

        mediaPlayerFactory = new MediaPlayerFactory();
        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
        CanvasVideoSurface videoSurface = mediaPlayerFactory.newVideoSurface(canvas);
        mediaPlayer.setVideoSurface(videoSurface);
        panel.setVisible(false);
    }
        
    public void play() {
        if(FilesInFolder.get(index).getType().equals("Video")) {
            panel.setVisible(true);
        } else {
            panel.setVisible(false);
        }
        
        mediaPlayer.playMedia(FilesInFolder.get(index).getPath());
        return;
    }
    
    public void stop() {
        mediaPlayer.stop();
        panel.setVisible(false);
    }
    
    public void sendFileNames(InetAddress _IPAddress, int _port) throws IOException {
        byte[] sendData = new byte[1500];
        StringBuilder sb = new StringBuilder();
        sb.append(System.lineSeparator());
        for(int i = 0; i < FilesInFolder.size(); i++) {
            String fileName = FilesInFolder.get(i).getFileName();
            sb.append(fileName);
            sb.append(",");
        }
        sendData = sb.toString().getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, _IPAddress, _port);
        serverSocket.send(sendPacket);
    }
    
    public void sendToClient(InetAddress _IPAddress, int _port) throws Exception {
        /* Send file details (headers) first */
        sendFileDetails(_IPAddress, _port);
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket;
        
        if(!FilesInFolder.get(index).getType().equalsIgnoreCase("Image")) {
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String status = new String(receivePacket.getData(), 0, receivePacket.getLength());
            if(status.equalsIgnoreCase("New Thumbnail")) {
                /* Send the actual file by chopping it into 1500-byte chunks */
                sendFile(_IPAddress, _port, FilesInFolder.get(index).getThumbnailPath());
            }
            return;
        }
        
        /* Receive status to see if file already exists or not in client */
        receiveData = new byte[1024];
        receivePacket = new DatagramPacket(receiveData, receiveData.length);
        serverSocket.receive(receivePacket);
        String status = new String(receivePacket.getData(), 0, receivePacket.getLength());
        if(status.equalsIgnoreCase("File Exists")) {
            return;
        } else if(status.equalsIgnoreCase("New File")) {
            /* Send the actual file by chopping it into 1500-byte chunks */
            sendFile(_IPAddress, _port, FilesInFolder.get(index).getPath());
        }
    }
    
    public void sendFileDetails(InetAddress _IPAddress, int _port) throws IOException {
        byte[] headers = serialize(FilesInFolder.get(index));
        DatagramPacket sendPacket = new DatagramPacket(headers, headers.length, _IPAddress, _port);
        serverSocket.send(sendPacket);
        System.out.println("Sent file details!");
    }
    
    public void sendFile(InetAddress _IPAddress, int _port, String _path) throws Exception {
        File file = new File(_path);
        byte[] buffer = new byte[(int) file.length()];
        
        /* Convert file to byte array so it can be sent */
        try (
            FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(buffer);
            fileInputStream.close();
        }
        
        int i = 0;
	int j = 1499;
        int length = buffer.length;
        int seqNum = 0;
        int ackNum = 0;
        int repeatedAck = 0;
        int prevAck = 0;
        int tempSeqNum = 0;
        byte[] chunk = new byte[1500];
        byte[] ack = new byte[1024];
        boolean fileComplete = false;
        boolean lostPacket = false;
        ArrayList<byte[]> chunkQueue = new ArrayList<>();
        Queue seqAck = new LinkedList();
        
        /* put all chunks into queue */
        while(length > 0) {
            if(j < buffer.length) {
                chunk = Arrays.copyOfRange(buffer, i, j);
            } else {
                int diff = j - buffer.length;
                j -= diff;
                chunk = Arrays.copyOfRange(buffer, i, j);
            }
            chunkQueue.add(chunk);

            i = j;
            j += 1500;
            length -= 1500;
        }
        
        System.out.println("Number of Packets to Send: " + chunkQueue.size());
        i = 0;
        while(seqNum < chunkQueue.size() || fileComplete) {
            if(!fileComplete) {
                while(i < 5 && i < chunkQueue.size()) {
                    Media fileChunk = new Media(seqNum, chunkQueue.get(seqNum));
                    byte[] test = serialize(fileChunk);
                    DatagramPacket sendPacket = new DatagramPacket(test, test.length, _IPAddress, _port);   
                    serverSocket.send(sendPacket);  
                    System.out.println("SENT: " + fileChunk.getID());
                    seqAck.add(seqNum);
                    i++;
                    seqNum++;
                    
                    if(lostPacket) {
                        seqNum = tempSeqNum;
                        lostPacket = false;
                    }
                    //Thread.sleep(10);
                }
                
                if(seqNum >= chunkQueue.size()) {
                    fileComplete = true;
                }
            }
            serverSocket.setSoTimeout(100);
            try {
                DatagramPacket receiveAck = new DatagramPacket(ack, ack.length);
                serverSocket.receive(receiveAck);
                ackNum = Integer.parseInt(new String(receiveAck.getData(), 0, receiveAck.getLength()));
                System.out.println("RCVD ACK#: " + ackNum);
                int temp = (int) seqAck.element();
                if(ackNum == temp) {
                    seqAck.remove();
                    
                } else {
                    if(prevAck == ackNum)
                        repeatedAck++;
                    else
                        repeatedAck = 1;
                    if(repeatedAck == 3) {
                        seqNum = ackNum + 1;
                        repeatedAck = 0;
                    }
                    
                    prevAck = ackNum;
                }
                
                i--;
            } catch(SocketTimeoutException e) {
                lostPacket = true;
                tempSeqNum = seqNum;
                seqNum = ackNum + 1;
                System.out.println("Packet with seq. num " + seqNum + " is deemed lost.");
                System.out.println("Trying to send packet with seq. num " + seqNum + "...");
                i--;
                fileComplete = false;
            } 
            
            if(fileComplete && ackNum == seqNum-1) {
                fileComplete = false;
            }
        }
        serverSocket.setSoTimeout(0);
        System.out.println("Packet transfer is completed.");
    }
    
    public byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(obj);
        objectStream.flush();
        return byteStream.toByteArray();
    }
    
    /*public void sendFile(InetAddress _IPAddress, int _port, String _path) throws IOException {
        File file = new File(_path);
        byte[] buffer = new byte[(int) file.length()];
        
        /* Convert file to byte array so it can be sent 
        try (
            FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(buffer);
            fileInputStream.close();
        }
        
        int i = 0;
	int j = 1499;
        int length = buffer.length;
        int count = 1;
        
        while(length > 0) {
            byte[] chunk = new byte[1500];
            if(j < buffer.length) {
                System.out.println("First");
		chunk = Arrays.copyOfRange(buffer, i, j);
            } else {
                System.out.println("Second");
                int diff = j - buffer.length;
                j -= diff;
                chunk = Arrays.copyOfRange(buffer, i, j);
                
                System.out.println("FINALLY");
            }
            System.out.println("Data: " + chunk.length + " x " + count);
            DatagramPacket sendPacket = new DatagramPacket(chunk, chunk.length, _IPAddress, _port);       
            serverSocket.send(sendPacket);  
            i = j;
            j += 1500;
            length -= 1500;
            count++;
            System.out.println("Length: " + length);
        }
    }*/
    
    public void slideshow(int i) { 
        time = new Timer(i,new ActionListener() { 
            @Override public void actionPerformed(ActionEvent e) { 
                if(FilesInFolder.get(index).getType().equals("Image")) {
                    showImage(setImageSize(index));
                    index++;
                } else {
                    index++;
                }
                
                if(index >= FilesInFolder.size())
                    index = 0; 
            } 
        }); 
        time.start(); 
    }  
    
    public void stopSlideshow() {
        time.stop();
    }
    
    public ImageIcon setImageSize(int i) { 
        Image img = null;
        String location;
        
        if(FilesInFolder.get(i).getType().equalsIgnoreCase("Image")) {
            location = FilesInFolder.get(i).getPath();
        } else {
            location = FilesInFolder.get(i).getThumbnailPath();
        }
        
        try{
            img = ImageIO.read(new File(location)).getScaledInstance(imageViewer.getWidth(), imageViewer.getHeight(), Image.SCALE_SMOOTH);
        }catch(IOException e){
            System.out.println(e);
        }
        
        ImageIcon icon = new ImageIcon(img); 
        return icon; 
    }
    
    public void editTimer(int i) {
        if(time.isRunning()) {
            time.stop();
            slideshow(i);
        }
    }
    
    public void receiveFromClient(InetAddress _IPAddress, int _port) throws Exception {
        byte[] sendRequest = new byte[1024];
        DatagramPacket sendPacket;
        
        /* Receive details (headers) of file being sent */
        Multimedia file = getFileDetails();
        FilesInFolder.add(file);
        //System.out.println("NUMBER OF FILES IN FOLDER: " + FilesInFolder.size());
        
        /* Finally request for the actual file */
        String location = new File("").getAbsolutePath() + "\\Multimedia\\" + file.getFileName();
        if(checkIfFileExists(location)) {
            System.out.println("File already exists!");
            sendRequest = "File Exists".getBytes();
            sendPacket = new DatagramPacket(sendRequest, sendRequest.length, _IPAddress, _port);
            serverSocket.send(sendPacket);
        } else {
            sendRequest = "New File".getBytes();
            sendPacket = new DatagramPacket(sendRequest, sendRequest.length, _IPAddress, _port);       
            serverSocket.send(sendPacket); 
            byte[] wholeFile = getFile((int) file.getLength(), _IPAddress, _port);
            writeToDisk(wholeFile, location);
        }
    }
    
    public Multimedia getFileDetails() throws Exception {
        byte[] headers = new byte[1024];
        DatagramPacket receiveHeaders = new DatagramPacket(headers, headers.length);
        serverSocket.receive(receiveHeaders);
        Multimedia file = (Multimedia) deserialize(receiveHeaders.getData(), "Multimedia");
       
        return file;
    }
    
    public boolean checkIfFileExists(String _path) {
        File file = new File(_path);
        if(file.exists() && file.isFile()) {
            return true;
        } else {
            return false;
        }        
    }
    
    public byte[] getFile(int totalLength, InetAddress _IPAddress, int _port) throws Exception {
        int length = totalLength;
        int i = 0;
        int j = 1499;
        int timeoutCount = 0;
        int seqNum = 0;
        int expectedSeq = 0;
        int lostSeq = 0;
        byte[] wholeFile = new byte[length];
        byte[] receiveData = new byte[2000];
        byte[] data = new byte[1500];
        byte[] ack = new byte[1024];
        boolean discardPacket = false;
        boolean doublePacket = false;
        boolean lostPacket = false;
        boolean discardNextPacket = false;
        Media receiveMSG = new Media();
        Media temp = new Media();
        LinkedList dataQueue = new LinkedList();
        
        while(length > 0) {
        serverSocket.setSoTimeout(50);
            try {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                receiveMSG = (Media) deserialize(receivePacket.getData(), "Media");
                seqNum = receiveMSG.getID();

                dataQueue.add(i, receiveMSG);
                if(seqNum < expectedSeq) {
                    doublePacket = true;
                }
                if(!discardPacket && !doublePacket) {
                    System.out.println("SAVED: " + seqNum);
                    if(lostPacket) {
                        lostPacket = false;
                        discardPacket = true;
                    }
                    seqNum++;
                    length -= 1500;                     
                } else {
                    if(doublePacket)
                        System.out.println("Received repeating packet: " + seqNum);
                    System.out.println("DISCARDED: " + seqNum);
                    if(seqNum == lostSeq)  {
                        discardPacket = false;
                        discardNextPacket = false;
                        doublePacket = true;
                    } 
                }
            } catch(SocketTimeoutException e) {
                System.out.println("Packet # " + seqNum + " timeout.");
                if(lostSeq == seqNum) 
                    timeoutCount++;
                else {
                    timeoutCount = 1;
                    lostSeq = seqNum;
                }
                if(timeoutCount == 3) {
                    System.out.println("Packet # " + seqNum + " is deemed lost.");
                    lostPacket = true;
                    timeoutCount = 0;
                } 
                if(seqNum != 0)
                    i--;
            }
            
            if(!doublePacket && !discardPacket)
                expectedSeq = seqNum;
            if(seqNum != 0) {
                serverSocket.setSoTimeout(0);
                temp = (Media) dataQueue.get(i);
                ack = Integer.toString(temp.getID()).getBytes();
                DatagramPacket sendPacket = new DatagramPacket(ack, ack.length, _IPAddress, _port);   
                serverSocket.send(sendPacket);
                System.out.println("SENT ACK#: " + temp.getID());
                if(discardNextPacket || doublePacket) {
                    dataQueue.remove(i);
                    doublePacket = false;
                    i--;
                }
                i++;
                if(discardPacket) {
                    discardNextPacket = true;
                }
            }
            //Thread.sleep(10);
        }
        
        System.out.println("Packet transfer is completed.");
        System.out.println("Number of Packets Received: " + dataQueue.size());
        length = totalLength;
        j = 1499;
        i = 0;
        while (length > 0) {
            temp = (Media) dataQueue.remove();
            data = temp.getBytes();
            if(j < totalLength) {
                System.arraycopy(data, 0, wholeFile, i, data.length);
            } else {
                int diff = j - totalLength;
                j -= diff;
                System.arraycopy(data, 0, wholeFile, i, data.length);
            }
            i = j;
            j += 1500;
            length -= 1500; 
        }
        
        return wholeFile;
    }
    
    private Object deserialize(byte[] bytes, String _type) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectStream = new ObjectInputStream(byteStream);
        if (_type.equalsIgnoreCase("Media"))
            return (Media) objectStream.readObject();
        else 
              return (Multimedia) objectStream.readObject();  
    }
    
    public void writeToDisk(byte[] _wholeFile, String _location) throws Exception {
        /* Check first if directories exists */
        if(!new File(searchPath + "\\Multimedia\\").isDirectory()) {
            new File(searchPath + "\\Multimedia\\").mkdirs();
        } 
        
        FileOutputStream fileOuputStream = new FileOutputStream(_location); 
	fileOuputStream.write(_wholeFile);
	fileOuputStream.close();
        System.out.println("Created file!");
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel allThumbnails;
    private javax.swing.JLabel imageViewer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panel;
    // End of variables declaration//GEN-END:variables
}

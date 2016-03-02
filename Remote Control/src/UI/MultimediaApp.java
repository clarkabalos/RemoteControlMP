package UI;

import Bean.Media;
import Bean.Photo;
import com.sun.jna.NativeLibrary;
import java.awt.Canvas;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class MultimediaApp extends javax.swing.JFrame {
    //list of multimedia files
    private ArrayList<Media> Multimedia = new ArrayList<>();
    private ArrayList<Photo> Photos = new ArrayList<>();
    
    private File[] files;
    private JLabel[] label;
    private int index = 0;
    private Timer time;
    
    //media player-related vars
    private MediaPlayerFactory mediaPlayerFactory;
    private EmbeddedMediaPlayer mediaPlayer;
    private Canvas canvas;
    
    private final String searchPath = new File("").getAbsolutePath();
    
    //socket programming-related vars
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
    
    public void showFilesInFolder(File folder) {
        Image thumbnail = null;
        ImageIcon icon = null;
        Photo temp = null;
        files = folder.listFiles();
        label = new JLabel[files.length];
        for(int i = 0; i < files.length; i++) {
            String path = files[i].getAbsolutePath();
            int x = path.lastIndexOf('\\');
            String name = path.substring(x+1);
            if(name.endsWith("jpg") || name.endsWith("mp3") || name.endsWith("mp4")) {
                if(name.endsWith("jpg")) {
                    Photo photo = new Photo(name, path);
                    Photos.add(photo);
                    try{
                        thumbnail = ImageIO.read(new File(Photos.get(i).getPath())).getScaledInstance(90, 90, Image.SCALE_SMOOTH);
                    }catch(IOException e){
                      System.out.println(e);
                    }
                    //thumbnail = photo.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
                } else {
                    if(name.endsWith("mp3")) {
                        temp = new Photo(name, "audio.png");
                    } else {
                        temp = new Photo(name, "video.png");
                    }
                    Media media = new Media(name, path);
                    Photos.add(temp);
                    Multimedia.add(media);
                    try{
                        thumbnail = ImageIO.read(new File(temp.getPath())).getScaledInstance(90, 90, Image.SCALE_SMOOTH);
                    }catch(IOException e){
                      System.out.println(e);
                    }
                    //thumbnail = temp.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
                } 
                icon = new ImageIcon(thumbnail);
                label[i] = new JLabel();
                label[i].setIcon(icon);
                label[i].setText("");
                allThumbnails.add(label[i]);
                allThumbnails.repaint();
                allThumbnails.updateUI();
            } 
        }
    }
    
    public void showImage(ImageIcon icon) {
        imageViewer.setIcon(icon);
    }
    
    public void nextImage() {
        if(index < Photos.size() - 1) {
            index++;
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
        for(int i = 0; i < Multimedia.size(); i++) {
            String check = Photos.get(index).getFileName();
            if(check.equals(Multimedia.get(i).getTitle())) {
                if(check.contains(".mp4")) {
                    panel.setVisible(true);
                } else {
                    panel.setVisible(false);
                }
                
                mediaPlayer.playMedia(Multimedia.get(i).getPath());
                return;
            }
        }
    }
    
    public void stop() {
        mediaPlayer.stop();
        panel.setVisible(false);
    }
    
    public void sendFileName(InetAddress _IPAddress, int _port) throws IOException {
        byte[] sendData = new byte[1500];
        String fileName = Photos.get(index).getFileName();
        sendData = fileName.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, _IPAddress, _port);
        serverSocket.send(sendPacket);
    }
    
    public void sendPreview(InetAddress _IPAddress, int _port) throws IOException {
        File file = new File(Photos.get(index).getFileName());
        
        //FileInputStream fileStream = new FileInputStream(file);
            //BufferedInputStream fileBuffer = new BufferedInputStream(fileStream);
            /*OutputStream out = sendSocket.getOutputStream();
            int count;
            while ((count = fileBuffer.read(data)) > 0) {
                System.out.println("Data Sent : " + count);
                out.write(data, 0, count);
                out.flush();
            }
            out.close();
            fileBuffer.close();
            fileStream.close();*/
            
        byte[] sendDataa = new byte[1500];
        BufferedImage bufferedImg = ImageIO.read(new File(Photos.get(index).getPath()));
        
        Image image = bufferedImg.getScaledInstance(210, 120, Image.SCALE_SMOOTH);
        
        BufferedImage img = new BufferedImage(210, 120, BufferedImage.TYPE_INT_RGB);
        img.getGraphics().drawImage(image, 0, 0 , null);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        /*ObjectOutputStream os = new ObjectOutputStream(outputStream);
        os.writeObject(Photos.get(index));*/
        ImageIO.write(img, "jpg", outputStream);
        outputStream.flush();
        byte[] buffer = outputStream.toByteArray();
        int i = 0;
	int j = sendDataa.length - 1;
        int length = buffer.length;
        int count = 1;
        String sentence = String.valueOf(length);
        sendDataa = sentence.getBytes();
        DatagramPacket sendPacket1 = new DatagramPacket(sendDataa, sendDataa.length, _IPAddress, _port);       
        serverSocket.send(sendPacket1);  
        
        while(length > 0) {
            byte[] sendData = new byte[1500];
            if(j < buffer.length) {
                System.out.println("First");
		sendData = Arrays.copyOfRange(buffer, i, j);
            } else {
                System.out.println("Second");
                int diff = j - buffer.length;
                j -= diff;
                sendData = Arrays.copyOfRange(buffer, i, j);
                
                System.out.println("FINALLY");
                System.out.println("J: " + j);
            }
    System.out.println("Data: " + sendData.length + " x " + count);
            DatagramPacket sendPacket2 = new DatagramPacket(sendData, sendData.length, _IPAddress, _port);       
            serverSocket.send(sendPacket2);  
            i = j;
            j += 1500;
            length -= 1500;
            count++;
            System.out.println("Length: " + length);
        }
        //sendData = outputStream.toByteArray();
        System.out.println("Length: " + buffer.length);
        //DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, _IPAddress, _port);
        //serverSocket.send(sendPacket);
        //os.close();
        outputStream.close();
        /*FileInputStream fileStream = new FileInputStream(file);
        BufferedInputStream fileBuffer = new BufferedInputStream(fileStream);
        int count = 0;
        while(fileBuffer.read(sendData) > 0) { 
            System.out.println("Data sent: " + count);
            count++;
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, _IPAddress, _port);
            serverSocket.send(sendPacket);
        }
        fileBuffer.close();
        fileStream.close();*/
        //ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //sendData = outputStream.toByteArray();	
        //DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, _IPAddress, _port);
        
        /*BufferedImage img = ImageIO.read(new File("src/test.jpg"));
 ByteArrayOutputStream baos = new ByteArrayOutputStream();        
 ImageIO.write(img, "jpg", baos);
 baos.flush();
 byte[] buffer = baos.toByteArray();*/
        
    }
    
    public void setFileDetails() throws FileNotFoundException, IOException {
        File file = new File(Photos.get(index).getPath());
        DataInputStream diStream = new DataInputStream(new FileInputStream(file));
        long len = (int) file.length();
        byte[] fileBytes = new byte[(int) len];
        int read = 0;
        int numRead = 0;
        while (read < fileBytes.length && (numRead = diStream.read(fileBytes, read,
            fileBytes.length - read)) >= 0) {
            read = read + numRead;
        }
    }
    public void sendToClient(InetAddress _IPAddress, int _port) throws IOException {
        byte[] sendData = new byte[1500];
        String temp = "Slideshow is currently ongoing.";
        sendData = temp.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, _IPAddress, _port);
        serverSocket.send(sendPacket);
    }
    
    public void slideshow(int i) { 
        /*final InetAddress IPAddress = _IPAddress;
        final int port = _port;*/
        time = new Timer(i,new ActionListener() { 
            @Override public void actionPerformed(ActionEvent e) { 
                String check = Photos.get(index).getFileName();
                /*try {
                    sendFileName(IPAddress, port);
                } catch (IOException ex) {
                    Logger.getLogger(MultimediaApp.class.getName()).log(Level.SEVERE, null, ex);
                }*/
                if(check.contains(".jpg")) {
                    showImage(setImageSize(index));
                    index++;
                } else {
                    index++;
                }
                
                if(index >= Photos.size())
                    index = 0; 
            } 
        }); 
        time.start(); 
    }  
    
    public void stopSlideshow() {
        time.stop();
    }
    
    public ImageIcon setImageSize(int i) { 
        Image img = null; //Photos.get(i).getImage().getScaledInstance(imageViewer.getWidth(), imageViewer.getHeight(), Image.SCALE_SMOOTH);
        try{
            img = ImageIO.read(new File(Photos.get(i).getPath())).getScaledInstance(imageViewer.getWidth(), imageViewer.getHeight(), Image.SCALE_SMOOTH);
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
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel allThumbnails;
    private javax.swing.JLabel imageViewer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panel;
    // End of variables declaration//GEN-END:variables
}

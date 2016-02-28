package UI;

import Bean.Media;
import Bean.Photo;
import com.sun.jna.NativeLibrary;
import java.awt.Canvas;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
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
    private InetAddress IPAddress;
    private int port;
    
            
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

        jScrollPane1.setViewportView(allThumbnails);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 720, 100));

        jLabel1.setBackground(new java.awt.Color(153, 153, 153));
        jLabel1.setFont(new java.awt.Font("Calibri Light", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Multimedia Application");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 4, 710, 50));

        panel.setBackground(new java.awt.Color(0, 0, 0));
        panel.setLayout(new java.awt.BorderLayout());
        getContentPane().add(panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 720, 430));
        getContentPane().add(imageViewer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 720, 430));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    
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
                try {
                    request("isPlaying");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return;
            }
        }
    }
    
    public void stop() {
        mediaPlayer.stop();
        panel.setVisible(false);
        try {
            request("isNotPlaying");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
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
                    thumbnail = photo.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
                } else {
                    if(name.endsWith("mp3")) {
                        temp = new Photo(name, "audio.png");
                    } else {
                        temp = new Photo(name, "video.png");
                    }
                    Media media = new Media(name, path);
                    Photos.add(temp);
                    Multimedia.add(media);
                    thumbnail = temp.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
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
            checkFile();
            showImage(setImageSize(index));
        } 
    }
    
    public void prevImage() {
        if(index > 0) {
            index--;
            checkFile();
            showImage(setImageSize(index));
        }
    }
    
    public void checkFile() {
        String check = Photos.get(index).getFileName();
        if(check.contains(".mp3") || check.contains(".mp4")) {
            try {
                request("isMedia");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                request("isNotMedia");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void slideshow(int i) { 
        time = new Timer(i,new ActionListener() { 
            @Override public void actionPerformed(ActionEvent e) { 
                String check = Photos.get(index).getFileName();
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
        Image img = Photos.get(i).getImage().getScaledInstance(imageViewer.getWidth(), imageViewer.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(img); 
        return icon; 
    }
    
    public void editTimer(int i) {
        if(time.isRunning()) {
            time.stop();
            slideshow(i);
        }
    }
    
    public void request(String request) throws Exception {
        byte[] sendRequest = new byte[4];
        sendRequest = request.getBytes();   
        DatagramPacket sendPacket = new DatagramPacket(sendRequest, sendRequest.length, IPAddress, port);       
        serverSocket.send(sendPacket);  
    }
    
    public void sendFileNames(InetAddress _IPAddress, int _port) throws IOException {
        byte[] sendData = new byte[1500];
        IPAddress = _IPAddress;
        port = _port;
        
        for(int i = 0; i < Photos.size(); i++) {
            String fileName = Photos.get(i).getFileName();
            sendData = fileName.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
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

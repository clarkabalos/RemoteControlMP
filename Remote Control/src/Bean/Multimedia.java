package Bean;

import java.awt.Image;
//import java.io.File;
//import java.io.IOException;
import java.io.Serializable;
//import javax.imageio.ImageIO;

public class Multimedia implements Serializable {
    private String fileName;
    private String filePath;
    private String thumbnailPath;
    private long thumbnailLength;
    private long length;
    private String type;
    //private Image img;
    
    public Multimedia(String _fileName, String _filePath){
        fileName = _fileName;
        filePath = _filePath;
        /*try{
            img = ImageIO.read(new File(imgPath));
        }catch(IOException e){
          System.out.println(e);
        }*/
    }

    public String getFileName() {
        return fileName;
    }

    public String getPath() {
        return filePath;
    }
    
    public void setPath(String _filePath) {
        filePath = _filePath;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String _type) {
        type = _type;
    }
    
    public long getLength() {
        return length;
    }
    
    public void setLength(long _length) {
        length = _length;
    }
    
    public String getThumbnailPath() {
        return thumbnailPath;
    }
    
    public void setThumbnailPath(String _thumbnailPath) {
        thumbnailPath = _thumbnailPath;
    }
    
    public long getThumbnailLength() {
        return thumbnailLength;
    }
    
    public void setThumbnailLength(long _thumbnailLength) {
        thumbnailLength = _thumbnailLength;
    }
}

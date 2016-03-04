package Bean;

import java.awt.Image;
//import java.io.File;
//import java.io.IOException;
import java.io.Serializable;
//import javax.imageio.ImageIO;

public class Multimedia implements Serializable {
    private String fileName;
    private String filePath;
    //private String thumbnail;
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
    
    /*public Image getThumbnail() {
        return thumbnail;
    }*/
    
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
    /*public void setThumbnail(Image _thumbnail) {
        thumbnail = _thumbnail;
    }*/
}

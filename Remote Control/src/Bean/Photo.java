package Bean;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.imageio.ImageIO;

public class Photo implements Serializable {
    private String fileName;
    private String imgPath;
    //private Image img;
    
    public Photo(String _fileName, String _imgPath){
        fileName = _fileName;
        imgPath = _imgPath;
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
        return imgPath;
    }
    
    /*public Image getImage() {
        return img;
    }*/
}

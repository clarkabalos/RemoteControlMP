package Bean;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Photo {
    private String fileName;
    private String imgPath;
    private Image img;
    
    public Photo(String _fileName, String _imgPath){
        fileName = _fileName;
        imgPath = _imgPath;
        try{
            img = ImageIO.read(new File(imgPath));
        }catch(IOException e){
          System.out.println(e);
        }
    }

    public String getFileName() {
        return fileName;
    }

    public Image getImage() {
        return img;
    }
}

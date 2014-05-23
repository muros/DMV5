import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class TestDMV5 {

  public static void main(String[] args) throws Exception {
    BufferedImage image = null;
    FileInputStream fin = null;
    ImageInputStream iis = null;

    fin = new FileInputStream(args[0]);
    Iterator readers = ImageIO.getImageReadersByFormatName("DMV5");
    ImageReader imageReader = (ImageReader) readers.next();
    iis = ImageIO.createImageInputStream(fin);
    imageReader.setInput(iis, false);
    int num = 0;
    num = imageReader.getNumImages(true);
    for (int i = 0; i < num; ++i) {
      image = imageReader.read(i);
    }
    fin.close();
    
    ImageIO.write(image, "tif",new File("out.tif"));
    
    System.out.println("Done.");
  }

}

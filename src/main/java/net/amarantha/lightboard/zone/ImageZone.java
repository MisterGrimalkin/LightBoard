package net.amarantha.lightboard.zone;

import net.amarantha.lightboard.entity.Pattern;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageZone extends AbstractZone {

    private Pattern imagePattern;
    private String filename;

    @Override
    public Pattern getNextPattern() {
        return imagePattern;
    }

    @Override
    public void init() {
        super.init();
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        convertImage(Scalr.resize(image, Scalr.Mode.FIT_TO_WIDTH, getWidth(), getHeight()));
    }

    public ImageZone addImage(String filename) {
        this.filename = filename;
        return this;
    }

    private void convertImage(BufferedImage image) {
        double[][][] convertedImage = new double[4][image.getHeight()][image.getWidth()];
        for ( int row=0; row<image.getHeight(); row++ ) {
            for ( int col=0; col<image.getWidth(); col++ ) {
                int[] pixel = (image.getRaster().getPixel(col,row,new int[3]));
                double red = pixel[0]/255.0;
                double green = pixel[1]/255.0;
                double blue = pixel[2]/255.0;
                if ( row==0 || row==image.getHeight()-1 || col==0 || col==image.getWidth()-1 ) {
                    // put blank space around image
                    convertedImage[0][row][col] = 0.0;
                    convertedImage[1][row][col] = 0.0;
                    convertedImage[2][row][col] = 0.0;
                    convertedImage[3][row][col] = 0.0;
                } else {
                    convertedImage[0][row][col] = red;
                    convertedImage[1][row][col] = green;
                    convertedImage[2][row][col] = blue;
                    convertedImage[3][row][col] = (red+green+blue) / 3;     // colour 3 is a grey-scale value (not really used anymore)
                }
            }
        }
        imagePattern = new Pattern(convertedImage);
    }

}

package net.amarantha.lightboard.zone.impl;

import com.google.inject.Inject;
import net.amarantha.lightboard.entity.Edge;
import net.amarantha.lightboard.entity.Pattern;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.PropertyManager;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.lightboard.zone.LightBoardZone;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.imgscalr.Scalr.Mode;

public class ImageZone extends LightBoardZone {

    public ImageZone scrollUp() {
        scroll(Edge.BOTTOM, Edge.TOP);
        return this;
    }

    Pattern imagePattern;
    double[][][] convertedImage;
    boolean[][] convertedImageBinary;

    @Inject
    public ImageZone(LightBoardSurface surface, Sync sync) {
        super(surface, sync);
        scroll(Edge.BOTTOM, Edge.TOP);
        clear(false);
        setRestDuration(2000);
    }

    public ImageZone loadImage(String filename) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        convertImage(Scalr.resize(image, Mode.FIT_TO_WIDTH, getRegion().width, getRegion().height));
        return this;
    }

    @Override
    public int getContentWidth() {
        return convertedImage[0][0].length;
    }

    @Override
    public int getContentHeight() {
        return convertedImage[0].length;
    }

    @Override
    public boolean render() {
        boolean drawn = drawPattern(0, 0, imagePattern, true);
        return drawn;
    }

    private void convertImage(BufferedImage image) {
        convertedImage = new double[4][image.getHeight()][image.getWidth()];
        convertedImageBinary = new boolean[image.getHeight()][image.getWidth()];
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
                    convertedImageBinary[row][col] = ( red>=0.5 || green>=0.5 || blue>=0.5 );
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

package lightboard.zone.impl;

import lightboard.surface.LightBoardSurface;
import lightboard.util.MessageQueue;
import lightboard.zone.LightBoardZone;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.imgscalr.Scalr.Mode;
import static org.imgscalr.Scalr.resize;

public class ImageZone extends LightBoardZone {

    public static ImageZone fixed(LightBoardSurface s) {
        ImageZone zone = new ImageZone(s);
        zone.scroll(MessageQueue.Edge.NO_SCROLL, MessageQueue.Edge.NO_SCROLL);
        return zone;
    }

    public static ImageZone scrollUp(LightBoardSurface s) {
        ImageZone zone = new ImageZone(s);
        zone.scroll(MessageQueue.Edge.BOTTOM_EDGE, MessageQueue.Edge.TOP_EDGE);
        return zone;
    }

    public static ImageZone scrollLeft(LightBoardSurface s) {
        ImageZone zone = new ImageZone(s);
        zone.scroll(MessageQueue.Edge.RIGHT_EDGE, MessageQueue.Edge.LEFT_EDGE);
        return zone;
    }

    double[][][] convertedImage;
    boolean[][] convertedImageBinary;

    public ImageZone(LightBoardSurface surface) {
        super(surface);
        scroll(MessageQueue.Edge.BOTTOM_EDGE, MessageQueue.Edge.TOP_EDGE);
        clear(false);
        setRestDuration(2000);
    }

    public void loadImage(String filename) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        convertImage(resize(image, Mode.FIT_TO_WIDTH, getRegion().width, getRegion().height));
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
        boolean drawn = false;
        switch ( getBoardType() )  {
            case BINARY:
                drawn = drawPattern(0, 0, convertedImageBinary, true);
                break;
            case COLOUR:
                drawn = drawPattern(0, 0, convertedImage, true);
                break;
        }
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
    }

}

package lightboard.board.zone.impl;

import lightboard.board.surface.LightBoardSurface;
import lightboard.board.zone.LightBoardZone;
import lightboard.util.MessageQueue;

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
            case MONO:
                drawn = drawPattern(0, 0, convertedImage[3], true);
                break;
            case POLY:
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
                int red = pixel[0];
                int green = pixel[1];
                int blue = pixel[2];
                int avg = ( red+green+blue ) / 3;
                if ( row==0 || row==image.getHeight()-1 || col==0 || col==image.getWidth()-1 ) {
                    convertedImage[0][row][col] = 0.0;
                    convertedImage[1][row][col] = 0.0;
                    convertedImage[2][row][col] = 0.0;
                    convertedImage[3][row][col] = 0.0;
                } else {
                    convertedImageBinary[row][col] = (avg/255.0)>=0.5;
                    convertedImage[0][row][col] = red / 255.0;
                    convertedImage[1][row][col] = green / 255.0;
                    convertedImage[2][row][col] = blue / 255.0;
                    convertedImage[3][row][col] = avg / 255.0;
                }
            }
        }
    }

    private enum ImageMode { BINARY, MONO, POLY }
}

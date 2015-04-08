package lightboard.zone.impl;

import lightboard.board.GrayscaleLightBoardSurface;
import lightboard.board.LightBoardSurface;
import lightboard.util.MessageQueue;
import lightboard.zone.GrayscaleLBZone;
import lightboard.zone.LBZone;

import java.awt.image.BufferedImage;

/**
 * Created by grimalkin on 08/04/15.
 */
public class ImageZone extends GrayscaleLBZone {

    double[][] convertedImage;

    private BufferedImage image;

    public ImageZone(GrayscaleLightBoardSurface surface, BufferedImage image) {
        super(surface);
        convertedImage = convertImage(image);
        scroll(MessageQueue.Edge.BOTTOM_EDGE, MessageQueue.Edge.TOP_EDGE);
        clear(false);
        restDuration(2000);
    }

    @Override
    public int getContentWidth() {
        return convertedImage[0].length;
    }

    @Override
    public int getContentHeight() {
        return convertedImage.length;
    }

    @Override
    public boolean render() {
        boolean drawn = drawPattern(0,0,convertedImage, true);
        return drawn;
    }

    private double[][] convertImage(BufferedImage image) {
        double[][] result = new double[image.getHeight()][image.getWidth()];
        for ( int row=0; row<image.getHeight(); row++ ) {
            for ( int col=0; col<image.getWidth(); col++ ) {
                int[] pixel = (image.getRaster().getPixel(col,row,new int[3]));
                int red = pixel[0];
                int green = pixel[1];
                int blue = pixel[2];
                int avg = ( red+green+blue ) / 3;
                result[row][col] = avg;
            }
        }
        return result;
    }

    @Override
    public void onScrollComplete() {

    }
}

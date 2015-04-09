package lightboard.board.zone.impl;

import lightboard.board.surface.MonochromeLightBoardSurface;
import lightboard.board.zone.MonochromeLBZone;
import lightboard.util.MessageQueue;

import java.awt.image.BufferedImage;

import static org.imgscalr.Scalr.Mode;
import static org.imgscalr.Scalr.resize;

/**
 * Created by grimalkin on 08/04/15.
 */
public class ImageZone extends MonochromeLBZone {

    double[][] convertedImage;

    private ImageMode mode = ImageMode.MONO;

    public ImageZone(MonochromeLightBoardSurface surface, BufferedImage image) {
        super(surface);
        scroll(MessageQueue.Edge.BOTTOM_EDGE, MessageQueue.Edge.TOP_EDGE);
        convertedImage = convertImage(resize(image, Mode.FIT_TO_WIDTH, getRegion().width, getRegion().height));
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
                if ( row==0 || row==image.getHeight()-1 || col==0 || col==image.getWidth()-1 ) {
                    result[row][col] = 0.0;
                } else {
                    switch ( mode ) {
                        case BINARY:
                            result[row][col] = (avg/255.0)>=0.5 ? 1.0 : 0.0;
                            break;
                        case MONO:
                            result[row][col] = avg / 255.0;
                            break;
                        case POLY:
                            break;
                    }
                }
            }
        }
        return result;
    }

    private enum ImageMode { BINARY, MONO, POLY }
}

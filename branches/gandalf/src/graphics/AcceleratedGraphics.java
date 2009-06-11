package graphics;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.VolatileImage;

/**
 * @author kbok
 * Provides routines for accelerated-enabled blits.
 */
public class AcceleratedGraphics {
    // This method draws a volatile image and returns it or possibly a
    // newly created volatile image object. Subsequent calls to this method
    // should always use the returned volatile image.
    // If the contents of the image is lost, it is recreated using orig.
    // img may be null, in which case a new volatile image is created.
    /**
     * Draws a VolatileImage on the given Graphics2D. The blit is accelerated unless
     * there is too much errors with the VolatileImage. If the VolatileImage has
     * been reseted then it is reconstructed from the given Image.
     * @param g The Graphics2D on which to draw the image.
     * @param img The VolatileImage to draw.
     * @param sx Source X of the part of image to blit.
     * @param sy Source Y of the part of image to blit.
     * @param w Width of the image to blit.
     * @param h Height of the image to blit.
     * @param x Destination X of the image to blit.
     * @param y Destination Y of the image to blit.
     * @param orig Original image used to reconstruct the VolatileImage.
     * @return An Updated VolatileImage.
     */
    public static VolatileImage drawVolatileImage(Graphics2D g, VolatileImage img,
                                           int sx, int sy, int w, int h, int x, int y, Image orig) {
        final int MAX_TRIES = 100;
        for (int i=0; i<MAX_TRIES; i++) {
            if (img != null) {
                // Draw the volatile image
                g.drawImage(img, x, y, x+w, y+h, sx, sy, sx+w, sy+h, null);
    
                // Check if it is still valid
                if (!img.contentsLost()) {
                    return img;
                }
            } else {
                // Create the volatile image
                img = g.getDeviceConfiguration().createCompatibleVolatileImage(
                    orig.getWidth(null), orig.getHeight(null));
            }
    
            // Determine how to fix the volatile image
            switch (img.validate(g.getDeviceConfiguration())) {
            case VolatileImage.IMAGE_OK:
                // This should not happen
                break;
            case VolatileImage.IMAGE_INCOMPATIBLE:
                // Create a new volatile image object;
                // this could happen if the component was moved to another device
                img.flush();
                img = g.getDeviceConfiguration().createCompatibleVolatileImage(
                    orig.getWidth(null), orig.getHeight(null));
            case VolatileImage.IMAGE_RESTORED:
                // Copy the original image to accelerated image memory
                Graphics2D gc = (Graphics2D)img.createGraphics();
                gc.drawImage(orig, 0, 0, null);
                gc.dispose();
                break;
            }
        }
    
        // The image failed to be drawn after MAX_TRIES;
        // draw with the non-accelerated image
        g.drawImage(orig, x, y, null);
        return img;
    }

}

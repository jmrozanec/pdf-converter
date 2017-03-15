package pdf.converter.img;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.ghost4j.document.DocumentException;
import org.ghost4j.document.PDFDocument;
import org.ghost4j.renderer.RendererException;
import org.ghost4j.renderer.SimpleRenderer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImgCreator {
//    private static final Logger log = LoggerFactory.getLogger(ImgCreator.class);

    /**
     * @param pdf - File to be processed;
     * @param output - output directory, where images will be written;
     * @param resolution - output images resolution (usually 300+ for print quality, 72 for web);
     * @param extension - "png" or "jpg"
     *
     */
    public void process(File pdf, File output, int resolution, int width, int height, ImageFileExtension extension){
        PDFDocument document = new PDFDocument();
        try {
            document.load(pdf);
            for(int page=0; page<document.getPageCount();page++){
                SimpleRenderer renderer = new SimpleRenderer();
                renderer.setResolution(resolution);
                java.util.List<Image> images = renderer.render(document, page, page);
                if(!images.isEmpty()){
                    persistImage(toBufferedImage(images.get(0)), width, height, new File(output, String.format("%05d.%s", page+1, extension.toString().toLowerCase())));
                }
            }
        } catch (IOException |RendererException |DocumentException e) {
//            log.warn(String.format("Failed to create images for document: %s", pdf.getName()), e);
        }
    }

    /*
     * Kudos to: http://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage
     */
    private BufferedImage toBufferedImage(Image img){
        if (img instanceof BufferedImage){
            return (BufferedImage) img;
        }
        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        // Return the buffered image
        return bimage;
    }

    private void persistImage(BufferedImage image, int width, int height, File output) throws IOException {
        output.getParentFile().mkdirs();
        ImageIO.write(Thumbnails.of(image).size(width, height).asBufferedImage(), FilenameUtils.getExtension(output.getAbsolutePath()), output);
    }
}

package pdf.converter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import pdf.converter.epub.EpubCreator;
import pdf.converter.img.ImageFileExtension;
import pdf.converter.img.ImgCreator;
import pdf.converter.zip.ZipCreator;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PdfConverter {
    private static final int RESOLUTION = 300;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 800;
    private File pdf;

    private PdfConverter(File pdf){
        this.pdf = pdf;
    }

    public static PdfConverter convert(File pdf){
        return new PdfConverter(pdf);
    }

    public void intoImage(File output, int resolution, int width, int height, ImageFileExtension format){
        ImgCreator creator = new ImgCreator();
        creator.process(pdf, output, resolution, width, height, format);
    }

    public void intoEpub(String title, File output){
        try {
            File imgsdir = File.createTempFile(UUID.randomUUID().toString(), "");
            imgsdir.mkdirs();
            ImgCreator creator = new ImgCreator();
            creator.process(pdf, imgsdir, RESOLUTION, WIDTH, HEIGHT, ImageFileExtension.PNG);
            EpubCreator epubCreator = new EpubCreator();
            epubCreator.create(title, imgsdir, output);
            FileUtils.deleteDirectory(imgsdir);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void intoEpub(File imgsdir, String title, File output){
        EpubCreator epubCreator = new EpubCreator();
        try {
            epubCreator.create(title, imgsdir, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void intoZip(String title, File output){
        try {
            File imgsdir = File.createTempFile(UUID.randomUUID().toString(), "");
            imgsdir.mkdirs();
            ImgCreator creator = new ImgCreator();
            creator.process(pdf, imgsdir, RESOLUTION, WIDTH, HEIGHT, ImageFileExtension.PNG);
            ZipCreator zipCreator = new ZipCreator();
            zipCreator.create(imgsdir, output);
            FileUtils.deleteDirectory(imgsdir);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void intoZip(File imgsdir, String title, File output){
        ZipCreator zipCreator = new ZipCreator();
        try {
            zipCreator.create(imgsdir, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

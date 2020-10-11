package pdf.converter.txt;

import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class TxtCreator {
    private static final Logger log = LoggerFactory.getLogger(TxtCreator.class);

    public void process(File pdf, File output){
        PDDocument pdDoc;
        try {//Kudos for closing: http://stackoverflow.com/questions/156508/closing-a-java-fileinputstream
            pdDoc = PDDocument.load(pdf);
            FileWriter writer = new FileWriter(output);
            try {
                PDFTextStripper stripper = new PDFTextStripper();
                int numberOfPages = pdDoc.getNumberOfPages();

                for (int j = 1; j < numberOfPages+1; j++) {
                    stripper.setStartPage(j);
                    stripper.setEndPage(j);
                    writer.write(stripper.getText(pdDoc));
                    writer.flush();
                }
            } finally {
                pdDoc.close();
                writer.close();
            }
        } catch (IOException ioe) {
            log.warn(String.format("Failed to create txt for file: %s", pdf.getName()), ioe);
        }
    }
}

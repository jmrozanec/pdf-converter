package pdf.converter.docx;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.docx4j.Docx4J;
import org.docx4j.convert.in.Doc;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

public class DocToPDFConverter extends Converter{


    public DocToPDFConverter(InputStream, inStream, OutputStream outStream, boolean showMessages, boolean closeStreamWhenComplete){
        super(inStream, outStream, showMessages, closeStreamWhenComplete);
    }

    @Override
    public void convert() throws Exception{
        
        loading();

        InputStream iStream = inStream;

        WordprocessingMLPackage wordMLPackage = getMLPackage(iStream);

        processing();
        Docx4J.toPDF(wordMLPackage, outStream);

        finished();
    }

    protected WordprocessingMLPackage getMLPackage(InputStream iStream) throws Exception {
        PrintStream originalStdout = System.out;

        System.setOut(new PrintStream(new OutputStream(){

            public void write(int b){
                //Do Everithing
            }
        }));

        WordprocessingMLPackage mlPackage = Doc.convert(iStream);

        System.setOut(originalStdout);
        return mlPackage;
    }
}
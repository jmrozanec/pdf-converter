package pdf.converter.epub;

import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class EpubCreator {
    private String timestamp;
    private String uuid;
    private File basedir;
    private ClassLoader classLoader;
    private String title;
    private File imgsDir;


    public EpubCreator(){
    }

    public void create(String title, File imgsDir, File output) throws IOException {
        timestamp = DateTimeFormat.forPattern("yyyy-MM-dd'T'hh:mm:ssSZZ").print(DateTime.now());
        uuid = UUID.randomUUID().toString();
        this.title = title;
        this.imgsDir = imgsDir;

        try {
            basedir = File.createTempFile(uuid,"");
            basedir.delete();
            basedir.mkdirs();
        } catch (IOException e) {
            e.printStackTrace();
        }
        classLoader = getClass().getClassLoader();

        copyImages();
        copyStandardFilez();
        createOPFFile();
        createIndex();
        createTitlePage();
        createTOC();
        pack(basedir.getAbsolutePath(), output.getAbsolutePath());
        FileUtils.deleteDirectory(basedir);
    }

    private void copyImages() throws IOException {
        File imagesDir = new File(basedir, "images");
        imagesDir.mkdirs();
        for(File file : listFiles()){
            IOUtils.copy(new FileInputStream(file), new FileOutputStream(new File(imagesDir, file.getName())));
        }
    }

    private void copyStandardFilez() throws IOException {
        File metainf = new File(basedir, "META-INF");
        metainf.mkdirs();
        writeFile(new File(metainf, "container.xml"), readFileFromSrc("epub/META-INF/container.xml"));

        writeFile(new File(basedir, "mimetype"), readFileFromSrc("epub/mimetype"));
        writeFile(new File(basedir, "page_styles.css"), readFileFromSrc("epub/page_styles.css"));
        writeFile(new File(basedir, "stylesheet.css"), readFileFromSrc("epub/stylesheet.css"));
    }

    private void createOPFFile() throws IOException {
        StringBuilder content = new StringBuilder();
        for(File file : listFiles()){
            content.append(String.format("<item href=\"images/%s\" id=\"%s\" media-type=\"image/png\"/>\n", file.getName(), idForImage(file.getName())));
        }
        String opf = readFileFromSrc("epub/content.opf");
        opf = opf
                .replace("$AUTHOR", "Unknown")
                .replace("$TIMESTAMP", timestamp)
                .replace("$TITLE", title)
                .replace("$UUID", uuid)
                .replace("$CONTENT", content.toString());

        writeFile(new File(basedir, "content.opf"), opf);
    }

    private void createIndex() throws IOException {
        StringBuilder content = new StringBuilder();
        for(File file : listFiles()){
            content.append(String.format("<p class=\"pdf-converter1\"><a id=\"%s\"></a><img src=\"images/%s\" class=\"pdf-converter2\"/></p>\n", idForImage(file.getName()), file.getName()));
        }
        String index = readFileFromSrc("epub/index.html");
        index = index.replace("$CONTENT", content.toString());

        writeFile(new File(basedir, "index.html"), index);
    }

    private void createTitlePage() throws IOException {
        String pagetitle = readFileFromSrc("epub/titlepage.xhtml");
        pagetitle = pagetitle.replace("$TITLE", title);

        writeFile(new File(basedir, "titlepage.xhtml"), pagetitle);
    }

    private void createTOC() throws IOException {
        String toc = readFileFromSrc("epub/toc.ncx");
        toc = toc.replace("$TITLE", title);

        writeFile(new File(basedir, "toc.ncx"), toc);
    }

    private void writeFile(File dest, String content) throws IOException {
        FileWriter writer = new FileWriter(dest);
        writer.write(content);
        writer.flush();
        writer.close();
    }

    private String readFileFromSrc(String path) throws IOException {
        return IOUtils.toString(classLoader.getResourceAsStream(path));
    }

    private String idForImage(String name){
        return String.format("id%s", name.replace(".png", ""));
    }

    private static void pack(String sourceDirPath, String zipFilePath) throws IOException {
        Path p = Files.createFile(Paths.get(zipFilePath));
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
            Path pp = Paths.get(sourceDirPath);
            Files.walk(pp)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                        try {
                            zs.putNextEntry(zipEntry);
                            zs.write(Files.readAllBytes(path));
                            zs.closeEntry();
                        } catch (Exception e) {
                            System.err.println(e);
                        }
                    });
        }
    }

    private List<File> listFiles(){
        File[] files = imgsDir.listFiles((dir, name) -> {
            return name.toLowerCase().endsWith(".png");
        });
        List<File> sorted = Lists.newArrayList(files);
        Collections.sort(sorted, (o1, o2) -> o1.toString().compareTo(o2.toString()));
        return sorted;
    }
}

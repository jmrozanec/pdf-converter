package pdf.converter;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class PdfConverterTest {

    @Test
    public void convert() throws Exception {
        Path path = copyToTmp();
        File dest = new File(path.toFile(), "mobydick.epub");
        PdfConverter
                .convert(new File(path.toFile(), "mobydick.pdf"))
                .intoEpub("Moby Dick", dest);
        Assert.assertTrue(dest.exists());
    }

    private Path copyToTmp() throws IOException {
        Path tmpdir = new File(String.format("/tmp/%s", UUID.randomUUID().toString())).toPath();
        System.out.println(tmpdir);
        Files.createDirectories(tmpdir);
        Path dest = new File(tmpdir.toFile(), "mobydick.pdf").toPath();
        ClassLoader classLoader = getClass().getClassLoader();
        Path file = new File(classLoader.getResource("mobydick.pdf").getFile()).toPath();
        Files.copy(file, dest, StandardCopyOption.REPLACE_EXISTING);
        return tmpdir;
    }
}
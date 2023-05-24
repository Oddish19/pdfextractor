package it.fabersystem.di.rb.pdfextractor;

import java.io.File;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

public class MainTest {
    
    
    private static final String resourceDir = "src" + File.separator + "test" +
            File.separator + "resources";
            
    @Test
    public void mainTest() {
        Path inputPath = Path.of(resourceDir, "RBIT04_VATREP_202202_2023_05_08_14_20_12.pdf");
        

        // java -jar programmadioddi.jar -i /home/tommy/pippo.pdf -o /home

        System.out.println(inputPath);
        String[] params = {"-i", inputPath.toAbsolutePath().toString()};

        Main.main(params);
    }
}

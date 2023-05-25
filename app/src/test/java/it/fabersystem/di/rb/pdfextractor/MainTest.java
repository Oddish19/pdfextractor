package it.fabersystem.di.rb.pdfextractor;

import java.io.File;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

public class MainTest {
    
    
    private static final String resourceDir = "src" + File.separator + "test" +
            File.separator + "resources";
            
    @Test
    public void mainTest() {
        Path inputPathpdf = Path.of(resourceDir, "RBIT04_VATREP_202202_2023_05_08_14_20_12.pdf");
        Path inputPathcsv = Path.of(resourceDir, "RBIT04_VATREP_202202_2023_05_08_14_20_12.csv");
        Path outputPath = Path.of(resourceDir);
        String[] params = {
            "-i", inputPathpdf.toAbsolutePath().toString(),
            "-c", inputPathcsv.toAbsolutePath().toString(), 
            "-o" , outputPath.toAbsolutePath().toString()
        };

        Main.main(params);
    }

}

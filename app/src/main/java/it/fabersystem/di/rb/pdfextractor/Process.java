package it.fabersystem.di.rb.pdfextractor;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class Process {
    
    public static String elabpdfbox (String PDFfile){
        String dateextract = "";
        try {
            File file = new File(PDFfile);
            PDDocument document = PDDocument.load(file);
                
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setStartPage(1);
            stripper.setEndPage(1); 
            
            String content = stripper.getText(document);
            dateextract = content;

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dateextract;  
        
    }

}

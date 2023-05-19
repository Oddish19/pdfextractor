package it.fabersystem.di.rb.pdfextractor;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class Process {
    
    public static String elabpdfbox (String PDFfile){
        String dateextract = "";
        String titolo = "";
        String Nreport = "";
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

        String regex1 = "Titolo suppl\\.\\s+(.+)";
        String regex2 = "(N\\. report *)(\\d+)";
        
        Pattern pattern = Pattern.compile(regex1);
        Matcher match = pattern.matcher(dateextract);
        System.out.println(dateextract);
      
        if (match.find()) {
            String match1 = match.group(1);
            
            titolo = match1;
            System.out.println("Testo dopo Titolo suppl.: " + match1);
        }

        Pattern pattern2 = Pattern.compile(regex2);
        Matcher matcher2 = pattern2.matcher(dateextract);
        boolean found = false;

        while (matcher2.find()) {
            String match2 = matcher2.group(2);
            Nreport = match2;
            System.out.println("Testo dopo N. report: " + match2);
            found = true;
        }

        if (!found) {
            System.out.println("Nessun match per la regex N. report");
        }
        dateextract = Nreport +" " +titolo;
        return dateextract;  
        
    }

}

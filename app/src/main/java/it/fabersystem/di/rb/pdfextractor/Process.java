package it.fabersystem.di.rb.pdfextractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

public class Process {
    
    public static String elabpdfbox (String PDFfile){
        String dateextract = "";

        //estrae tutto il contenuto della prima pagina
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
        
        try {
            File file = new File(PDFfile); // Specifica il percorso del tuo file PDF
            PDDocument document = PDDocument.load(file);
            
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            
            // Specifica le coordinate del rettangolo da cui estrarre il testo
            float x = 265; // Coordinata x del rettangolo
            float y = 2; // Coordinata y del rettangolo
            float width = 333; // Larghezza del rettangolo
            float height = 38; // Altezza del rettangolo
            
            stripper.addRegion("region", new java.awt.Rectangle((int) x, (int) y, (int) width, (int) height));
            
            for (int pageNumber = 1; pageNumber <= document.getNumberOfPages(); pageNumber++) {
                stripper.extractRegions(document.getPage(pageNumber));
            }
            
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return dateextract;  
        
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void  recuperadati (String pathcsv,String datiestratti) throws IOException{

        // prende il contenuto del file e lo mette in una stringa
        String fileContent = Files.readString(Path.of(pathcsv));
        System.out.println("il file contine: " +fileContent);
        
        // prendo anno e mese da nome file
        String annomese = fileContent.substring(14, 20);
        System.out.println("anno e mese: " +annomese);
        
        // metodo che estrae la categoria
        String categoria = categoria(datiestratti);
        System.out.println("Categoria del documento: " +categoria);



        
        //procedo con la creazione del file csv
        createcsv();
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void createcsv(){

    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String categoria(String datiestratti){
        
        String titolo = "";
        String Nreport = "";
        String regex1 = "Titolo suppl\\.\\s+(.+)";
        String regex2 = "(N\\. report *)(\\d+)";
        
        Pattern pattern = Pattern.compile(regex1);
        Matcher match = pattern.matcher(datiestratti);
        System.out.println(datiestratti);
      
        if (match.find()) {
            String match1 = match.group(1);
            
            titolo = match1;
           // titolo = titolo.replace(" ", "");
            System.out.println("Testo dopo Titolo suppl.: " + match1);
        }

        Pattern pattern2 = Pattern.compile(regex2);
        Matcher matcher2 = pattern2.matcher(datiestratti);
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
        datiestratti = ";" +Nreport +" - " +titolo;

        return datiestratti;
    }

}

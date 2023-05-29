package it.fabersystem.di.rb.pdfextractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Process {
    private static final Logger logger = LoggerFactory.getLogger(Process.class);

    public static String categoria (Path PDFfile){
        String datiestratti = "";
        
        //estrae tutto il contenuto della prima pagina
        try {
            File file = new File(PDFfile.toString());
            PDDocument document = PDDocument.load(file);
                
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setStartPage(1);
            stripper.setEndPage(1); 
            
            String content = stripper.getText(document);
            datiestratti = content;

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
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
        String categoria = Nreport +" - " +titolo;
       
        return categoria;  
        
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void  recuperadati (Path pathcsv,String categoria,int pag, Path out){

        // prende il contenuto del file e lo mette in una stringa
        String fileContent;
        try {
            fileContent = Files.readString(pathcsv);
            System.out.println("il file contine: " +fileContent);
            String puntoevirgola = ";";
            int indiceCarattere = fileContent.indexOf(puntoevirgola);
            String nomefile = fileContent.substring(0, indiceCarattere);
            String societa = fileContent.substring(indiceCarattere +1);
    
    
            // prendo anno e mese da nome file
            String annomese = fileContent.substring(14, 20);
            System.out.println("anno e mese: " +annomese);
            
            //dati estratti contiene la categoria
            String categoria1 = categoria;
            System.out.println("La categoria è: " +categoria1);
            
            //numero di pagine nel pdf
             System.out.println("Le pagine del pdf sono: " +pag);
            
            List<String> dataList = new ArrayList<>();
    
            // Aggiungi dati all'ArrayList
            dataList.add(nomefile);
            dataList.add(societa.replaceAll("\r\n", "").replaceAll("\n", ""));
            dataList.add("VATREP");
            dataList.add(annomese.substring(0, 4));
            dataList.add(categoria1);
            dataList.add("1");
            dataList.add(String.valueOf(pag));
            dataList.add(annomese);
            dataList.add(annomese.substring(4, 6));
           // dataList.add("\r\n");
            for (int i = 0; i < dataList.size(); i++) {
                String element = dataList.get(i);
                element = element.replaceAll("\n", "");
                dataList.set(i, element);
            }
    
            System.out.println(dataList);
            //procedo con la creazione del file csv
            createcsv(dataList, nomefile, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
       
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static int numpag(Path PDFfile){
        try {
            PDDocument document = PDDocument.load(new File(PDFfile.toString()));
            int numPages = document.getNumberOfPages();
            document.close();
            
            System.out.println("Le pagine del file pdf sono: "+numPages);
            return numPages;
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void createcsv (List<String> datalList, String nomefile,Path out){
        
        Path writePath = Paths.get(out.toAbsolutePath().toString(), nomefile+".csv");
        System.out.println(out);
        System.out.println("Creo il file: " + writePath.toAbsolutePath());

        StringBuilder newStr = new StringBuilder();
        datalList.forEach(s -> {
            newStr.append(s + ";");
        });

        String outString =  newStr.toString().substring(0, newStr.length() - 1);

        try {
            Files.writeString(writePath, outString, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            logger.error("Can't write file to path -> " + writePath.toAbsolutePath(),e);
            throw new RuntimeException("non riesco a scrivere nel documento");
        }
    }
   
}

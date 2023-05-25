package it.fabersystem.di.rb.pdfextractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


public class Process {
    
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
    public static void  recuperadati (String pathcsv,String categoria,int pag) throws IOException{

        // prende il contenuto del file e lo mette in una stringa
        String fileContent = Files.readString(Path.of(pathcsv));
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
        dataList.add(societa);
        dataList.add("VATREP");
        dataList.add(annomese);
        dataList.add(categoria1);
        dataList.add("1");
        dataList.add(String.valueOf(pag));
        dataList.add(annomese.substring(0, 4));
        dataList.add(annomese.substring(4, 6));
        System.out.println(dataList);
        //procedo con la creazione del file csv
        createcsv(dataList, nomefile);
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
    public static void createcsv (List<String> datalList, String nomefile){
        String csvFile = "C:\\Users\\marco.oddi\\Desktop\\pdfbox\\app\\src\\test\\resources\\" + nomefile + ".csv";

        StringBuilder newStr = new StringBuilder();
        datalList.forEach(s -> {
            newStr.append(s + ";");
        });

        String outString = newStr.toString().substring(0, newStr.length() - 1);

        try {
            Files.writeString(Path.of(csvFile), outString, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Can't write file to path -> " + csvFile);
            throw new RuntimeException("AOO non riesco a scrivere DC");
        }
    }
}

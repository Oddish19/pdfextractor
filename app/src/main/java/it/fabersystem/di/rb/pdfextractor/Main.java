package it.fabersystem.di.rb.pdfextractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;

import java.io.IOException;

import org.apache.commons.cli.*;


public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

 
    public static void main(String[] args){

        
        CommandLineParser parser = new DefaultParser();
        Options options = setCliOptions();

        try {
        
            CommandLine line = parser.parse(options, args);

            if (line.hasOption('l'))
                setupLogger(line.getOptionValue('l'), line.hasOption('v'));
            logger.info("*********** PDF BOX - STARTED ***********");
            /* 
            // Controllo dei campi obbligatori
            if (!line.hasOption("i")) {
                logger.error("Mandatory field -i is missing...");
                throw new ParseException("Missing mandatory fields");
            }
            */


            // gli passo il file pdf per prendere da esso i dati
            String filepdf = "C:\\Users\\marco.oddi\\Desktop\\pdfbox\\app\\src\\test\\resources\\RBIT04_VATREP_202202_2023_05_08_14_20_12.pdf";
            String datiestratti = Process.elabpdfbox(filepdf);
            logger.info("il file contiene: "+datiestratti);


            // gli passo il file csv
            String filecsv = "C:\\Users\\marco.oddi\\Desktop\\pdfbox\\app\\src\\test\\resources\\RBIT04_VATREP_202202_2023_05_08_14_20_12.csv";
            try {
                Process.recuperadati(filecsv,datiestratti);
            } catch (IOException e) {
                logger.error("Non è stato possibile accedere al file .csv ", e);
            }


        } catch (ParseException e) {
            logger.error("Invalid arguments");
            new HelpFormatter().printHelp("otf-to-pdf", options);
            throw new RuntimeException("impossible to continue elaboration");
        }
    }

    public static void setupLogger(String logPath, boolean isVerbose) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("[%d{yyyy-MM-dd HH:mm:ss.SSS}] [%thread] %-5level %logger{35} - %msg%n");
        encoder.start();


        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        fileAppender.setContext(loggerContext);
        fileAppender.setFile(logPath);
        fileAppender.setEncoder(encoder);
        fileAppender.start();

        ch.qos.logback.classic.Logger iLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        iLogger.addAppender(fileAppender);
        iLogger.setLevel(isVerbose ? Level.DEBUG : Level.INFO);
    }

    
    public static Options setCliOptions() {
        Options options = new Options();

        options.addOption("i", "input-file", true, "Input pdf file");
        options.addOption("l", "log-path", true, "Full Path for storing log (filename included)");
        options.addOption("v", "verbose", false, "Enable verbose logging (add debug info to the logs)");

        return options;
    }
}
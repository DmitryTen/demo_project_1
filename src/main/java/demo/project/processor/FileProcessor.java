package demo.project.processor;

import demo.project.counter.IWordCounterHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.ConcurrentLinkedDeque;

public class FileProcessor implements Runnable{
    private static final Logger log = LoggerFactory.getLogger( FileProcessor.class );

    private final ConcurrentLinkedDeque<File> files;
    private final IResultsCollector collector;
    private final IWordCounterHandler handler;

    public FileProcessor(ConcurrentLinkedDeque<File> files, IResultsCollector collector, IWordCounterHandler handler) {
        this.files = files;
        this.collector = collector;
        this.handler = handler;
    }

    @Override
    public void run() {
        File fileToBeProcessed;
        while ((fileToBeProcessed = files.poll()) != null) {
            if (fileToBeProcessed.isDirectory()) {
                log.info("Attention, {} is a directory, bypassing", fileToBeProcessed.getAbsolutePath());
                return;
            }

            log.info("Starting to process file: {}", fileToBeProcessed.getAbsolutePath());
            try (BufferedReader buf = new BufferedReader(new FileReader(fileToBeProcessed))) {
                collector.collect(handler.countWords(buf), fileToBeProcessed.getAbsolutePath());
            } catch (Exception e) {
                log.warn("Exception", e);
            }
        }
    }
}

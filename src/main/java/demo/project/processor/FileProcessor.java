package demo.project.processor;

import demo.project.counter.IWordCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * "P.S: Ожидается реализация на Thread, для анализа слов использовать Regex."
 *
 * Решение основано на тредах, как указано в ТЗ, хотя с использованием Executors и CompletableFuture
 * код был бы чище и проще.
 * */
public class FileProcessor2 implements Runnable{
    private static final Logger log = LoggerFactory.getLogger( FileProcessor2.class );

    private final Thread workingThread;

    public FileProcessor2(int threadId,
                          ConcurrentLinkedDeque<File> files,
                          IWordCounter wordCounter,
                          IResultsCollector collector) {
        this.workingThread = new Thread(() -> {
            File fileToBeProcessed;
            while ((fileToBeProcessed = files.poll()) != null) {
                if (fileToBeProcessed.isDirectory()) {
                    log.info("Attention, {} is a directory, bypassing", fileToBeProcessed.getAbsolutePath());
                    return;
                }

                log.info("Starting to process file: {}", fileToBeProcessed.getAbsolutePath());
                try (BufferedReader buf = new BufferedReader(new FileReader(fileToBeProcessed))) {
                    collector.collect(wordCounter.countWords(buf), fileToBeProcessed.getAbsolutePath());
                } catch (Exception e) {
                    log.warn("Exception", e);
                }
            }
        }, String.format("process-%s", threadId));
    }


    @Override
    public void run() {
        
    }

    public void start() {
        workingThread.start();
    }

    public boolean isAlive() {
        return workingThread.isAlive();
    }
}

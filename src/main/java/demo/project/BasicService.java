package demo.project;

import demo.project.counter.IWordCounter;
import demo.project.processor.FileProcessor;
import demo.project.result.ResultsCollector;
import demo.project.result.SumService;
import java.io.File;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class BasicService {
    private final FileProcessor[] processors;
    private final ResultsCollector onResultsSubscriber = new ResultsCollector();
    private final SumService sumService = new SumService(this, onResultsSubscriber);

    public BasicService(File directory, int concurrencyLevel, IWordCounter wordCounter){
        processors = new FileProcessor[concurrencyLevel];
        ConcurrentLinkedDeque<File> files = new ConcurrentLinkedDeque<>(Arrays.asList(directory.listFiles()));

        for (int i=0; i < concurrencyLevel; i++) {
            processors[i] = new FileProcessor(i, files, wordCounter, onResultsSubscriber);
        }
    }

    public void startService(){
        Arrays.stream(processors).forEach(p -> p.start());
    }

    public boolean isThereAnyProcessAlive(){
        return Arrays.stream(processors).filter(p -> p.isAlive()).findAny().isPresent();
    }

    public TreeMap<Integer, String> waitAndGetResults(int cnt) {
        return sumService.waitAndGetResults(cnt);
    }
}

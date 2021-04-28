package demo.project;

import demo.project.counter.IWordCounterHandler;
import demo.project.processor.FileProcessor;
import demo.project.result.ResultsCollector;
import demo.project.result.SumService;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class BasicService {
    private final Thread[] threads;
    private final ResultsCollector resultsCollector = new ResultsCollector();
    private final SumService sumService = new SumService(this, resultsCollector);

    public BasicService(Charset charset, File directory, int concurrencyLevel, IWordCounterHandler wordCounter){
        threads = new Thread[concurrencyLevel];
        ConcurrentLinkedDeque<File> files = new ConcurrentLinkedDeque<>(Arrays.asList(directory.listFiles()));

        /**
         * "P.S: Ожидается реализация на Thread, для анализа слов использовать Regex."
         *
         * Решение основано на тредах, как указано в ТЗ, хотя с использованием Executors и CompletableFuture
         * код был бы чище и проще.
         * */
        for (int i=0; i < concurrencyLevel; i++) {
            threads[i] = new Thread(new FileProcessor(charset, files, resultsCollector, wordCounter), String.format("process-%s", i));
        }
    }

    public void startService(){
        Arrays.stream(threads).forEach(t -> t.start());
    }

    public boolean isThereAnyProcessAlive(){
        return Arrays.stream(threads).filter(t -> t.isAlive()).findAny().isPresent();
    }

    public List<String> waitAndGetResults(int cnt) {
        return sumService.waitAndGetResults(cnt);
    }
}

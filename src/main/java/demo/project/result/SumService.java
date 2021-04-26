package demo.project.result;

import demo.project.BasicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;



/**
 * Сервис получающий подсчитанные FileProcessor-ами слова
 * */
public class SumService {
    private static final Logger log = LoggerFactory.getLogger( SumService.class );

    private final BasicService basicService;
    private final ResultsCollector collector;
    private final ConcurrentHashMap<String, AtomicInteger> TOTAL_COLLECTION = new ConcurrentHashMap<>();

    public SumService(BasicService basicService, ResultsCollector collector) {
        this.collector = collector;
        this.basicService = basicService;
    }

    public TreeMap<Integer, String> waitAndGetResults(int cnt)  {
        summarizeResults();

        TreeMap<Integer, String> treeMap = new TreeMap<>(Collections.reverseOrder());
        TOTAL_COLLECTION.entrySet().forEach(entry -> {
            treeMap.put(entry.getValue().get(), entry.getKey());

            if (treeMap.size() > cnt) {
                treeMap.remove(treeMap.lastKey());
            }
        });

        return treeMap;
    }


    private void summarizeResults(){
        try {
            /**
             * Пока не все файлы обработаны, работаем.
             * */
            while (basicService.isThereAnyProcessAlive() || collector.getSize() > 0) {
                /**
                 * Если пока нечего обрабатывать - ждем
                 * */
                ResultsCollector.ResultsContainer container = collector.poll();
                if (container == null) {
                    log.debug("sleep");
                    Thread.sleep(100);
                } else {
                    Map<String, AtomicInteger> words = container.results;
                    words.entrySet().forEach(entry -> {
                        AtomicInteger existingResult = TOTAL_COLLECTION.get(entry.getKey());
                        if (existingResult == null) {
                            TOTAL_COLLECTION.put(entry.getKey(), entry.getValue());
                        } else {
                            existingResult.addAndGet(entry.getValue().get());
                        }
                    });
                    log.info("just added '{}' cnt: {}. Total size: {}", container.sourceName, words.size(), TOTAL_COLLECTION.size());
                }
            }
        } catch (Exception e) {
            log.warn("Exception", e);
        }
    }
}

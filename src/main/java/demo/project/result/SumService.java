package demo.project.result;

import demo.project.BasicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;



/**
 * Сервис получающий подсчитанные FileProcessor-ами слова
 * */
public class SumService {
    private static final Logger log = LoggerFactory.getLogger( SumService.class );

    private final BasicService basicService;
    private final ResultsCollector collector;
    private final Map<String, AtomicInteger> TOTAL_COLLECTION = new LinkedHashMap<>();

    public SumService(BasicService basicService, ResultsCollector collector) {
        this.collector = collector;
        this.basicService = basicService;
    }

    public List<String> waitAndGetResults(int maxCount)  {
        summarizeResults();

        AtomicInteger counter = new AtomicInteger();
        TreeMap<Integer, LinkedList<String>> treeMap = new TreeMap<>(Collections.reverseOrder());
        TOTAL_COLLECTION.entrySet().forEach(entry -> {
            LinkedList<String> bucket = treeMap.computeIfAbsent(entry.getValue().get(), (value) -> new LinkedList<String>());
            bucket.add(entry.getKey());
            int size = counter.incrementAndGet();

            if (size > maxCount) {
                LinkedList<String> lastBucket = treeMap.get(treeMap.lastKey());
                lastBucket.removeLast();
                counter.decrementAndGet();
                if (lastBucket.size() == 0) {
                    treeMap.remove(treeMap.lastKey());
                }
            }
        });

        return evalToList(treeMap);
    }


    private List<String> evalToList(TreeMap<Integer, LinkedList<String>> treeMap) {
        List<String> list = new ArrayList<>();
        treeMap.forEach((k, v) -> {
            v.forEach( word -> {
               log.info("word {}, cnt: {}", word, k);
               list.add(word);
            });
        });
        return list;
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

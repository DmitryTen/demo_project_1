package demo.project.processor;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Интерфейс для получения результатов подсчета слов
 * */
public interface IResultsCollector {
    void collect(Map<String, AtomicInteger> results, String sourceName);



}

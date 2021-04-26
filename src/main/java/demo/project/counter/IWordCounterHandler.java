package demo.project.counter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Интерфейс для парсинга текста и подсчета количества слов.
 * */
public interface IWordCounter {

    Map<String, AtomicInteger> countWords(BufferedReader textSource) throws IOException;
}

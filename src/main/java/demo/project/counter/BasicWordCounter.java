package demo.project.counter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class BasicWordCounter implements IWordCounter {
    private static final Logger log = LoggerFactory.getLogger( BasicWordCounter.class );


    final int maxLength;
    static final Pattern PUNCTUATION = Pattern.compile("\\p{Punct}");
    static final Pattern AVOID_WORDS_WITH_DIGITS = Pattern.compile("\\S*\\d+\\S*");

    public BasicWordCounter(int maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * Тут конечно большой вопрос что считать словом
     * Считаем ли мы словом 'BasicWordCounter2' или 'Basic2WordCounter' ?
     * Слова через дефис? "супер-пупер" - одно слово или два?
     *
     * В общем в данном случае, т.к. ТЗ явно не прописано, то работает так:
     * - Убираем все знаки пунктуации
     * - делим слова по пробелам
     * - слова с цифрами за слова не считаются.
     * */
    @Override
    public Map<String, AtomicInteger> countWords(BufferedReader textSource) throws IOException {
        if (textSource == null) {
            return Collections.emptyMap();
        }

        Map<String, AtomicInteger> words = new HashMap<>();
        String line;
        while ((line = textSource.readLine()) != null) {
            Arrays.stream(PUNCTUATION.matcher(line).replaceAll("").split(" "))
                    .forEach(word -> {
                        if (word.length() > maxLength && !AVOID_WORDS_WITH_DIGITS.matcher(word).matches()) {
                            AtomicInteger cnt = words.computeIfAbsent(word.toLowerCase(), s -> new AtomicInteger(0));
                            cnt.incrementAndGet();
                        }
                    });
        }

        log.debug("words encountered: {}", words.size());
        return words;
    }
}

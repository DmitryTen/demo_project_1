package demo.project;

import demo.project.counter.BasicWordCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.TreeMap;


/**
 * Реализовать приложение, которое будет выдавать список из 10-ти самых часто используемых слов, длина которых превышает
 * заданную. Слова должны подсчитываться в множестве текстовых файлов в указанной папке. Приложение должно производить
 * подсчёт в многопоточном режиме.
 * P.S: Ожидается реализация на Thread, для анализа слов использовать Regex.
 *
 * При выполнении задачи не забывать о принципах SOLID.
 * При оценки выполненного задания будет учитываться эффективность работы приложения
 * */
public class Main {
    private static final Logger log = LoggerFactory.getLogger( Main.class );

    /**
     * "список из 10-ти самых часто используемых слов"
     * */
    private static final int WORDS_CNT = 10;

    public static void main(String[] args) throws InterruptedException {
        try {
            String path = args[0];
            File file = new File(path);

            if (!file.exists()) {
                System.out.println(String.format("file '%s' doesn't exists", file.getAbsolutePath()));
                return;
            }
            if (!file.isDirectory()) {
                System.out.println(String.format("file '%s' is not a directory", file.getAbsolutePath()));
                return;
            }

            int wordLength = Integer.parseInt(args[1]);
            int concurrencyLevel;
            if (args.length > 2) {
                concurrencyLevel = Integer.parseInt(args[2]);
            } else {
                concurrencyLevel = 20;
            }

            log.info("Starting. FilePath: '{}', word length: {}, concurrencyLevel: {}",
                    file.getAbsolutePath(), wordLength, concurrencyLevel);

            calculate(file, wordLength, concurrencyLevel);
        } catch (Exception e) {
            log.info("Exception", e);
        }
    }


    /**
     * Описание решения:
     *
     * Каждый файл обрабатывается в отдельном потоке, обработка происходит внутри FileProcessor,
     * внутрь FileProcessr передается список файлов для обработки, после завершения обработки одного файла тред сразу
     * берет другой. Обработка прекращается только тогда когда в списке не остается файлов.
     *
     * Результаты обработки передаются в IResultsCollector, откуда они забираются основным тредом для суммирования
     * результатов.
     *
     *
     * */
    public static void calculate(File directory, int wordLength, int concurrencyLevel) throws InterruptedException {
        if (directory.listFiles() == null) {
            System.out.println(String.format("files not found inside '%s' directory", directory.getAbsolutePath()));
            return;
        }

        BasicService basicService = new BasicService(directory, concurrencyLevel, new BasicWordCounter(wordLength));
        basicService.startService();

        TreeMap<Integer, String> results = basicService.waitAndGetResults(WORDS_CNT);
        results.forEach((k,v) -> {
            log.info(String.format("Word: '%s', cnt: %s", v, k));
            System.out.println(v);
        });
    }
}

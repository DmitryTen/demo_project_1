package demo.project;

import demo.project.counter.BasicWordCounterHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;


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
            int concurrencyLevel = 20;
            Charset charset = null;
            if (args.length > 2) {
                String[] additionalArgs = new String[args.length - 2];
                System.arraycopy(args, 2, additionalArgs, 0, additionalArgs.length);

                for (int i = 0; i < additionalArgs.length; i++) {
                    String arg = additionalArgs[i];
                    if (arg.startsWith("-charset=")) {
                        charset = Charset.forName(arg.split("=")[1]);
                    } else if (arg.startsWith("-concurrency=")) {
                        concurrencyLevel = Integer.parseInt(arg.split("=")[1]);
                    }
                }
            }

            if (charset == null) {
                charset = Charset.defaultCharset();
            }

            log.info("Starting to process files.\nFilePath: '{}',\nword length: {},\nconcurrencyLevel: {}\nCharset: {}",
                    file.getAbsolutePath(), wordLength, concurrencyLevel, charset.toString());

            calculate(charset, file, wordLength, concurrencyLevel);
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
    public static void calculate(Charset charset, File directory, int wordLength, int concurrencyLevel) throws InterruptedException {
        if (directory.listFiles() == null) {
            System.out.println(String.format("files not found inside '%s' directory", directory.getAbsolutePath()));
            return;
        }

        BasicService basicService = new BasicService(charset, directory, concurrencyLevel, new BasicWordCounterHandler(wordLength));
        basicService.startService();

        List<String> results = basicService.waitAndGetResults(WORDS_CNT);
        results.forEach( word -> {
            System.out.println(word);
        });
    }
}

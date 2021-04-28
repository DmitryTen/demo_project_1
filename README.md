Тестовое задание 
1) Реализовать приложение, которое будет выдавать список из 10-ти самых часто используемых слов, длина которых превышает заданную. Слова должны подсчитываться в множестве текстовых файлов в указанной папке. Приложение должно производить подсчёт в многопоточном режиме.
P.S: Ожидается реализация на Thread, для анализа слов использовать Regex.

При выполнении задачи не забывать о принципах SOLID.
При оценки выполненного задания будет учитываться эффективность работы приложения

Для сборки приложения:

    mvn clean package
    
   
Приложение запускается и отрабатывает из коммандной строки следующим образом:

    java -jar target/demo_word_count-1.0-SNAPSHOT-one-jar-build.jar test_dir/0 5
    
    где test_dir/ - путь к файлу с текстовыми папками
    5 - минимальная длина слова
    
Дополнительные настройки:
По дефолту приложение работает в кодировке системы (обычно это UTF-8), если необходимо прописать 
иную:
    
    java -jar target/demo_word_count-1.0-SNAPSHOT-one-jar-build.jar test_dir/1 5 -charset=Windows-1251
    
По дефолту приложение работает в максимум 20 потоков, если нужно прописать иное то:
    
    java -jar target/demo_word_count-1.0-SNAPSHOT-one-jar-build.jar test_dir/0 5 -concurrency=50
    
можно комбинировать 
    
    java -jar target/demo_word_count-1.0-SNAPSHOT-one-jar-build.jar test_dir/1 5 -concurrency=50 -charset=Windows-1251
    
Программа генерирует лог-файл logfile.log 

Результаты работы выводятся в консоль, реализовано на Thread и Regex, из библиотек использовались только Slf4j и Log4j.

 
Приложение работает следующим образом:

Обработка файлов происходит в FileProcessor-ах, в отдельных потоках
FileProcessor принимает в себя IWordCounterHandler для подсчета слов из текста
также FileProcessor принимает в себя IResultsCollector для получения результатов подсчета слов.
 
Суммирование результатов происходит в SumService в основном потоке [main]
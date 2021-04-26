Тестовое задание 
1) Реализовать приложение, которое будет выдавать список из 10-ти самых часто используемых слов, длина которых превышает заданную. Слова должны подсчитываться в множестве текстовых файлов в указанной папке. Приложение должно производить подсчёт в многопоточном режиме.
P.S: Ожидается реализация на Thread, для анализа слов использовать Regex.


Для сборки приложения:

    mvn clean package
    
 
   
Приложение запускается и отрабатывает из коммандной строки следующим образом:

    java -jar target/demo_word_count-1.0-SNAPSHOT-one-jar-build.jar test_dir/ 5
    
    где test_dir/ - путь к файлу с текстовыми папками
    5 - минимальная длина слова
    
По дефолту приложение работает в 20 потоков (на каждый файл по потоку, если файлов меньше то и потоков будет меньше), но если нужно отрегулировать поточность нужно добавить аргумент

    java -jar target/demo_word_count-1.0-SNAPSHOT-one-jar-build.jar test_dir/ 5 10
    10 - количество потоков
    
Программа генерирует лог-файл logfile.log 

Результаты работы выводятся в консоль, реализовано на Thread и Regex, из библиотек использовались только Slf4j и Log4j. 
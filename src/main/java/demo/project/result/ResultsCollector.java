package demo.project.result;

import demo.project.processor.IResultsCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ResultsCollector implements IResultsCollector {
    private static final Logger log = LoggerFactory.getLogger( ResultsCollector.class );

    private final ConcurrentLinkedQueue<ResultsContainer> RESULTS_FROM_PROCESSORS = new ConcurrentLinkedQueue<>();

    static class ResultsContainer {
        final Map<String, AtomicInteger> results;
        final String sourceName;

        public ResultsContainer(Map<String, AtomicInteger> results, String sourceName) {
            this.results = results;
            this.sourceName = sourceName;
        }
    }

    @Override
    public void collect(Map<String, AtomicInteger> results, String sourceName) {
        if (results != null && results.size() > 0) {
            RESULTS_FROM_PROCESSORS.add(new ResultsContainer(results, sourceName));
            log.info("placing results of '{}' into collector, words_cnt: {}, results_waiting: {}",
                    sourceName, results.size(), RESULTS_FROM_PROCESSORS.size());
        }
    }

    int getSize() {
        return RESULTS_FROM_PROCESSORS.size();
    }

    ResultsContainer poll() {
        return RESULTS_FROM_PROCESSORS.poll();
    }


}

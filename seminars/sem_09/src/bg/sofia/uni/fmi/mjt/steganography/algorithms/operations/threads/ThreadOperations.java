package bg.sofia.uni.fmi.mjt.steganography.algorithms.operations.threads;

import java.nio.file.Path;
import java.util.List;

public interface ThreadOperations {

    List<Thread> startConsumers(String outputDirectory);

    Thread startCurrentConsumer(String outputDirectory);

    List<Thread> startProducers(List<Path>... files);

    Thread startCurrentProducer(Path... sourceFile);

    default void waitThreads(List<Thread> threads) throws InterruptedException {
        for (Thread thread : threads) {
            thread.join();
        }
    }

    int CONSUMERS_COUNT = Runtime.getRuntime().availableProcessors();

}

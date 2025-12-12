package bg.sofia.uni.fmi.mjt.steganography.algorithms.blocking;

import java.util.LinkedList;
import java.util.Queue;

public class TaskQueue<T> {

    public synchronized void put(T task) {
        blockingQueue.offer(task); // does not throw exception
        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        while (blockingQueue.isEmpty() && !noMoreTasks) {
            wait(); // wait for tasks to occur
        }

        if (blockingQueue.isEmpty() && noMoreTasks) {
            return null; // no more tasks
        }

        return blockingQueue.poll(); // does not throw exception
    }

    public synchronized void signalNoMoreTasks() {
        noMoreTasks = true;
        notifyAll(); // awake all Consumer threads to check in needing to stop
    }

    private final Queue<T> blockingQueue = new LinkedList<>(); // tasks in the queue waiting
    private boolean noMoreTasks = false; // producers are done

}

import java.util.Collection;
import java.util.Queue;

/**
 * <a href="https://en.wikipedia.org/wiki/Circular_buffer">Circular queue</a> implementation.
 * <p>The queue uses an array with a fixed size and loops around it.</p>
 * Alias for Circular Queue
 *
 * @param <E> the type of elements stored in the queue.
 *
 * @author  lel
 * @see     Collection
 * @see     Queue
 * @see     CircularQueue
 */
@Alias(CircularQueue.class)
public class CircularBuffer<E> extends CircularQueue<E> {
    /**
     * Create a circular queue with the default capacity.
     */
    public CircularBuffer() {
        super(DEFAULT_CAPACITY);
    }

    /**
     * Create a circular queue with the specified capacity
     *
     * @param capacity capacity
     */
    public CircularBuffer(int capacity) {
        super(capacity);
    }

    /**
     * Create a circular queue from an array
     *
     * @param elements array to use
     */
    public CircularBuffer(Object[] elements) {
        super(elements);
    }
}
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
public class CyclicBuffer<E> extends CircularQueue<E> {}
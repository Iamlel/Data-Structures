import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;

/**
 * <a href="https://en.wikipedia.org/wiki/Circular_buffer">Circular queue</a> implementation.
 * <p>The queue uses an array with a fixed size and loops around it.</p>
 *
 * @param <E> the type of elements stored in the queue.
 *
 * @author  lel
 * @see     Collection
 * @see     Queue
 */
public class CircularQueue<E> extends AbstractQueue<E> {
    protected static final int DEFAULT_CAPACITY = 10;

    private final Object[] elements;

    private int head;
    private int tail;
    private boolean full;

    /**
     * Create a circular queue with the default capacity.
     */
    public CircularQueue() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Create a circular queue with the specified capacity
     *
     * @param capacity capacity
     */
    public CircularQueue(int capacity) {
        this.elements = new Object[capacity];
        this.head = 0;
        this.tail = 0;
        this.full = false;
    }

    /**
     * Create a circular queue from an array
     *
     * @param elements array to use
     */
    public CircularQueue(Object[] elements) {
        this.elements = Arrays.copyOf(elements, elements.length);
        this.head = 0;
        this.tail = 0;
        this.full = true;
    }

    /**
     * Returns the number of elements.
     *
     * @return the number of elements
     */
    @Override
    public int size() {
        if (full) {
            return this.capacity();
        }

        final int size = head - tail + 1;
        return (size < 0) ? this.capacity() - size : size;
    }

    /**
     * Returns the maximum number of elements.
     *
     * @return the maximum number of elements
     */
    public int capacity() {
        return elements.length;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return (!full && head - tail == 0);
    }

    /**
     * Returns {@code true} if this collection is full.
     *
     * @return {@code true} if this collection is full
     */
    public boolean isFull() {
        return full;
    }

    /**
     * Returns {@code true} if this collection contains the specified element.
     * More formally, returns {@code true} if and only if this collection
     * contains at least one element {@code e} such that
     * {@code Objects.equals(o, e)}.
     *
     * @param o element whose presence in this collection is to be tested
     * @return {@code true} if this collection contains the specified
     * element
     */
    @Override
    public boolean contains(Object o) {
        for (Object e : this) {
            if (e.equals(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns an iterator over the elements in this collection.
     * The iterator will iterate in the same order that calling
     * {@link CircularQueue#dequeue} multiple times would.
     *
     * @return an {@code Iterator} over the elements in this collection
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int ptr = 0;

            @Override
            public boolean hasNext() {
                return (ptr < size());
            }

            @Override
            public E next() {
                if (hasNext()) {
                    E e = peek(ptr);
                    ptr++;
                    return e;
                }
                throw new NoSuchElementException();
            }

            @Override
            @Deprecated
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void forEachRemaining(Consumer<? super E> action) {
                Iterator.super.forEachRemaining(action);
            }
        };
    }

    /**
     * Returns an array containing all of the elements in this collection.
     * The order will be the same order that calling {@link CircularQueue#dequeue}
     * multiple times would result in. The returned array's
     * {@linkplain Class#getComponentType runtime component type} is {@code Object}.
     *
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this collection. The caller is thus free to modify the
     * returned array.
     *
     * @return an array, whose {@linkplain Class#getComponentType runtime component
     * type} is {@code Object}, containing all of the elements in this collection
     * @apiNote This method acts as a bridge between array-based and collection-based APIs.
     * It returns an array whose runtime type is {@code Object[]}.
     * Use {@link #toArray(Object[]) toArray(T[])} to reuse an existing
     * array, or use {@link #toArray(IntFunction)} to control the runtime type
     * of the array.
     */
    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size()];

        int i = 0;
        for (Object e : this) {
            arr[i] = e;
        }

        return arr;
    }

    /**
     * Returns an array containing all of the elements in this collection;
     * the runtime type of the returned array is that of the specified array.
     * If the collection fits in the specified array, it is returned therein.
     * Otherwise, a new array is allocated with the runtime type of the
     * specified array and the size of this collection.
     *
     * <p>If this collection fits in the specified array with room to spare
     * (i.e., the array has more elements than this collection), the element
     * in the array immediately following the end of the collection is set to
     * {@code null}.  (This is useful in determining the length of this
     * collection <i>only</i> if the caller knows that this collection does
     * not contain any {@code null} elements.)
     *
     * <p>The order will be the same order that calling {@link CircularQueue#dequeue}
     * multiple times would result in.
     *
     * @param a the array into which the elements of this collection are to be
     *          stored, if it is big enough; otherwise, a new array of the same
     *          runtime type is allocated for this purpose.
     * @return an array containing all of the elements in this collection
     * @throws ArrayStoreException  if the runtime type of any element in this
     * collection is not assignable to the {@linkplain Class#getComponentType
     * runtime component type} of the specified array
     * @throws NullPointerException if the specified array is null
     * @apiNote This method acts as a bridge between array-based and collection-based APIs.
     * It allows an existing array to be reused under certain circumstances.
     * Use {@link #toArray()} to create an array whose runtime type is {@code Object[]},
     * or use {@link #toArray(IntFunction)} to control the runtime type of
     * the array.
     *
     * <p>Suppose {@code x} is a collection known to contain only strings.
     * The following code can be used to dump the collection into a previously
     * allocated {@code String} array:
     *
     * <pre>
     *     String[] y = new String[SIZE];
     *     ...
     *     y = x.toArray(y);</pre>
     *
     * <p>The return value is reassigned to the variable {@code y}, because a
     * new array will be allocated and returned if the collection {@code x} has
     * too many elements to fit into the existing array {@code y}.
     *
     * <p>Note that {@code toArray(new Object[0])} is identical in function to
     * {@code toArray()}.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        Object[] arr = this.toArray();
        if (a.length < size()) {
            return (T[]) Arrays.copyOf(arr, size(), a.getClass());
        }
        System.arraycopy((T[]) arr, 0, a, 0, size());
        if (a.length > size()) {
            a[size()] = null;
        }
        return a;
    }

    /**
     * Returns an array containing all of the elements in this collection,
     * using the provided {@code generator} function to allocate the returned array.
     *
     * <p>If this collection makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the elements in
     * the same order.
     *
     * @param generator a function which produces a new array of the desired
     *                  type and the provided length
     * @return an array containing all of the elements in this collection
     * @throws ArrayStoreException  if the runtime type of any element in this
     *                              collection is not assignable to the {@linkplain Class#getComponentType
     *                              runtime component type} of the generated array
     * @throws NullPointerException if the generator function is null
     * @apiNote This method acts as a bridge between array-based and collection-based APIs.
     * It allows creation of an array of a particular runtime type. Use
     * {@link #toArray()} to create an array whose runtime type is {@code Object[]},
     * or use {@link #toArray(Object[]) toArray(T[])} to reuse an existing array.
     *
     * <p>Suppose {@code x} is a collection known to contain only strings.
     * The following code can be used to dump the collection into a newly
     * allocated array of {@code String}:
     *
     * <pre>
     *     String[] y = x.toArray(String[]::new);</pre>
     * @implSpec The default implementation calls the generator function with zero
     * and then passes the resulting array to {@link #toArray(Object[]) toArray(T[])}.
     * @since 11
     */
    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return super.toArray(generator);
    }

    @Override
    @Deprecated
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns {@code true} if this collection contains all of the elements
     * in the specified collection.
     *
     * @param c collection to be checked for containment in this collection
     * @return {@code true} if this collection contains all of the elements
     * in the specified collection
     * @see #contains(Object)
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds all the elements in the specified collection to this collection
     * The behavior of this operation is undefined if the specified collection
     * is modified while the operation is in progress. (This implies that the
     * behavior of this call is undefined if the specified collection is this
     * collection, and this collection is nonempty.) If the specified collection
     * has a defined <a href="SequencedCollection.html#encounter">encounter order</a>,
     * processing of its elements generally occurs in that order.
     *
     * @param c collection containing elements to be added to this collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws IllegalStateException         if not all the elements can be added at
     *                                       this time due to insertion restrictions
     * @see #add(Object)
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            add(e);
        }
        return true;
    }

    /**
     * Adds all the elements in the specified collection to this collection
     * The behavior of this operation is undefined if the specified collection
     * is modified while the operation is in progress. (This implies that the
     * behavior of this call is undefined if the specified collection is this
     * collection, and this collection is nonempty.) If the specified collection
     * has a defined <a href="SequencedCollection.html#encounter">encounter order</a>,
     * processing of its elements generally occurs in that order.
     *
     * @param c collection containing elements to be added to this collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws IllegalStateException         if not all the elements can be added at
     *                                       this time due to insertion restrictions
     * @see #add(Object)
     */
    @Alias(method = "addAll")
    public boolean queueAll(Collection<? extends E> c) {
        return addAll(c);
    }

    @Override
    @Deprecated
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public boolean removeIf(Predicate<? super E> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     *
     */
    @Override
    public void clear() {
        for (int ptr = 0; ptr < size(); ptr++) {
            elements[(tail + ptr) % capacity()] = null;
        }

        this.head = 0;
        this.tail = 0;
        this.full = false;
    }

    /**
     * Inserts the specified element into this queue if it is possible to do
     * so immediately without violating capacity restrictions.
     * When using a capacity-restricted queue, this method is generally
     * preferable to {@link #add}, which can fail to insert an element only
     * by throwing an exception.
     *
     * @param e the element to add
     * @return {@code true} if the element was added to this queue, else
     * {@code false}
     */
    @Override
    public boolean offer(E e) {
        if (full) {
            return false;
        }

        elements[head] = e;
        head = (head + 1) % elements.length;

        if (head - tail == 0) {
            full = true;
        }

        return true;
    }

    /**
     * Inserts the specified element into this queue if it is possible to do
     * so immediately without violating capacity restrictions.
     * When using a capacity-restricted queue, this method is generally
     * preferable to {@link #add}, which can fail to insert an element only
     * by throwing an exception.
     *
     * @param e the element to add
     * @return {@code true} if the element was added to this queue, else
     * {@code false}
     */
    @Alias(method = "offer")
    public boolean queue(E e) {
        return this.offer(e);
    }

    /**
     * Retrieves and removes the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    @Override
    public E poll() {
        if (this.isEmpty()) {
            return null;
        }

        E element = this.peek();

        elements[tail] = null;
        tail = (tail + 1) % elements.length;

        if (full) {
            full = false;
        }

        return element;
    }

    /**
     * Retrieves and removes the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    @Alias(method = "poll")
    public E dequeue() {
        return this.poll();
    }

    /**
     * Retrieves, but does not remove, the head + {@code ahead} of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head + {@code ahead} of this queue, or {@code null} if this queue is empty
     */
    @SuppressWarnings("unchecked")
    public E peek(int ahead) {
        return (E) elements[(tail + ahead) % capacity()];
    }

    /**
     * Retrieves, but does not remove, the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    @Override
    @SuppressWarnings("unchecked")
    public E peek() {
        return (E) elements[tail];
    }

    /**
     * Retrieves the head of this queue, and queues an element.
     * Returns {@code null} if the queue is empty.
     *
     * @param e Element to queue.
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    public E replace(E e) {
        E ret = this.dequeue();
        this.queue(e);
        return ret;
    }

    /**
     * Makes a new instance but with an array of specified size.
     *
     * @param capacity capacity
     * @return new instance of {@code CircularQueue}
     */
    public CircularQueue<E> resize(int capacity) {
        return new CircularQueue<>(Arrays.copyOf(this.toArray(), capacity));
    }

    /**
     * Makes a new instance but with an array that is {@code capacity} larger.
     *
     * @param capacity capacity to add
     * @return new instance of {@code CircularQueue}
     */
    public CircularQueue<E> addSize(int capacity) {
        return this.resize(capacity() + capacity);
    }

    /**
     * Create a circular queue from an array
     *
     * @param elements array to use
     */
    public static <E> CircularQueue<E> fromArray(Object[] elements) {
        return new CircularQueue<E>(elements);
    }
}

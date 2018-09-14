import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;

    private int maxSize;

    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        maxSize = 1;
        size = 0;
        queue = (Item[]) new Object[maxSize];
    }

    // unit testing (optional)
    public static void main(String[] args) {

        RandomizedQueue<Integer> queue = new RandomizedQueue<>();

        for (int i = 0; i < 10; i++) {
            queue.enqueue(i);
        }

        for (int value : queue) {
            for (int value1 : queue) {
                System.out.println(String.format("%d, %d", value, value1));
            }
        }

    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        }
        if (size == maxSize) {
            resize(maxSize * 2);
        }

        queue[size] = item;
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }
        int index = StdRandom.uniform(size);
        swap(index, --size);
        Item item = queue[size];
        queue[size] = null;
        if (maxSize > 3 && size == maxSize / 4) {
            resize(maxSize / 2);
        }
        return item;
    }

    private void resize(int newSize) {
        if (newSize < 1 || newSize < size) {
            throw new java.lang.IllegalArgumentException(
                    String.format("Can't resize queue with size %d from %d to %d", size, maxSize,
                                  newSize));
        }
        Item[] copyQueue = (Item[]) new Object[newSize];

        int j = 0;
        for (int i = 0; i < size; i++) {
            copyQueue[i] = queue[j++];
        }

        maxSize = newSize;
        queue = copyQueue;
    }

    private void swap(int i, int j) {
        Item item = queue[i];
        queue[i] = queue[j];
        queue[j] = item;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }
        int index = StdRandom.uniform(size);
        return queue[index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private final int[] indexArray;
        private int current;

        public RandomizedQueueIterator() {
            current = 0;
            indexArray = StdRandom.permutation(size);
        }

        @Override
        public boolean hasNext() {
            return current < indexArray.length;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            return queue[indexArray[current++]];
        }


        @Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }
}
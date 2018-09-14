
import java.util.Iterator;

// linked list implementation of deque
public class Deque<Item> implements Iterable<Item> {

    private Node first = null;
    private Node last = null;
    private int size = 0;

    // construct an empty deque
    public Deque() {
    }

    // // unit testing (optional)
    // public static void main(String[] args) {
    // }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        }
        size++;
        Node next = last;
        last = new Node(item, last, null);
        if (next != null) {
            next.prev = last;
        }
        if (size == 1) {
            first = last;
        }
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (last == null) {
            throw new java.util.NoSuchElementException();
        }
        Item ret = last.getValue();
        last = last.getNext();

        if (last != null) {
            last.prev = null;
            if (last.next != null) {
                last.next.prev = last;
            }
        }
        size--;
        if (size == 0) {
            first = null;
        }
        return ret;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        }
        size++;
        Node prev = first;
        first = new Node(item, null, first);
        if (prev != null) {
            prev.next = first;
        }
        if (size == 1) {
            last = first;
        }
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (first == null || last == null);
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (first == null) {
            throw new java.util.NoSuchElementException();
        }
        Item ret = first.getValue();
        first = first.getPrev();
        if (first != null) {
            first.next = null;
            if (first.prev != null) {
                first.prev.next = first;
            }
        }
        size--;
        if (size == 0) {
            last = null;
        }
        return ret;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class Node {
        private Item value;
        private Node next;
        private Node prev;

        public Node(Item value, Node next, Node prev) {
            this.setValue(value);
            this.setNext(next);
            this.setPrev(prev);

        }

        Item getValue() {
            return value;
        }

        void setValue(Item value) {
            this.value = value;
        }

        Node getNext() {
            return next;
        }

        void setNext(Node next) {
            this.next = next;
        }

        Node getPrev() {
            return prev;
        }

        void setPrev(Node prev) {
            this.prev = prev;
        }
    }

    private class DequeIterator implements Iterator<Item> {

        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            Item value = current.getValue();
            current = current.prev;
            return value;
        }

        @Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }
}

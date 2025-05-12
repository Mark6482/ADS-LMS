package by.it.group310951.suponenko.lesson11;

import java.util.Collection;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;
    private int threshold;

    private Node<E> head; // for insertion order
    private Node<E> tail;

    private static class Node<E> {
        E key;
        Node<E> nextInBucket;
        Node<E> nextInOrder;
        Node<E> prevInOrder;

        Node(E key) {
            this.key = key;
        }
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    private int index(Object key) {
        return (key == null ? 0 : Math.abs(key.hashCode())) % table.length;
    }

    @Override
    public boolean add(Object o) {
        int idx = index(o);
        Node<E> node = table[idx];

        while (node != null) {
            if (o == null ? node.key == null : o.equals(node.key)) {
                return false;
            }
            node = node.nextInBucket;
        }

        Node<E> newNode = new Node<>((E) o);
        newNode.nextInBucket = table[idx];
        table[idx] = newNode;

        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.nextInOrder = newNode;
            newNode.prevInOrder = tail;
            tail = newNode;
        }

        size++;
        if (size >= threshold) {
            resize();
        }
        return true;
    }

    private void resize() {
        int newCapacity = table.length * 2;
        @SuppressWarnings("unchecked")
        Node<E>[] newTable = new Node[newCapacity];

        Node<E> current = head;
        while (current != null) {
            int idx = (current.key == null ? 0 : Math.abs(current.key.hashCode())) % newCapacity;
            Node<E> next = current.nextInOrder;

            current.nextInBucket = newTable[idx];
            newTable[idx] = current;

            current = next;
        }

        table = newTable;
        threshold = (int) (newCapacity * LOAD_FACTOR);
    }

    @Override
    public boolean contains(Object o) {
        int idx = index(o);
        Node<E> node = table[idx];
        while (node != null) {
            if (o == null ? node.key == null : o.equals(node.key)) {
                return true;
            }
            node = node.nextInBucket;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        int idx = index(o);
        Node<E> node = table[idx];
        Node<E> prev = null;

        while (node != null) {
            if (o == null ? node.key == null : o.equals(node.key)) {
                if (prev == null) {
                    table[idx] = node.nextInBucket;
                } else {
                    prev.nextInBucket = node.nextInBucket;
                }
                if (node.prevInOrder != null) {
                    node.prevInOrder.nextInOrder = node.nextInOrder;
                } else {
                    head = node.nextInOrder;
                }

                if (node.nextInOrder != null) {
                    node.nextInOrder.prevInOrder = node.prevInOrder;
                } else {
                    tail = node.prevInOrder;
                }

                size--;
                return true;
            }
            prev = node;
            node = node.nextInBucket;
        }

        return false;
    }

    @Override
    public void clear() {
        @SuppressWarnings("unchecked")
        Node<E>[] newTable = new Node[DEFAULT_CAPACITY];
        table = newTable;
        head = tail = null;
        size = 0;
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        boolean first = true;
        while (current != null) {
            if (!first) sb.append(", ");
            sb.append(current.key);
            first = false;
            current = current.nextInOrder;
        }
        sb.append("]");
        return sb.toString();
    }

    // ===== Методы уровня B =====

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object e : c) {
            if (remove(e)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.nextInOrder;
            if (!c.contains(current.key)) {
                remove(current.key);
                changed = true;
            }
            current = next;
        }
        return changed;
    }

    // === Необязательные методы ===
    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}

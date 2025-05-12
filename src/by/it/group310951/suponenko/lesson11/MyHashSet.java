package by.it.group310951.suponenko.lesson11;

import java.util.Set;

public class MyHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;
    private int threshold;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    private static class Node<E> {
        final E key;
        Node<E> next;

        Node(E key) {
            this.key = key;
        }
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
            node = node.next;
        }

        Node<E> newNode = new Node<>((E) o);
        newNode.next = table[idx];
        table[idx] = newNode;
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
        for (Node<E> node : table) {
            while (node != null) {
                Node<E> next = node.next;
                int newIndex = (node.key == null ? 0 : Math.abs(node.key.hashCode())) % newCapacity;

                node.next = newTable[newIndex];
                newTable[newIndex] = node;

                node = next;
            }
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
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        int idx = index(o);
        Node<E> current = table[idx];
        Node<E> prev = null;

        while (current != null) {
            if (o == null ? current.key == null : o.equals(current.key)) {
                if (prev == null) {
                    table[idx] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }

        return false;
    }

    @Override
    public void clear() {
        @SuppressWarnings("unchecked")
        Node<E>[] newTable = new Node[DEFAULT_CAPACITY];
        table = newTable;
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
        boolean first = true;

        for (Node<E> node : table) {
            while (node != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(node.key);
                first = false;
                node = node.next;
            }
        }

        sb.append("]");
        return sb.toString();
    }

    // === НЕОБЯЗАТЕЛЬНЫЕ методы интерфейса Set ===
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}

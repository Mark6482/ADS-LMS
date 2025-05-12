package by.it.group310951.suponenko.lesson10;

import java.util.*;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private E[] heap;
    private int size;

    public MyPriorityQueue() {
        heap = (E[]) new Comparable[DEFAULT_CAPACITY];
    }

    @Override
    public boolean add(E element) {
        if (element == null) throw new NullPointerException();
        ensureCapacity();
        heap[size] = element;
        siftUp(size++);
        return true;
    }

    @Override
    public boolean offer(E element) {
        return add(element);
    }

    @Override
    public E peek() {
        return size == 0 ? null : heap[0];
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return heap[0];
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E result = heap[0];
        heap[0] = heap[--size];
        heap[size] = null;
        siftDown(0);
        return result;
    }

    @Override
    public E remove() {
        if (size == 0) throw new NoSuchElementException();
        return poll();
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? heap[i] == null : o.equals(heap[i])) return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) heap[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (size >= heap.length) {
            E[] newHeap = (E[]) new Comparable[heap.length * 2];
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
        }
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (heap[parentIndex].compareTo(heap[index]) > 0) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }


    private void siftDown(int idx) {
        E element = heap[idx];
        int half = size / 2;
        while (idx < half) {
            int left = 2 * idx + 1;
            int right = left + 1;
            int smallest = left;
            if (right < size && heap[right].compareTo(heap[left]) < 0) smallest = right;
            if (heap[smallest].compareTo(element) >= 0) break;
            heap[idx] = heap[smallest];
            idx = smallest;
        }
        heap[idx] = element;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        List<E> retained = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                retained.add(heap[i]);
            } else {
                modified = true;
            }
        }

        if (modified) {
            size = retained.size();
            heap = (E[]) new Comparable[heap.length];
            for (int i = 0; i < size; i++) {
                heap[i] = retained.get(i);
            }
            for (int i = size / 2 - 1; i >= 0; i--) {
                siftDown(i);
            }
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        List<E> retained = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                retained.add(heap[i]);
            } else {
                modified = true;
            }
        }
        if(modified){
            size = retained.size();
            heap = (E[]) new Comparable[heap.length];
            for (int i = 0; i < size; i++) {
                heap[i] = retained.get(i);
            }
            for (int i = size / 2 - 1; i >= 0; i--) {
                siftDown(i);
            }
        }
        return modified;
    }

    private void swap(int i, int j) {
        E temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object o) { return false; }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
}

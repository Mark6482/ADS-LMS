package by.it.group310951.suponenko.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node {
        Integer key;
        String value;
        Node left, right;
        boolean color;

        Node(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private Node root;
    private int size;

    public MyRbMap() {
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inorderTraversal(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void inorderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inorderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inorderTraversal(node.right, sb);
        }
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        String oldValue = get(key);
        root = put(root, key, value);
        root.color = BLACK;
        return oldValue;
    }

    private Node put(Node node, Integer key, String value) {
        if (node == null) {
            size++;
            return new Node(key, value, RED);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = put(node.left, key, value);
        else if (cmp > 0) node.right = put(node.right, key, value);
        else node.value = value;

        if (isRed(node.right) && !isRed(node.left)) node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right)) flipColors(node);

        return node;
    }

    @Override
    public String remove(Object key) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;
        if (!containsKey(k)) return null;
        String oldValue = get(k);
        root = remove(root, k);
        if (root != null) root.color = BLACK;
        return oldValue;
    }

    private Node remove(Node node, Integer key) {
        if (key.compareTo(node.key) < 0) {
            if (!isRed(node.left) && !isRed(node.left.left)) node = moveRedLeft(node);
            node.left = remove(node.left, key);
        } else {
            if (isRed(node.left)) node = rotateRight(node);
            if (key.compareTo(node.key) == 0 && node.right == null) {
                size--;
                return null;
            }
            if (!isRed(node.right) && !isRed(node.right.left)) node = moveRedRight(node);
            if (key.compareTo(node.key) == 0) {
                Node min = min(node.right);
                node.key = min.key;
                node.value = min.value;
                node.right = deleteMin(node.right);
                size--;
            } else {
                node.right = remove(node.right, key);
            }
        }
        return balance(node);
    }

    @Override
    public String get(Object key) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;
        Node node = root;
        while (node != null) {
            int cmp = k.compareTo(node.key);
            if (cmp < 0) node = node.left;
            else if (cmp > 0) node = node.right;
            else return node.value;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        if (!(key instanceof Integer)) return false;
        return get((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value == null) throw new NullPointerException("Value cannot be null");
        return containsValue(root, value.toString());
    }

    private boolean containsValue(Node node, String value) {
        if (node == null) return false;
        if (value.equals(node.value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap headMap = new MyRbMap();
        headMapHelper(root, toKey, headMap);
        return headMap;
    }

    private void headMapHelper(Node node, Integer toKey, MyRbMap headMap) {
        if (node == null) return;
        if (node.key.compareTo(toKey) < 0) {
            headMap.put(node.key, node.value);
            headMapHelper(node.right, toKey, headMap);
        }
        headMapHelper(node.left, toKey, headMap);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap tailMap = new MyRbMap();
        tailMapHelper(root, fromKey, tailMap);
        return tailMap;
    }

    private void tailMapHelper(Node node, Integer fromKey, MyRbMap tailMap) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0) {
            tailMap.put(node.key, node.value);
            tailMapHelper(node.left, fromKey, tailMap);
            tailMapHelper(node.right, fromKey, tailMap);
        } else {
            tailMapHelper(node.right, fromKey, tailMap);
        }
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = root;
        while (node.left != null) node = node.left;
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = root;
        while (node.right != null) node = node.right;
        return node.key;
    }

    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Map.Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }

    private boolean isRed(Node node) {
        return node != null && node.color == RED;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private void flipColors(Node h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    private Node moveRedLeft(Node h) {
        flipColors(h);
        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    private Node moveRedRight(Node h) {
        flipColors(h);
        if (isRed(h.left.left)) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    private Node balance(Node h) {
        if (isRed(h.right)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);
        return h;
    }

    private Node min(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private Node deleteMin(Node node) {
        if (node.left == null) return null;
        if (!isRed(node.left) && !isRed(node.left.left)) node = moveRedLeft(node);
        node.left = deleteMin(node.left);
        return balance(node);
    }
}

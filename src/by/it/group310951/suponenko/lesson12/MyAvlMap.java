package by.it.group310951.suponenko.lesson12;

import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {
    private class Node {
        int key;
        String value;
        int height;
        Node left, right;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return get((Integer) key) != null;
    }

    @Override
    public String get(Object key) {
        Node node = find(root, (Integer) key);
        return node == null ? null : node.value;
    }

    private Node find(Node node, int key) {
        if (node == null) return null;
        if (key < node.key) return find(node.left, key);
        if (key > node.key) return find(node.right, key);
        return node;
    }

    @Override
    public String put(Integer key, String value) {
        String[] oldValue = new String[1];
        root = insert(root, key, value, oldValue);
        if (oldValue[0] == null) size++;
        return oldValue[0];
    }

    private Node insert(Node node, int key, String value, String[] oldValue) {
        if (node == null) return new Node(key, value);

        if (key < node.key) {
            node.left = insert(node.left, key, value, oldValue);
        } else if (key > node.key) {
            node.right = insert(node.right, key, value, oldValue);
        } else {
            oldValue[0] = node.value;
            node.value = value;
            return node;
        }

        updateHeight(node);
        return balance(node);
    }

    @Override
    public String remove(Object key) {
        String[] removedValue = new String[1];
        root = delete(root, (Integer) key, removedValue);
        if (removedValue[0] != null) size--;
        return removedValue[0];
    }

    private Node delete(Node node, int key, String[] removedValue) {
        if (node == null) return null;

        if (key < node.key) {
            node.left = delete(node.left, key, removedValue);
        } else if (key > node.key) {
            node.right = delete(node.right, key, removedValue);
        } else {
            removedValue[0] = node.value;

            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            Node minLargerNode = getMin(node.right);
            node.key = minLargerNode.key;
            node.value = minLargerNode.value;
            node.right = delete(node.right, minLargerNode.key, new String[1]);
        }

        updateHeight(node);
        return balance(node);
    }

    private Node getMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private void updateHeight(Node node) {
        int hl = node.left == null ? 0 : node.left.height;
        int hr = node.right == null ? 0 : node.right.height;
        node.height = 1 + Math.max(hl, hr);
    }

    private int getBalance(Node node) {
        int hl = node.left == null ? 0 : node.left.height;
        int hr = node.right == null ? 0 : node.right.height;
        return hl - hr;
    }

    private Node balance(Node node) {
        int balance = getBalance(node);

        if (balance > 1) {
            if (getBalance(node.left) < 0) node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balance < -1) {
            if (getBalance(node.right) > 0) node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T = x.right;
        x.right = y;
        y.left = T;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T = y.left;
        y.left = x;
        x.right = T;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderToString(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2); // remove last comma
        sb.append("}");
        return sb.toString();
    }

    private void inOrderToString(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrderToString(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        inOrderToString(node.right, sb);
    }

    // Необязательные для задания методы — заглушки:
    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}

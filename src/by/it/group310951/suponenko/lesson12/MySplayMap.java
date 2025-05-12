package by.it.group310951.suponenko.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {
    private class Node {
        Integer key;
        String value;
        Node left, right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    // Splay operation
    private Node splay(Node node, Integer key) {
        if (node == null || key.equals(node.key)) return node;

        if (key < node.key) {
            if (node.left == null) return node;
            if (key < node.left.key) {
                node.left.left = splay(node.left.left, key);
                node = rotateRight(node);
            } else if (key > node.left.key) {
                node.left.right = splay(node.left.right, key);
                if (node.left.right != null)
                    node.left = rotateLeft(node.left);
            }
            return (node.left == null) ? node : rotateRight(node);
        } else {
            if (node.right == null) return node;
            if (key > node.right.key) {
                node.right.right = splay(node.right.right, key);
                node = rotateLeft(node);
            } else if (key < node.right.key) {
                node.right.left = splay(node.right.left, key);
                if (node.right.left != null)
                    node.right = rotateRight(node.right);
            }
            return (node.right == null) ? node : rotateLeft(node);
        }
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        return x;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        return x;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }

        root = splay(root, key);

        if (key.equals(root.key)) {
            String old = root.value;
            root.value = value;
            return old;
        }

        Node newNode = new Node(key, value);
        if (key < root.key) {
            newNode.left = root.left;
            newNode.right = root;
            root.left = null;
        } else {
            newNode.right = root.right;
            newNode.left = root;
            root.right = null;
        }
        root = newNode;
        size++;
        return null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        root = splay(root, (Integer) key);
        return root != null && root.key.equals(key) ? root.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, String.valueOf(value));
    }

    private boolean containsValue(Node node, String value) {
        if (node == null) return false;
        if (value.equals(node.value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public String remove(Object keyObj) {
        Integer key = (Integer) keyObj;
        if (key == null || root == null) return null;

        root = splay(root, key);
        if (!key.equals(root.key)) return null;

        String removedValue = root.value;

        if (root.left == null) {
            root = root.right;
        } else {
            Node leftSubtree = root.left;

            leftSubtree = splay(leftSubtree, key);
            leftSubtree.right = root.right;
            root = leftSubtree;
        }

        size--;
        return removedValue;
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
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = root;
        while (node.left != null) node = node.left;
        root = splay(root, node.key);
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = root;
        while (node.right != null) node = node.right;
        root = splay(root, node.key);
        return node.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap map = new MySplayMap();
        collect(root, map, (k) -> k < toKey);
        return map;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap map = new MySplayMap();
        collect(root, map, (k) -> k >= fromKey);
        return map;
    }

    private void collect(Node node, MySplayMap target, java.util.function.Predicate<Integer> condition) {
        if (node != null) {
            collect(node.left, target, condition);
            if (condition.test(node.key)) target.put(node.key, node.value);
            collect(node.right, target, condition);
        }
    }

    @Override
    public Integer lowerKey(Integer key) {
        return findNear(root, key, (k) -> k < key, true);
    }

    @Override
    public Integer floorKey(Integer key) {
        return findNear(root, key, (k) -> k <= key, true);
    }

    @Override
    public Integer ceilingKey(Integer key) {
        return findNear(root, key, (k) -> k >= key, false);
    }

    @Override
    public Integer higherKey(Integer key) {
        return findNear(root, key, (k) -> k > key, false);
    }


    private Integer findNear(Node node, Integer key, java.util.function.Predicate<Integer> condition, boolean searchLeft) {
        Integer result = null;
        while (node != null) {
            if (condition.test(node.key)) {
                result = node.key;
                node = searchLeft ? node.right : node.left;
            } else {
                node = searchLeft ? node.left : node.right;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inorder(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void inorder(Node node, StringBuilder sb) {
        if (node != null) {
            inorder(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inorder(node.right, sb);
        }
    }

    @Override public Comparator<? super Integer> comparator() { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> lowerEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> floorEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> ceilingEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> higherEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> firstEntry() { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> lastEntry() { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> pollFirstEntry() { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> pollLastEntry() { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> descendingMap() { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> navigableKeySet() { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> descendingKeySet() { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { throw new UnsupportedOperationException(); }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
}

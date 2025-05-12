package by.it.group310951.suponenko.lesson13;

import java.util.*;

public class GraphC {
    private static Map<String, List<String>> graph = new HashMap<>();
    private static Map<String, Integer> indexMap = new HashMap<>();
    private static Map<String, Integer> lowLinkMap = new HashMap<>();
    private static Stack<String> stack = new Stack<>();
    private static Set<String> onStack = new HashSet<>();
    private static int index = 0;
    private static List<List<String>> components = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0];
            String to = parts[1];

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            graph.get(from).add(to);
        }

        for (String node : graph.keySet()) {
            if (!indexMap.containsKey(node)) {
                tarjan(node);
            }
        }

        for (List<String> component : components) {
            Collections.sort(component);
        }

        Collections.reverse(components);
        for (List<String> component : components) {
            for (String node : component) {
                System.out.print(node);
            }
            System.out.println();
        }
    }

    private static void tarjan(String v) {
        indexMap.put(v, index);
        lowLinkMap.put(v, index);
        index++;
        stack.push(v);
        onStack.add(v);

        for (String neighbor : graph.get(v)) {
            if (!indexMap.containsKey(neighbor)) {
                tarjan(neighbor);
                lowLinkMap.put(v, Math.min(lowLinkMap.get(v), lowLinkMap.get(neighbor)));
            } else if (onStack.contains(neighbor)) {
                lowLinkMap.put(v, Math.min(lowLinkMap.get(v), indexMap.get(neighbor)));
            }
        }

        if (lowLinkMap.get(v).equals(indexMap.get(v))) {
            List<String> component = new ArrayList<>();
            String w;
            do {
                w = stack.pop();
                onStack.remove(w);
                component.add(w);
            } while (!w.equals(v));
            components.add(component);
        }
    }
}

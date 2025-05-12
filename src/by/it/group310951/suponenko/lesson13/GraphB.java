package by.it.group310951.suponenko.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> graph = new HashMap<>();
        String[] edges = input.split(",\\s*");

        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String u = parts[0];
            String v = parts[1];

            graph.putIfAbsent(u, new ArrayList<>());
            graph.get(u).add(v);
            graph.putIfAbsent(v, new ArrayList<>());
        }

        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();

        boolean hasCycle = false;
        for (String node : graph.keySet()) {
            if (dfsHasCycle(graph, node, visited, recursionStack)) {
                hasCycle = true;
                break;
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean dfsHasCycle(Map<String, List<String>> graph, String current,
                                       Set<String> visited, Set<String> stack) {
        if (stack.contains(current)) return true;
        if (visited.contains(current)) return false;

        visited.add(current);
        stack.add(current);

        for (String neighbor : graph.get(current)) {
            if (dfsHasCycle(graph, neighbor, visited, stack)) {
                return true;
            }
        }

        stack.remove(current);
        return false;
    }
}

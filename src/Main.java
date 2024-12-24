import java.util.*;

// Class to represent a graph
class Graph {
    int V, E; // Number of vertices and edges
    Edge[] edge; // Array to store all edges

    // Graph constructor
    Graph(int v, int e) {
        V = v;
        E = e;
        edge = new Edge[E];
        for (int i = 0; i < e; ++i)
            edge[i] = new Edge();
    }

    // Class to represent an edge
    static class Edge {
        int src, dest;
    }
}

// Disjoint Set Union (DSU) with Path Compression and Union by Rank
class DisjointUnionSets {
    int[] rank, parent;
    int n;

    // Constructor
    public DisjointUnionSets(int n) {
        rank = new int[n];
        parent = new int[n];
        this.n = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i; // Initially, each element is its own parent
            rank[i] = 1;   // Each set starts with rank 1
        }
    }

    // Find operation with path compression
    public int find(int u) {
        if (parent[u] != u) {
            parent[u] = find(parent[u]); // Path compression
        }
        return parent[u];
    }

    // Union operation by rank
    public void union(int u, int v) {
        int rootU = find(u);
        int rootV = find(v);

        if (rootU == rootV) return; // Already in the same set

        // Union by rank
        if (rank[rootU] < rank[rootV]) {
            parent[rootU] = rootV;
            rank[rootV] += rank[rootU];
        } else {
            parent[rootV] = rootU;
            rank[rootU] += rank[rootV];
        }
    }
}

public class Main {
    public static boolean hasCycle(Graph graph, DisjointUnionSets dus) {
        for (int i = 0; i < graph.E; i++) {
            int u = graph.edge[i].src;
            int v = graph.edge[i].dest;

            int rootU = dus.find(u);
            int rootV = dus.find(v);

            if (rootU == rootV) {
                return true;
            } else {
                dus.union(u, v);
            }
        }
        return false;
    }

    // Method to calculate the number of pairs from different sets
    public static long numberOfPairs(Graph graph, DisjointUnionSets dus) {
        // Perform union operations for all edges
        for (int i = 0; i < graph.E; i++) {
            dus.union(graph.edge[i].src, graph.edge[i].dest);
        }

        Map<Integer, Integer> componentSize = new HashMap<>();
        for (int i = 0; i < graph.V; i++) {
            int root = dus.find(i);
            componentSize.put(root, componentSize.getOrDefault(root, 0) + 1);
        }

        long totalPairs = ((long) graph.V * (graph.V - 1)) / 2;

        long intraSetPairs = 0;
        for (int size : componentSize.values()) {
            intraSetPairs += ((long) size * (size - 1)) / 2;
        }


        return totalPairs - intraSetPairs;
    }

    public static void main(String[] args) {
        // Test Case 1: Cycle Detection
        System.out.println("=== Test Case 1: Cycle Detection ===");
        int V1 = 3, E1 = 3;
        Graph graph1 = new Graph(V1, E1);

        // Add edges
        graph1.edge[0].src = 0;
        graph1.edge[0].dest = 1;

        graph1.edge[1].src = 1;
        graph1.edge[1].dest = 2;

        graph1.edge[2].src = 0;
        graph1.edge[2].dest = 2;

        DisjointUnionSets dus1 = new DisjointUnionSets(V1);

        boolean cycle1 = hasCycle(graph1, dus1);
        if (cycle1)
            System.out.println("Graph contains cycle");
        else
            System.out.println("Graph doesn't contain cycle");

        // Test Case 2: Number of Pairs from Different Sets
        System.out.println("\n=== Test Case 2: Number of Pairs from Different Sets ===");
        int V2 = 5, E2 = 3;
        Graph graph2 = new Graph(V2, E2);

        // Add edges
        graph2.edge[0].src = 0;
        graph2.edge[0].dest = 1;

        graph2.edge[1].src = 2;
        graph2.edge[1].dest = 3;

        graph2.edge[2].src = 0;
        graph2.edge[2].dest = 4;

        DisjointUnionSets dus2 = new DisjointUnionSets(V2);

        long result = numberOfPairs(graph2, dus2);
        System.out.println("Number of pairs from different sets: " + result);
    }
}

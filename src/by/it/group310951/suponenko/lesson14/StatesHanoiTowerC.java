package by.it.group310951.suponenko.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {
    static int N;
    static int[][] pegs;
    static int[] sizes;
    static int totalMoves;
    static int[] maxH;
    static int[] parent;
    static int[] dsuSize;
    static int[] firstOccur;
    static int moveIndex = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        N = sc.nextInt();
        sc.close();
        if (N <= 0) {
            System.out.println();
            return;
        }
        totalMoves = (1 << N) - 1;
        maxH = new int[totalMoves];
        parent = new int[totalMoves];
        dsuSize = new int[totalMoves];
        firstOccur = new int[N + 1];
        for (int i = 0; i < totalMoves; i++) {
            parent[i] = i;
            dsuSize[i] = 1;
        }
        for (int h = 0; h <= N; h++) {
            firstOccur[h] = -1;
        }
        pegs = new int[3][N];
        sizes = new int[3];
        for (int i = 0; i < N; i++) {
            pegs[0][i] = N - i;
        }
        sizes[0] = N;
        sizes[1] = 0;
        sizes[2] = 0;
        moveIndex = 0;
        hanoi(N, 0, 1, 2);
        int roots = 0;
        for (int i = 0; i < totalMoves; i++) {
            if (find(i) == i) roots++;
        }
        int[] result = new int[roots];
        int idx = 0;
        for (int i = 0; i < totalMoves; i++) {
            if (find(i) == i) {
                result[idx++] = dsuSize[i];
            }
        }
        java.util.Arrays.sort(result);
        StringBuilder sb = new StringBuilder();
        for (int v : result) {
            sb.append(v).append(' ');
        }
        if (sb.length() > 0) sb.setLength(sb.length() - 1);
        System.out.println(sb.toString());
    }

    static void hanoi(int n, int from, int to, int aux) {
        if (n == 0) return;
        hanoi(n - 1, from, aux, to);
        moveDisk(from, to);
        hanoi(n - 1, aux, to, from);
    }

    static void moveDisk(int from, int to) {
        int disk = pegs[from][--sizes[from]];
        pegs[to][sizes[to]++] = disk;
        int h = sizes[0];
        if (sizes[1] > h) h = sizes[1];
        if (sizes[2] > h) h = sizes[2];
        int i = moveIndex;
        if (firstOccur[h] == -1) {
            firstOccur[h] = i;
        } else {
            union(i, firstOccur[h]);
        }
        moveIndex++;
    }

    static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    static void union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) return;
        if (dsuSize[ra] < dsuSize[rb]) {
            parent[ra] = rb;
            dsuSize[rb] += dsuSize[ra];
        } else {
            parent[rb] = ra;
            dsuSize[ra] += dsuSize[rb];
        }
    }
}

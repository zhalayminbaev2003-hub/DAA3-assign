import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Missions 1 and 2. Building the maze out of the walls.
 * A wall k joins the cells w.a()[k] and w.b()[k] and costs w.cost()[k]. Indexes start from 0,
 * the line number in walls.txt is the index + 1. The cells are connected by DisjointSet.
 */
public class Maze {

    /**
     * Mission 1. Take the walls in the order of the file. Break a wall when its two cells are in different sets.
     * Returns the indexes of the broken walls, in the order of breaking.
     */
    public static int[] breakWalls(Input.Walls w) {
        int numCells = w.cells();
        DisjointSet d = new DisjointSet(numCells);
        int totalWalls = w.count();
        List<Integer> broken = new ArrayList<>();

        for (int k = 0; k < totalWalls; k++) {
            if (d.union(w.a()[k], w.b()[k])) {
                broken.add(k);
            }
        }

        int[] result = new int[broken.size()];
        for (int i = 0; i < broken.size(); i++) {
            result[i] = broken.get(i);
        }
        return result;
    }

    /**
     * Mission 2. Kruskal: the same, but take the walls from the cheapest to the most expensive.
     * Returns the indexes of the broken walls.
     */
    public static int[] cheapestMaze(Input.Walls w) {
        // TODO
        int numCells = w.cells();
        DisjointSet d = new DisjointSet(numCells);
        int totalWalls = w.count();

        Integer[] wallIdices = new Integer[totalWalls];
        for (int i = 0; i < totalWalls; i++) {
            wallIdices[i] = i;
        }

        Arrays.sort(wallIdices, Comparator.comparingInt(i -> w.cost()[i]));

        List<Integer> broken = new ArrayList<>();
        for (int i : wallIdices) {
            if (d.union(w.a()[i], w.b()[i])) {
                broken.add(i);
            }
        }

        int[] result = new int[broken.size()];
        for (int i = 0; i < broken.size(); i++) {
            result[i] = broken.get(i);
        }
        return result;
    }

    /**
     * Mission 2. Prim with a priority queue. Returns the total cost of the cheapest maze.
     * It must be equal to the total cost of cheapestMaze.
     */
    public static long primCost(Input.Walls w) {
        int numCells = w.cells();
        int totalWalls = w.count();

        List<int[]>[] adj = new ArrayList[numCells];
        for (int i = 0; i < numCells; i++) {
            adj[i] = new ArrayList<>();
        }

        for (int i = 0; i < totalWalls; i++) {
            int cellA = w.a()[i];
            int cellB = w.b()[i];
            int wallCost = w.cost()[i];

            adj[cellA].add(new int[]{cellB, wallCost});
            adj[cellB].add(new int[]{cellA, wallCost});
        }

        boolean[] visited = new boolean[numCells];

        PriorityQueue<int[]> pq = new PriorityQueue<>(new java.util.Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return Integer.compare(a[1], b[1]);
            }
        });

        long totalCost = 0;
        int visitedCount = 0;

        pq.add(new int[]{0, 0});

        while (!pq.isEmpty() && visitedCount < numCells) {
            int[] edge = pq.poll();
            int currentCell = edge[0];
            int currentCost = edge[1];

            if (visited[currentCell]) {
                continue;
            }

            visited[currentCell] = true;
            totalCost += currentCost;
            visitedCount++;

            List<int[]> neighbors = adj[currentCell];
            for (int i = 0; i < neighbors.size(); i++) {
                int[] neighborEdge = neighbors.get(i);
                int nextCell = neighborEdge[0];
                int nextCost = neighborEdge[1];

                if (!visited[nextCell]) {
                    pq.add(new int[]{nextCell, nextCost});
                }
            }
        }

        return totalCost;
    }
}

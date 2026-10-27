import java.util.*;

/**
 * Missions 4 and 5. The slides: a directed graph. Cells are 0 .. n-1, slide i goes from s.from()[i] to s.to()[i]
 * and takes s.time()[i]. There are no slides from a cell to itself, and the exit can always be reached from the start.
 *
 * Ready for you: Graphs.slidesFrom(s)[u] is the list of the slides that leave cell u, and Graphs.slidesInto(s)[v] is the list
 * of the slides that enter cell v (reversed graph). The lists hold slide numbers i; the cell at the other end is s.to()[i]
 * or s.from()[i].
 */
public class Zones {

    /**
     * Mission 4. Kosaraju. Returns zoneOf: zoneOf[cell] = number of the zone, numbers 0 .. zones-1.
     * Number the zones in the order in which the second pass finds them. That order is a topological order
     * of the zone graph: every slide between two zones goes from a smaller number to a larger number.
     *
     * Plan: 1) DFS on the slides, write the cells in the order in which they are finished (use a stack, not recursion);
     * 2) go through the cells from the last finished to the first; if a cell has no zone yet, DFS from it on the reversed
     * slides (slidesInto) and give every cell it reaches the next zone number.
     * Is DAG in Main is 1 when the number of zones equals the number of cells (no cycle), so you do not need a separate check.
     */
    public static int[] kosaraju(Input.Slides s) {
        int n = s.n();
        int[][] adj = Graphs.slidesFrom(s);
        int[][] revAdj = Graphs.slidesInto(s);

        boolean[] visited = new boolean[n];
        Deque<Integer> finishOrder = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                dfs1(i, s, adj, visited, finishOrder);
            }
        }

        Arrays.fill(visited, false);
        int[] zoneOf = new int[n];
        Arrays.fill(zoneOf, -1);
        int zoneId = 0;

        for (int u : finishOrder) {
            if (!visited[u]) {
                dfs2(u, s, revAdj, visited, zoneOf, zoneId);
                zoneId++;
            }
        }

        return zoneOf;
    }

    private static void dfs1(int start, Input.Slides s, int[][] adj, boolean[] visited, Deque<Integer> finishOrder) {
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(start);
        
        while (!stack.isEmpty()) {
            int u = stack.peek();
            if (!visited[u]) {
                visited[u] = true;
            }
            
            boolean hasUnvisited = false;
            for (int slideIdx : adj[u]) {
                int v = s.to()[slideIdx];
                if (!visited[v]) {
                    stack.push(v);
                    hasUnvisited = true;
                    break;
                }
            }
            
            if (!hasUnvisited) {
                stack.pop();
                finishOrder.push(u);
            }
        }
    }

    private static void dfs2(int start, Input.Slides s, int[][] revAdj, boolean[] visited, int[] zoneOf, int zoneId) {
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(start);
        
        while (!stack.isEmpty()) {
            int u = stack.pop();
            if (!visited[u]) {
                visited[u] = true;
                zoneOf[u] = zoneId;
                for (int slideIdx : revAdj[u]) {
                    int v = s.from()[slideIdx];
                    if (!visited[v]) {
                        stack.push(v);
                    }
                }
            }
        }
    }

    
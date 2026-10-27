import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * Mission 3. Escape: DFS from cell 0 to the last cell (rows * cols - 1) in the maze of Mission 1.
 * Two neighbouring cells are connected when the wall between them is broken.
 * The neighbours are tried in this order: up, right, down, left (cell - cols, cell + 1, cell + cols, cell - 1).
 * The ready method Graphs.open(w, broken) gives you the open neighbours of every cell, in no particular order:
 * to visit them in the required order, go through the four cells above one by one and take those that are in the list.
 * The program must not overflow the stack on a maze of 1000 by 1000 cells.
 */
public class Escape {

    /**
     * path: the cells of the way out, from 0 to the last cell.
     * visited: how many cells are marked as visited at the moment the exit is marked (the start and the exit are counted).
     * deadEnds: how many cells of the maze have exactly one open neighbour.
     */
    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {
        int rows = w.rows();
        int cols = w.cols();
        int n = w.cells();
        int exitCell = n - 1;

        int[][] openNeighbors = Graphs.open(w, broken);
        
        boolean[] visited = new boolean[n];
        int visitedCount = 0;
        int[] parent = new int[n]; 
        for (int i = 0; i < n; i++) {
            parent[i] = -1;
        }

        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(0);
        visited[0] = true;
        visitedCount++;

        int exitVisitedCount = 0;
        boolean foundExit = false;

        while (!stack.isEmpty()) {
            int curr = stack.peek();

            // Если дошли до выхода
            if (curr == exitCell && !foundExit) {
                foundExit = true;
                exitVisitedCount = visitedCount;
            }

            int[] directions = { -cols, 1, cols, -1 };
            
            int nextCellToVisit = -1;

            for (int dir : directions) {
                int neighbor = curr + dir;

                if (dir == 1 && curr % cols == cols - 1) continue;
                if (dir == -1 && curr % cols == 0) continue;

                if (neighbor >= 0 && neighbor < n) {
                    if (isOpen(openNeighbors[curr], neighbor) && !visited[neighbor]) {
                        nextCellToVisit = neighbor;
                        break; 
                    }
                }
            }

            if (nextCellToVisit != -1) {
                visited[nextCellToVisit] = true;
                visitedCount++;
                parent[nextCellToVisit] = curr;
                stack.push(nextCellToVisit);
            } else {
                stack.pop();
            }
        }
        
        List<Integer> pathList = new ArrayList<>();
        int current = exitCell;
        while (current != -1) {
            pathList.add(current);
            current = parent[current];
        }
        Collections.reverse(pathList);

        int[] path = new int[pathList.size()];
        for (int i = 0; i < pathList.size(); i++) {
            path[i] = pathList.get(i);
        }

        int deadEnds = 0;
        for (int i = 0; i < n; i++) {
            if (openNeighbors[i].length == 1) {
                deadEnds++;
            }
        }

        return new Result(path, exitVisitedCount == 0 ? visitedCount : exitVisitedCount, deadEnds);
    }

    private static boolean isOpen(int[] neighbors, int target) {
        for (int cell : neighbors) {
            if (cell == target) {
                return true;
            }
        }
        return false;
    }
}
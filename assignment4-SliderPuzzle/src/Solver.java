/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private int moves;
    private Node goalNode;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        if (initial == null)
            throw new IllegalArgumentException("Illegal inital in Solver(initial) invoking");
        MinPQ<Node> pq = new MinPQ<>();
        MinPQ<Node> twinpq = new MinPQ<>();
        /**
         * we solve board and its twin at the same time
         * when twin is solved earlier, the board is unsolvable
         */
        pq.insert(new Node(initial, 0, null));
        twinpq.insert(new Node(initial.twin(), 0, null));
        Node cur, twincur;
        while (!pq.min().board.isGoal() && !twinpq.min().board.isGoal()) {
            cur = pq.delMin();
            for (Board neiBoard : cur.board.neighbors()) {
                if (cur.prev == null || !neiBoard.equals(cur.prev.board)) {
                    pq.insert(new Node(neiBoard, cur.moves + 1, cur));
                }
            }

            twincur = twinpq.delMin();
            for (Board neiBoard : twincur.board.neighbors()) {
                if (twincur.prev == null || !neiBoard.equals(twincur.prev.board)) {
                    twinpq.insert(new Node(neiBoard, twincur.moves + 1, twincur));
                }
            }
        }
        if (pq.min().board.isGoal()) {
            moves = pq.min().moves;
            goalNode = pq.min();
        }
        else {
            moves = -1;
        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return moves != -1;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> soluBoardStack = new Stack<>();
        Node node = goalNode;
        while (node != null) {
            soluBoardStack.push(node.board);
            node = node.prev;
        }
        return soluBoardStack;
    }

    private class Node implements Comparable<Node> {

        private final Board board;
        private final int moves;
        private final Node prev;
        private int manhattanPriority = -1;

        Node(Board board, int moves, Node prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
        }

        private int getManhattanPriority() {
            if (manhattanPriority == -1) {
                manhattanPriority = board.manhattan() + moves;
            }
            return manhattanPriority;
        }

        @Override
        public int compareTo(Node node) {
            return Integer.compare(this.getManhattanPriority(),
                                   node.getManhattanPriority());
        }
    }


    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}

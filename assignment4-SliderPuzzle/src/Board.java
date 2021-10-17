/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Board {

    private final int dimension;
    private final int[][] blocks;
    private int hammingDistance = -1;
    private int manhattanDistance = -1;


    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        dimension = tiles.length;
        this.blocks = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            System.arraycopy(tiles[i], 0, blocks[i], 0, dimension);
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dimension).append("\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                sb.append(String.format("%2d ", blocks[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of tiles out of place
    public int hamming() {
        if (hammingDistance == -1) {
            // first invoking
            hammingDistance = 0;
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    int currentValue = blocks[i][j];
                    if (currentValue != 0 && currentValue != goalValue(i, j)) {
                        hammingDistance++;
                    }
                }
            }
        }
        return hammingDistance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (manhattanDistance == -1) {
            // first invoking
            manhattanDistance = 0;
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    int currentValue = blocks[i][j];
                    if (currentValue != 0 && currentValue != goalValue(i, j)) {
                        int ii = (currentValue - 1) / dimension;
                        int jj = currentValue - 1 - ii * dimension;
                        int distance = Math.abs(i - ii) + Math.abs(j - jj);
                        manhattanDistance += distance;
                    }
                }
            }
        }
        return manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    @Override
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y == this) return true;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        return Arrays.deepEquals(this.blocks, that.blocks);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int i0 = -1, j0 = -1;
        FindBlankSpace:
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (blocks[i][j] == 0) {
                    i0 = i;
                    j0 = j;
                    break FindBlankSpace;
                }
            }
        }

        Stack<Board> boardStacks = new Stack<>();
        Board nextBoard;
        if (i0 != 0) {
            nextBoard = new Board(blocks);
            nextBoard.exch(i0, j0, i0 - 1, j0);
            boardStacks.push(nextBoard);
        }
        if (i0 != dimension - 1) {
            nextBoard = new Board(blocks);
            nextBoard.exch(i0, j0, i0 + 1, j0);
            boardStacks.push(nextBoard);
        }
        if (j0 != 0) {
            nextBoard = new Board(blocks);
            nextBoard.exch(i0, j0, i0, j0 - 1);
            boardStacks.push(nextBoard);
        }
        if (j0 != dimension - 1) {
            nextBoard = new Board(blocks);
            nextBoard.exch(i0, j0, i0, j0 + 1);
            boardStacks.push(nextBoard);
        }
        return boardStacks;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twinBoard = new Board(blocks);
        for (int i = 0; i < 2; i++) {
            if (twinBoard.blocks[i][0] != 0 && twinBoard.blocks[i][1] != 0) {
                twinBoard.exch(i, 0, i, 1);
                return twinBoard;
            }
        }
        throw new IllegalArgumentException("");
    }

    // get the value of (i,j) tile on goal board
    private int goalValue(int i, int j) {
        // last tile is 0
        if (i == dimension - 1 && j == dimension - 1) return 0;
        return i * dimension + j + 1;
    }

    // swap squares at (sourX,sourY) and (destX, destY)
    private void exch(int sourX, int sourY, int destX, int destY) {
        assert (sourX >= 0 && sourX < dimension
                && sourY >= 0 && sourY < dimension
                && destX >= 0 && destX < dimension
                && destY >= 0 && destY < dimension);
        int tmp = blocks[destX][destY];
        blocks[destX][destY] = blocks[sourX][sourY];
        blocks[sourX][sourY] = tmp;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int[][] init = new int[n][n];
        int start = n * n - 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                init[i][j] = start--;
            }
        }
        Board b = new Board(init);
        StdOut.print(b);

        Board twin;
        twin = b.twin();
        StdOut.print(twin);
    }
}

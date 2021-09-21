/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int size;
    private final WeightedQuickUnionUF uf;
    private boolean[] sign;
    private int numberOfOpenSites = 0;
    private final int top;
    private final int bottom;


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        validate(n);
        this.size = n;
        this.uf = new WeightedQuickUnionUF(n * n + 2);
        this.sign = new boolean[n * n];
        this.top = n * n;
        this.bottom = n * n + 1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);
        int index = xyTo1D(row, col);
        if (!sign[index]) {
            sign[index] = true;
            numberOfOpenSites++;
        }

        // introduce 2 virtual sites：top and bottom
        if (row == 1) {
            uf.union(index, top);
        }
        if (row == size) {
            uf.union(index, bottom);
        }

        if (siteExists(row - 1, col) && isOpen(row - 1, col)) {
            uf.union(index, xyTo1D(row - 1, col));
        }
        if (siteExists(row + 1, col) && isOpen(row + 1, col)) {
            uf.union(index, xyTo1D(row + 1, col));
        }
        if (siteExists(row, col - 1) && isOpen(row, col - 1)) {
            uf.union(index, xyTo1D(row, col - 1));
        }
        if (siteExists(row, col + 1) && isOpen(row, col + 1)) {
            uf.union(index, xyTo1D(row, col + 1));
        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return sign[xyTo1D(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        return uf.find(xyTo1D(row, col)) == uf.find(top);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(top) == uf.find(bottom);
    }


    private void validate(int row, int col) {
        if (row <= 0 || col <= 0 || row > size || col > size) {
            throw new IllegalArgumentException();
        }
    }

    private void validate(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
    }

    private int xyTo1D(int row, int col) {
        validate(row, col);
        return size * (row - 1) + col - 1;
    }

    private boolean siteExists(int row, int col) {
        if (row <= 0 || row > size || col <= 0 || col > size) {
            return false;
        }
        return true;
    }

}

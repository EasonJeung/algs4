/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CFDE_95 = 1.96;
    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        validate(n, trials);
        double[] data = new double[trials];
        for (int numOfTrails = 0; numOfTrails < trials; numOfTrails++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                p.open(row, col);
            }
            data[numOfTrails] = (double) p.numberOfOpenSites() / (n * n);
        }
        mean = StdStats.mean(data);
        stddev = StdStats.stddev(data);
        confidenceLo = mean - CFDE_95 * stddev / Math.sqrt(trials);
        confidenceHi = mean + CFDE_95 * stddev / Math.sqrt(trials);
    }

    // sample mean of percolation threshold
    public double mean() {
        return this.mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return this.stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.confidenceHi;
    }

    private void validate(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trails = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trails);

        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println(
                "95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}

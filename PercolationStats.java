/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONF_95 = 1.96;
    // number of independent experiments
    private int t;
    // Percolation threshold for T experiments
    private double[] p;


    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials must be greater than 0");
        }
        this.t = trials;
        this.p = new double[trials];

        for (int i = 0; i < trials; i++) {
            // create a new percolation grid for each iteration
            Percolation grid = new Percolation(n);
            while (!grid.percolates()) {
                // randomly open sites at blocked locations
                int row = (int) StdRandom.uniformDouble(1, n + 1);
                int col = (int) StdRandom.uniformDouble(1, n + 1);

                if (!grid.isOpen(row, col)) {
                    grid.open(row, col);

                }
            }

            p[i] = (double) grid.numberOfOpenSites() / (n * n);
        }


    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(p);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(p);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return StdStats.mean(p) - (CONF_95 * StdStats.stddev(p) /
                Math.sqrt(t));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return StdStats.mean(p) + CONF_95 * StdStats.stddev(p) /
                Math.sqrt(t);
    }

    // test client (see below)
    public static void main(String[] args) {
        String n = args[0];
        String trials = args[1];
        PercolationStats stat = new PercolationStats(Integer.parseInt(n),
                                                     Integer.parseInt(trials));
        // print out mean
        System.out.println("mean = " + stat.mean());
        // print out standard deviation
        System.out.println("stddev = " + stat.stddev());
        // print out the confidence interval
        System.out.println("95% confidence interval = " + "[" + stat.confidenceLo() +
                                   ", " + stat.confidenceHi() + "]");
    }
}

/* *****************************************************************************
 *  Name:         John Otto
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // length or width of a grid
    private int n;
    // an open representing if a block is open or closed
    private boolean[][] open;
    // a grid that allows us to work and
    // private int[][] grid;
    // virtual top site
    private int source;
    // virtual bottom site
    private int sink;
    // the data structure we need and learn in the course
    private WeightedQuickUnionUF uf;


    // creates n-by-n open, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }
        this.n = n;

        // should initialize to all false
        open = new boolean[n][n];

        // initializes the grid from 0 to (n^2 - 1)
        // int[][] sub = new int[n][n];
        // int count = 0;
        // for (int i = 0; i < n; i++) {
        //     for (int j = 0; j < n; j++) {
        //         grid[i][j] = count;
        //         count++;
        //     }
        // }
        // grid = sub;

        // initialize a sink and source
        source = n * n;
        sink = n * n + 1;
        // initialize the weighted union find
        this.uf = new WeightedQuickUnionUF(n * n + 2);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        // check for null pointer exceptions
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException("Out of bounds");
        }

        // if the spot is open then do nothing, else open the spot
        if (open[row - 1][col - 1]) {
            return;
        }
        open[row - 1][col - 1] = true;

        // when opening a top of bottom row we also need to connect to the
        // source or sink
        if (row == 1) {
            uf.union(index(row, col), source);
        }
        if (row == n) {
            uf.union(index(row, col), sink);
        }

        // connect the new open site to a previously open site
        // make sure to check for out of bounds exceptions
        // up
        if (row > 1) {
            if (open[row - 1][col]) {
                uf.union(index(row - 1, col), index(row, col));
            }
        }

        // down
        if (row < n) {
            if (isOpen(row + 1, col)) {
                uf.union(index(row, col), index(row, col));
            }
        }

        // left
        if (col > 1) {
            if (isOpen(row, col - 1)) {
                uf.union(index(row, col - 1), index(row, col));
            }
        }

        // right
        if (col > n) {
            if (isOpen(row, col + 1)) {
                uf.union(index(row, col + 1), index(row, col));
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException("Out of bounds");
        }
        return open[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        // edit this
        // I think it is asking a different question
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException("Out of bounds");
        }
        return uf.find(index(row, col)) == uf.find(source);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int total = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (open[i][j]) {
                    total++;
                }
            }
        }
        return total;
    }

    public int index(int row, int col) {
        return col - 1 + n * (row - 1);
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(source) == uf.find(sink);
    }
}

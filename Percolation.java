/* *****************************************************************************
 *  Name:         John Otto
 **************************************************************************** */


import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // length or width of a grid
    private final int n;
    // an open representing if a block is open or closed
    private boolean[][] open;
    // total number of open sites
    private int totalOpen;
    // virtual top site
    private final int source;
    // virtual bottom site
    private final int sink;
    // the data structure we need and learn in the course
    private final WeightedQuickUnionUF uf;


    // creates n-by-n open, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }
        this.n = n;

        // should initialize to all false
        this.open = new boolean[n][n];

        this.totalOpen = 0;

        // initialize a sink and source
        source = 0;
        sink = n * n + 1;

        // initialize the weighted union find
        this.uf = new WeightedQuickUnionUF(n * n + 2);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        // check for null pointer exceptions
        validate(row, col);

        // if the spot is open then do nothing, else open the spot
        if (isOpen(row, col)) {
            return;
        }
        open[row - 1][col - 1] = true;
        totalOpen++;

        // initialize the int of the current site for readability
        int currentSiteIndex = index(row, col);

        // when opening a top of bottom row we also need to connect to the source or sink
        if (row == 1) {
            uf.union(currentSiteIndex, source);
        }
        if (row == n) {
            uf.union(currentSiteIndex, sink);
        }

        /*
         * Connect the new open site to a previously open site
         * while making sure to check for out of bounds exceptions.
         */
        // up
        if (row > 1) {
            if (isOpen(row - 1, col)) {
                uf.union(currentSiteIndex, index(row - 1, col));
            }
        }

        // down
        if (row < n) {
            if (isOpen(row + 1, col)) {
                uf.union(currentSiteIndex, index(row + 1, col));
            }
        }

        // left
        if (col > 1) {
            if (isOpen(row, col - 1)) {
                uf.union(currentSiteIndex, index(row, col - 1));
            }
        }

        // right
        if (col < n) {
            if (isOpen(row, col + 1)) {
                uf.union(currentSiteIndex, index(row, col + 1));
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return open[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        // return true if the grid is connected to the source node
        return uf.find(index(row, col)) == uf.find(source);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return totalOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(source) == uf.find(sink);
    }

    // private helper method which return the index of each grid
    private int index(int row, int col) {
        validate(row, col);
        return col + n * (row - 1);
    }

    // checks for array index out of bounds exceptions
    private void validate(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException("Out of bounds");
        }
    }
}

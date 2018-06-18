/**
 * Created by Philip Ivanov (https://github.com/personalaccount)
 */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int gridSide; // holds the length of the grid axis
    private boolean[][] grid; // the grid itself

    private WeightedQuickUnionUF sites; // WQUF object holding open, connected sites and testing if the system percolates
    private WeightedQuickUnionUF sitesBackWash; // WQUF object holding open, connected sites and testing if the system percolates

    // Purely for the sake of coherence - variables that hold id's of two extra sites
    private int virtualTop;
    private int virtualBottom;

    private int numberOfOpenSites; // holds the number of open sites

    // Create n-by-n grid, with all sites blocked
    public Percolation(int n) {

        if (n < 1) throw new IllegalArgumentException("The number has to be positive");

        gridSide = n;
        int gridArea = gridSide * gridSide; // holds the area of the grid = gridSide*gridSide
        virtualTop = 0; // id of the virtual top
        virtualBottom = gridArea + 1; // id of the virtual bottom
        numberOfOpenSites = 0;

        grid = new boolean[gridSide][gridSide]; // initialize n-by-n grid

        // initialize WQUF object, adding two extra sites - one for top ( 0 ) and bottom (N*N + 1);
        sites = new WeightedQuickUnionUF(gridArea + 2);
        sitesBackWash = new WeightedQuickUnionUF(gridArea + 2);

        // fill out the grid
        for (int row = 0; row < gridSide; row++) {

            for (int col = 0; col < gridSide; col++) {

                // mark site as blocked
                grid[row][col] = false;

            }

        }
    }

    // determine site id, based on the n-by-n grid coordinates
    private int getSiteId(int row, int col) {
        return ((row - 1) * gridSide) + col; // decrement row to account for the 1,1 convention
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isOpen(row, col)) {

            grid[row-1][col-1] = true; // decrement row and col to account for the 1,1 convention

            int thisSite = getSiteId(row, col);

            // check if the site above is open and if so - connect
            try {
                if (isOpen(row - 1, col)) {
                    sites.union(getSiteId(row - 1, col), thisSite);
                    sitesBackWash.union(getSiteId(row - 1, col), thisSite);
                }
            } catch (IllegalArgumentException e) {
                // the site is in the top row - connect it to the virtual top
                sites.union(virtualTop, thisSite);
                sitesBackWash.union(virtualTop, thisSite);
            }

            // check if the site below is open and if so - connect
            try {
                if (isOpen(row + 1, col)) {
                    sites.union(getSiteId(row + 1, col), thisSite);
                    sitesBackWash.union(getSiteId(row + 1, col), thisSite);
                }
            } catch (IllegalArgumentException e) {
                // the site is in the bottom row
                sites.union(virtualBottom, thisSite);
            }

            // check if the site on the right is open and if so - connect
            try {
                if (isOpen(row, col+1)) {
                    sites.union(thisSite, getSiteId(row, col + 1));
                    sitesBackWash.union(thisSite, getSiteId(row, col + 1));
                }
            } catch (IllegalArgumentException e) {
                // the site is in the rightmost column
            }

            // check if the site on the left is open and connect if so
            try {
                if (isOpen(row, col-1)) {
                    sites.union(thisSite, getSiteId(row, col-1));
                    sitesBackWash.union(thisSite, getSiteId(row, col-1));
                }
            } catch (IllegalArgumentException e) {
                // the site is in the leftmost column
            }

            numberOfOpenSites++;
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > gridSide) throw new IllegalArgumentException("Row number has to be between 1 and " + gridSide + " Your entered: " + row);
        if (col < 1 || col > gridSide) throw new IllegalArgumentException("Column number has to be between 1 and " + gridSide + " Your entered: " + col);

        return grid[row-1][col-1];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        return isOpen(row, col) && sitesBackWash.connected(getSiteId(row, col), virtualTop);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return  sites.connected(virtualTop, virtualBottom);
    }

    private void printGrid() {
        for (int i = 0; i < gridSide; i++) {
            for (int j = 0; j < gridSide; j++) {
                StdOut.print(" " + grid[i][j]);
            }
            StdOut.println("");
        }
        StdOut.println("Number of open sites: " + numberOfOpenSites());
        if (percolates()) StdOut.println("System percolates!" + "\n");
    }

    private int[] getCoordinates(int siteId) {

        // array holding row[0] and column[1]
        int[] coordinates = new int[2];

        if (siteId > 0 && siteId <= gridSide*gridSide) {

            // Determine the row, by rounding up
            coordinates[0] = (int) (Math.ceil(((double) siteId / gridSide)));

            // Determine the column, if remainder is 0 then it is the last column
            int column = siteId % gridSide;
            coordinates[1] = (column == 0) ? gridSide : column;

            return coordinates;
        }
        throw new IllegalArgumentException("Site id has to be between 1 and " + gridSide*gridSide + ". Your entry is " + siteId);
    }

    // test client (optional)
    public static void main(String[] args) {

        int[] fullCheckCoordinates = new int[2]; // holds coordinates for the site to be checked

        int n = 3;
        fullCheckCoordinates[0] = 2;
        fullCheckCoordinates[1] = 3;

        // initialize percolation object
        Percolation p = new Percolation(n);

        int totalSites = n*n;

        int[] sitesToOpen = new int[totalSites]; // array holding sequence of sites to open, shuffled prior to each test run

        // fill out the sitesToOpen array
        for (int i = 0; i < totalSites; i++) {
            sitesToOpen[i] = i+1;
        }

        // shuffle sitesToOpen array
        StdRandom.shuffle(sitesToOpen);

        // open sites and check if they are full
        for (int i = 0; i < totalSites; i++) {
            int[] coordinates = p.getCoordinates(sitesToOpen[i]);
            p.open(coordinates[0], coordinates[1]);
            StdOut.println(coordinates[0] + "," + coordinates[1]);
            if (p.isFull(fullCheckCoordinates[0], fullCheckCoordinates[1])) {
                StdOut.println("isFull " + fullCheckCoordinates[0] + "," + fullCheckCoordinates[1] + ": " + p.isFull(fullCheckCoordinates[0], fullCheckCoordinates[1]) + "; number of open sites: " + p.numberOfOpenSites());
                p.printGrid();
                break;
            } else {
                StdOut.println(p.isFull(fullCheckCoordinates[0], fullCheckCoordinates[1]));
            }
        }
    }
}
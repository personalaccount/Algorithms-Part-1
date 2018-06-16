/**
 * Created by Philip Ivanov
 */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private int totalSites; // Total number of sites
    private int gridSide;
    private double mean;
    private double stdDev;
    private double confidenceLo;
    private double confidenceHi;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {

        if (n < 1 || trials < 1) throw new IllegalArgumentException("Grid side and number of trials have to be positive");

        double[] percolationResults = new double[trials]; // array that holds the number of open sites when the system percolates for each trial

        double confidenceLevel = 1.96;
        gridSide = n;
        totalSites = gridSide*gridSide;
        int[] sitesToOpen = new int[totalSites]; // array holding sequence of sites to open, shuffled prior to each test run

        // fill out the sitesToOpen array
        for (int i = 0; i < totalSites; i++) {
            sitesToOpen[i] = i+1;
        }

        // run trials
        for (int i = 0; i < trials; i++) {

            // initialize percolation object
            Percolation testSubject = new Percolation(n);

            // shuffle the sequence of sites to open
            StdRandom.shuffle(sitesToOpen);

            // begin traversing though the shuffled array until the system percolates
            int arrayIndex = 0;

            do {
                int currentSite = sitesToOpen[arrayIndex];
                int[] coordinates = getCoordinates(currentSite);

                testSubject.open(coordinates[0], coordinates[1]);

                arrayIndex++;
            } while (!testSubject.percolates() && arrayIndex < totalSites);

            double openSites = testSubject.numberOfOpenSites();
            percolationResults[i] = openSites/totalSites;
        }

        mean = StdStats.mean(percolationResults);
        stdDev = StdStats.stddev(percolationResults);

        confidenceLo = mean - ((confidenceLevel*stdDev)/Math.sqrt(trials));
        confidenceHi = mean + ((confidenceLevel*stdDev)/Math.sqrt(trials));

    }

    private int[] getCoordinates(int siteId) {

        // array holding row[0] and column[1]
        int[] coordinates = new int[2];

        if (siteId > 0 && siteId <= totalSites) {

            // Determine the row, by rounding up
            coordinates[0] = (int) (Math.ceil(((double) siteId / gridSide)));

            // Determine the column, if remainder is 0 then it is the last column
            int column = siteId % gridSide;
            coordinates[1] = (column == 0) ? gridSide : column;

            return coordinates;
        }
        throw new IllegalArgumentException("Site id has to be between 1 and " + totalSites + ". Your entry is " + siteId);
    }

    // sample mean of percolation threshold
    public double mean() {

        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {

        return stdDev;

    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {

        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {

        return confidenceHi;
    }

    // test client
    public static void main(String[] args) {

        int gridSide = (args.length > 0) ? Integer.parseInt(args[0]) : 2;
        int numberOfTrials = (args.length > 1) ? Integer.parseInt(args[1]) : 100000;

        PercolationStats percStats = new PercolationStats(gridSide, numberOfTrials);

        StdOut.println("mean " + percStats.mean());
        StdOut.println("stddev " + percStats.stddev());
        StdOut.println("95% confidence interval [" + percStats.confidenceLo() + ", " + percStats.confidenceHi() + "]");
    }
}

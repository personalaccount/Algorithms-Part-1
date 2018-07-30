import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.InputMismatchException;

/**
 * Examines 4 points at a time and checks whether they all lie on the same line segment,
 * returning all such line segments.
 *
 * The 4 points p, q, r, and s are collinear if three slopes
 * between p and q, between p and r, and between p and s are all equal.
 *
 * Created by Philip Ivanov (https://github.com/personalaccount)
 */


public class BruteCollinearPoints {

    private int numberOfSegments = 0;
    private Point[] points;
    private LineSegment[] lineSegments;

    /*  Constructor throws a java.lang.IllegalArgumentException if the argument to the constructor is null,
        if any point in the array is null, or if the argument to the constructor contains a repeated point.
     */

    public BruteCollinearPoints(Point[] inputArr) {
        if (inputArr == null) throw new IllegalArgumentException();

        int totalPoints = inputArr.length;

        /* Create a defensive copy of the object referenced by the parameter variable since Point is mutable.
           While creating a copy of the array sort it and check for nulls and duplicates
         */

        this.points = new Point[totalPoints];

        for (int i = 0; i < totalPoints; i++) {
            if (inputArr[i] == null) throw new IllegalArgumentException();

            // Sort array using Insertion sort and check for duplicates while doing so
            points[i] = inputArr[i];
            for (int j = i; j > 0; j--) {
                if (points[j].compareTo(points[j-1]) < 0) {
                    // exchange (points j, j-1)
                    Point swap = points[j-1];
                    points[j-1] = points[j];
                    points[j] = swap;
                }
                else if (points[j].compareTo(points[j-1]) == 0) {
                    // the two points are equal
                    throw new IllegalArgumentException();
                }
                else {
                    break;
                }
            }

        }


        lineSegments = new LineSegment[totalPoints];

        /* Examine 4 points at a time and check whether the three slopes
           between p and q, between p and r, and between p and s are all equal.
         */

        // Break at -3 since there are no subsequent points from the first one after that threshold.
        for (int p = 0; p < totalPoints - 3; p++) {
            for (int q = p + 1; q < totalPoints; q++) {
                for (int r = q + 1; r < totalPoints; r++) {
                    for (int s = r + 1; s < totalPoints; s++) {
                        if (pointsAlign(new int[]{p, q, r, s})) {
                            lineSegments[numberOfSegments++] = new LineSegment(points[p], points[s]);
                            break;
                        }
                    }
                }
            }
        }

    }

    // Traverses through each of the 4 points, checking for nullness and alignment.
    private boolean pointsAlign(int[] pk) {

        for (int i = 1; i < pk.length; i++) {
            // Starting with the second point, compare each point to the first one, to check for duplicates
            if (points[pk[0]].compareTo(points[pk[i]]) == 0) throw new IllegalArgumentException();

            // Start comparing slopes starting from the third point
            if (i > 1) {
                if (Double.compare(points[pk[0]].slopeTo(points[pk[1]]), points[pk[0]].slopeTo(points[pk[i]])) != 0) return false;
            }
        }
        return true;
    }

    // the number of line segments
    public int numberOfSegments() { return numberOfSegments; }

    // the line segments, should include each line segment containing 4 points exactly once
    public LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[numberOfSegments];

        for (int i = 0; i < numberOfSegments; i++) {
            segments[i] = lineSegments[i];
        }

        return segments;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In("collinear-testing/equidistant.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            try {
                int x = in.readInt();
                int y = in.readInt();
                points[i] = new Point(x, y);
            }
            catch (InputMismatchException e) {
                points[i] = null;
            }
        }

        // draw the points
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            try {
                p.draw();
            }
            catch (NullPointerException e) {
                // Encountered a null point, skip
            }
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        if (collinear.numberOfSegments() > 0) {
            StdOut.println(collinear.numberOfSegments());
            for (LineSegment segment : collinear.segments()) {
                StdOut.println(segment);
                segment.draw();
            }
            StdDraw.show();
        }
    }
}
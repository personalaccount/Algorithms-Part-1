import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

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
    private LineSegment[] lineSegments;

    // finds all line segments containing 4 points

    /*
        Throw a java.lang.IllegalArgumentException if the argument to the constructor is null,
        if any point in the array is null, or if the argument to the constructor contains a repeated point.
     */

    public BruteCollinearPoints(Point[] points) {
        int totalPoints = points.length;
        if (points == null || totalPoints < 4) throw new IllegalArgumentException();

        // Array containing line segments (at most half the total number of points)

        lineSegments = new LineSegment[totalPoints/2];

        /* examine 4 points at a time and check whether the three slopes
           between p and q, between p and r, and between p and s are all equal
         */

        Arrays.sort(points);

        double targetSlope;

        for (int p = 0; p < totalPoints - 4; p++) {

            for (int q = p + 1; q < totalPoints; q++) {

                for (int r = q + 1; r < totalPoints; r++) {

                    for (int s = r + 1; s < totalPoints; s++) {

                        if (numberOfSegments > 0) break;

                        if (points[q] == null || points[p] == null || points[r] == null || points[s] == null) throw new IllegalArgumentException();
                        if (points[q] == points[p] || points[q] == points[r] || points[q] == points[s]) throw new IllegalArgumentException();

                        targetSlope = points[q].slopeTo(points[p]);
                        if (points[q].slopeTo(points[r]) == targetSlope) {
                            if (points[q].slopeTo(points[s]) == targetSlope) {
                                lineSegments[numberOfSegments++] = new LineSegment(points[p], points[s]);
                            }
                        }
                    }
                }
            }
        }

    }

    // the number of line segments
    public int numberOfSegments() { return numberOfSegments; }

    // the line segments, should include each line segment containing 4 points exactly once
    public LineSegment[] segments() {
        return lineSegments;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In("test-samples/input6-pr.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
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
        else {
            StdOut.println("No linesegments found");
        }
    }
}
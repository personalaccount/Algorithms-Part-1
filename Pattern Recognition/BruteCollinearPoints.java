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
    private Point[] points;
    private LineSegment[] lineSegments;

    /*  Constructor throws a java.lang.IllegalArgumentException if the argument to the constructor is null,
        if any point in the array is null, or if the argument to the constructor contains a repeated point.
     */

    public BruteCollinearPoints(Point[] inputArr) {
        if (inputArr == null) throw new IllegalArgumentException();

        int totalPoints = inputArr.length;
        if (totalPoints < 4) throw new IllegalArgumentException();

        // a defensive copy of the object referenced by the parameter variable since Point is mutable.
        this.points = new Point[totalPoints];
        for (int i = 0; i < totalPoints; i++) {
            this.points[i] = inputArr[i];
        }

        lineSegments = new LineSegment[totalPoints];

        /* Examine 4 points at a time and check whether the three slopes
           between p and q, between p and r, and between p and s are all equal.
         */

        Arrays.sort(points);

        Point segmentStart = null;
        Point segmentEnd = null;

        // Break at -3 since there are no subsequent points from the first one after that threshold.
        for (int p = 0; p < totalPoints - 3; p++) {

            segmentStart = points[p];

            for (int q = p + 1; q < totalPoints; q++) {
                if (segmentStart == null) break;
                for (int r = q + 1; r < totalPoints; r++) {
                    if (segmentStart == null) break;
                    for (int s = r + 1; s < totalPoints; s++) {

                        if (pointsAlign(new int[]{p, q, r, s})) {
                            lineSegments[numberOfSegments++] = new LineSegment(points[p], points[s]);
                            segmentStart = null;
                            break;
//                            if (numberOfSegments == 0) {
//                                segmentEnd = points[s];
//                                lineSegments[numberOfSegments++] = new LineSegment(segmentStart, segmentEnd);
//                            }
//                            else if (numberOfSegments > 0) {
//                                if (points[p] == segmentStart && points[s] == segmentEnd) {
//                                    continue;
//                                }
//                                else {
//                                    segmentEnd = points[s];
//                                    lineSegments[numberOfSegments++] = new LineSegment(segmentStart, segmentEnd);
//                                }
//                            }
                        }
                    }
                }
            }
        }

    }

    // Check if points align.
    private boolean pointsAlign(int[] pk) {

        for (int i = 0; i < pk.length; i++) {
            // Make sure none of the points are null
            if (points[pk[i]] == null) throw new IllegalArgumentException();

            if (i > 0) {
                // Starting from the second point, make sure none of the points match
                if (points[pk[0]].slopeTo(points[pk[i]]) == Double.NEGATIVE_INFINITY) throw new IllegalArgumentException();

                // Start comparing points starting from the third
                if (i > 1) {
                    if (Double.compare(points[pk[0]].slopeTo(points[pk[i]]), points[pk[0]].slopeTo(points[pk[1]])) != 0) return false;
                }
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
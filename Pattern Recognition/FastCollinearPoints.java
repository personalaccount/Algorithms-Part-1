import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Arrays;
import java.util.InputMismatchException;

/**
 * Created by Philip Ivanov (https://github.com/personalaccount)
 */
public class FastCollinearPoints {

    private int numberOfSegments;
    private Point[] segmentHeads;
    private Point[] segmentTails;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] originalInputArr) {

        if (originalInputArr == null) throw new IllegalArgumentException();

        int totalPoints = originalInputArr.length;

        if (totalPoints < 1) return;

        Point[] points = new Point[totalPoints];
        Point[] inputArr = new Point[totalPoints];

        for (int i = 0; i < totalPoints; i++) {
            if (originalInputArr[i] == null) throw new IllegalArgumentException();

            points[i] = originalInputArr[i];
            inputArr[i] = originalInputArr[i];
        }

        numberOfSegments = 0;

        segmentHeads = new Point[2];
        segmentTails = new Point[2];

        for (int i = 0; i < totalPoints; i++) {

            Point targetPoint = points[i];

            int count = 0; // Holds the number of points in the segment

            // Sort the points according to the slopes they make with p.
            Arrays.sort(inputArr, targetPoint.slopeOrder());

            if (inputArr[1].compareTo(targetPoint) == 0) throw new IllegalArgumentException();
            // Sorted array always start with a point that has the lowest slope to the targetPoint - itself (NEGATIVE_INFINITY)

            // Therefore, search for a matching slope pair starting from the third element
            int j = 2;
            while (j < totalPoints) {
                if (Double.compare(inputArr[j - 1].slopeTo(targetPoint), inputArr[j].slopeTo(targetPoint)) == 0) {

                    // Found two adjacent points with the matching slope.
                    Point segmentStart, segmentEnd;

                    // Compare the targetPoint with the first matching point and set the lower of the two to segmentStart
                    if (inputArr[j - 1].compareTo(targetPoint) < 0) {
                        segmentStart = inputArr[j - 1];
                        segmentEnd = targetPoint;
                    }
                    else {
                        segmentEnd = inputArr[j - 1];
                        segmentStart = targetPoint;
                    }

                    // Increment the counter of points in the segment, which now contains targetPoint and inputArr[j-1]
                    count = 2;

                    double targetSlope = targetPoint.slopeTo(inputArr[j - 1]);

                    // Continue inspecting adjacent points
                    int k = j;
                    for (; k < totalPoints; k++) {
                        // Break out of the loop the moment a mismatching slope is detected
                        if (Double.compare(inputArr[k].slopeTo(targetPoint), targetSlope) != 0) break;

                        // Swap with segmentEnd if the encountered matching point is larger
                        if (segmentEnd.compareTo(inputArr[k]) < 0) segmentEnd = inputArr[k];

                        // Swap with segmentStart if the encountered matching point is smaller
                        if (segmentStart.compareTo(inputArr[k]) > 0) segmentStart = inputArr[k];
                        count++;
                    }

                    // There are no more points with a matching slope. Count the total points and add a segment if over 4
                    if (count >= 4 && segmentIsUnique(segmentStart, segmentEnd)) {

                        // Resize arrays if it has reached the limit of capacity
                        if (numberOfSegments == segmentHeads.length) {
                            resize(numberOfSegments * 2);
                        }

                        segmentHeads[numberOfSegments] = segmentStart;
                        segmentTails[numberOfSegments] = segmentEnd;

                        numberOfSegments++;
                    }

                    // jump over the traversed items
                    j = k;

                    // reset counter
                    count = 0;
                }
                else {
                    j++;
                }
            }
        }
    }

    // Checks if the segment is unique
    private boolean segmentIsUnique(Point segmentStart, Point segmentEnd) {
        if (numberOfSegments > 0) {
            for (int i = 0; i < numberOfSegments; i++) {
                if (segmentHeads[i].compareTo(segmentStart) == 0) {
                    if (segmentTails[i].compareTo(segmentEnd) == 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // resizing the segments array
    private void resize(int capacity) {
        Point[] copyHeads = new Point[capacity];
        Point[] copyTails = new Point[capacity];

        for (int i = 0; i < numberOfSegments; i++) {
            copyHeads[i] = segmentHeads[i];
            copyTails[i] = segmentTails[i];
        }

        segmentHeads = copyHeads;
        segmentTails = copyTails;
    }

    // the number of line segments
    public int numberOfSegments() {
        return numberOfSegments;
    }

    // The method segments() should include each maximal line segment containing 4 (or more) points exactly once.
    public LineSegment[] segments() {
        LineSegment[] returnSegments = new LineSegment[numberOfSegments];
        for (int i = 0; i < numberOfSegments; i++) {
            returnSegments[i] = new LineSegment(segmentHeads[i], segmentTails[i]);
        }
        return returnSegments;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In("collinear-testing/nulltest2.txt");
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
                // Encountered a null point - skip
            }
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        if (collinear.numberOfSegments() > 0) {
            for (LineSegment segment : collinear.segments()) {
                try {
                    segment.draw();
                }
                catch (NullPointerException e) {
                    break;
                }
            }
            StdDraw.show();
        }
    }
}
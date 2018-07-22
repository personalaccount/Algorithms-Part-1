/**
 * Examines 4 points at a time and checks whether they all lie on the same line segment,
 * returning all such line segments.
 *
 * Created by Philip Ivanov (https://github.com/personalaccount)
 */


public class BruteCollinearPoints {

    private int numberOfSegments = 0;
    private LineSegment[] lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {

    }

    // the number of line segments
    public int numberOfSegments() {
        return numberOfSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        if (numberOfSegments() == 0) return null;
        return lineSegments;
    }
}
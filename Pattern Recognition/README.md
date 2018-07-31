# Programming Assignment 3: Pattern Recognition

> Please navigate to the official [assignment 3 URL](http://coursera.cs.princeton.edu/algs4/assignments/collinear.html) for details.

Write a program to recognize line patterns in a given set of points.

**The problem.** Given a set of n distinct points in the plane, find every (maximal) line segment that connects a subset of 4 or more of the points.

**Point data type.** Immutable data type that represents a point in the plane.

- The compareTo() method should compare points by their y-coordinates, breaking ties by their x-coordinates. Formally, the invoking point (x0, y0) is less than the argument point (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.

- The slopeTo() method should return the slope between the invoking point (x0, y0) and the argument point (x1, y1), which is given by the formula (y1 − y0) / (x1 − x0). Treat the slope of a horizontal line segment as positive zero; treat the slope of a vertical line segment as positive infinity; treat the slope of a degenerate line segment (between a point and itself) as negative infinity.

- The slopeOrder() method should return a comparator that compares its two argument points by the slopes they make with the invoking point (x0, y0). Formally, the point (x1, y1) is less than the point (x2, y2) if and only if the slope (y1 − y0) / (x1 − x0) is less than the slope (y2 − y0) / (x2 − x0). Treat horizontal, vertical, and degenerate line segments as in the slopeTo() method.

- Do not override the equals() or hashCode() methods.

*Corner cases*. To avoid potential complications with integer overflow or floating-point precision, you may assume that the constructor arguments x and y are each between 0 and 32,767.

**Brute force.** Write a program BruteCollinearPoints.java that examines 4 points at a time and checks whether they all lie on the same line segment, returning all such line segments. To check whether the 4 points p, q, r, and s are collinear, check whether the three slopes between p and q, between p and r, and between p and s are all equal.

    public class BruteCollinearPoints {
       public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
       public           int numberOfSegments()        // the number of line segments
       public LineSegment[] segments()                // the line segments
    }

The method segments() should include each line segment containing 4 points exactly once. If 4 points appear on a line segment in the order p→q→r→s, then you should include either the line segment p→s or s→p (but not both) and you should not include subsegments such as p→r or q→r. For simplicity, we will not supply any input to BruteCollinearPoints that has 5 or more collinear points.

*Corner cases*. Throw a java.lang.IllegalArgumentException if the argument to the constructor is null, if any point in the array is null, or if the argument to the constructor contains a repeated point.

*Performance requirement*. The order of growth of the running time of your program should be n4 in the worst case and it should use space proportional to n plus the number of line segments returned.






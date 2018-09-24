import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;
import java.util.Comparator;

public class BruteCollinearPoints {

    private final LineSegment[] segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new java.lang.IllegalArgumentException();
        }
        for (Point p : points) {
            if (p == null) {
                throw new java.lang.IllegalArgumentException();
            }
        }
        Point[] pointsInit = points.clone();
        int n = pointsInit.length;
        Arrays.sort(pointsInit);

        for (int k = 0; k < n - 1; k++) {
            if (pointsInit[k].compareTo(pointsInit[k + 1]) == 0) {
                throw new java.lang.IllegalArgumentException();
            }
        }

        Stack<LineSegment> segmentsStack = new Stack<LineSegment>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    for (int m = k + 1; m < n; m++) {
                        Point p = pointsInit[i];
                        Point s = pointsInit[j];
                        Point q = pointsInit[k];
                        Point r = pointsInit[m];
                        if (p == null || q == null || s == null || r == null) {
                            throw new java.lang.IllegalArgumentException();
                        }

                        Point[] pointsSorted = { p, s, q, r };
                        Arrays.sort(pointsSorted);

                        if (pointsSorted[0].compareTo(pointsSorted[1]) == 0
                                || pointsSorted[1].compareTo(pointsSorted[2]) == 0
                                || pointsSorted[2].compareTo(pointsSorted[3]) == 0) {
                            throw new java.lang.IllegalArgumentException(
                                    "Points have to be differnet!");
                        }

                        Comparator<Point> pOrder = p.slopeOrder();

                        if (pOrder.compare(s, q) == 0 && pOrder.compare(q, r) == 0) {
                            segmentsStack.push(new LineSegment(pointsSorted[0], pointsSorted[3]));
                        }
                    }
                }
            }
        }

        segments = new LineSegment[segmentsStack.size()];
        int i = 0;
        for (LineSegment value : segmentsStack) {
            segments[i++] = value;
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.clone();
    }
}

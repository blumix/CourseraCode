import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
    private final LineSegment[] segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new java.lang.IllegalArgumentException();
        }
        for (Point p : points) {
            if (p == null) {
                throw new java.lang.IllegalArgumentException();
            }
        }
        Point[] pointsInit = points.clone();

        Stack<LineSegment> segmentsStack = new Stack<LineSegment>();

        Arrays.sort(pointsInit);
        int n = pointsInit.length;


        for (int k = 0; k < n - 1; k++) {
            if (pointsInit[k].compareTo(pointsInit[k + 1]) == 0) {
                throw new java.lang.IllegalArgumentException();
            }
        }

        Point[] pointsCopy = pointsInit.clone();
        for (int i = 0; i < n; i++) {
            if (n < 4) {
                break;
            }
            pointsInit = pointsCopy.clone();
            Point sortingPoint = pointsCopy[i];
            Comparator<Point> slopeComp = sortingPoint.slopeOrder();

            Arrays.sort(pointsInit, sortingPoint.slopeOrder());

            int curCompare = sortingPoint.compareTo(pointsInit[1]);
            int count = 1;
            boolean increasing = true;
            for (int j = 1; j < n - 1; j++) {
                Point curPoint = pointsInit[j];
                Point nextPoint = pointsInit[j + 1];

                if (slopeComp.compare(curPoint, nextPoint) == 0) {
                    if (increasing && (curCompare <= 0)) {
                        count++;
                        curCompare = curPoint.compareTo(nextPoint);
                    }
                    else {
                        increasing = false;
                        count = 1;
                        curCompare = curPoint.compareTo(nextPoint);
                    }
                }
                else {
                    if (count >= 3) {
                        segmentsStack
                                .push(new LineSegment(sortingPoint, curPoint));
                    }
                    count = 1;
                    increasing = true;
                    curCompare = sortingPoint.compareTo(nextPoint);
                }
            }
            if (count >= 3) {
                segmentsStack
                        .push(new LineSegment(sortingPoint, pointsInit[n - 1]));
            }
        }

        segments = new LineSegment[segmentsStack.size()];
        int i = 0;
        for (LineSegment value : segmentsStack) {
            segments[i++] = value;
        }
    }

    /*
    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        // StdDraw.setPenRadius(0.01);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        // StdDraw.setPenRadius();
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    } */

    // the line segments
    public LineSegment[] segments() {
        return segments.clone();
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.length;
    }

}
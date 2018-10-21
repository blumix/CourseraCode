import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {

    private final SET<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new SET<>();
    }

    // unit testing of the methods (optional)
    // public static void main(String[] args) {
    // }

    // is the set empty?
    public boolean isEmpty() {
        return points.size() == 0;
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new java.lang.IllegalArgumentException();
        }
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new java.lang.IllegalArgumentException();
        }
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        // for (Point2D p : points) {
        //     StdDraw.point(p.x(), p.y());
        // }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new java.lang.IllegalArgumentException();
        }
        SET<Point2D> containingPoints = new SET<>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                containingPoints.add(p);
            }
        }
        return containingPoints;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new java.lang.IllegalArgumentException();
        }
        double minValue = Double.POSITIVE_INFINITY;
        Point2D minPoint = null;
        for (Point2D curPoint : points) {
            double dist = curPoint.distanceSquaredTo(p);
            if (dist < minValue) {
                minPoint = curPoint;
                minValue = dist;
            }
        }
        return minPoint;
    }
}
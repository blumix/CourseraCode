import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private static final boolean FIRST_SIDE = true;
    private Node root;

    // construct an empty set of points
    public KdTree() {
        root = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // unit testing of the methods (optional)
    // public static void main(String[] args) {
    // }

    // number of points in the set
    public int size() {
        return root == null ? 0 : root.size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new java.lang.IllegalArgumentException();
        }
        if (root == null) {
            root = new Node(p);
        }
        else {
            root.insert(p, FIRST_SIDE);
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new java.lang.IllegalArgumentException();
        }
        if (root == null) {
            return false;
        }

        return root.contains(p, FIRST_SIDE);
    }

    // draw all points to standard draw
    public void draw() {
        if (root == null) {
            return;
        }
        root.draw(0, 1, 1, 0, FIRST_SIDE);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new java.lang.IllegalArgumentException();
        }

        Stack<Point2D> stack = new Stack<>();
        if (root != null) {
            root.rectangle(stack, rect, FIRST_SIDE, new RectHV(0, 0, 1, 1));
        }
        return stack;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new java.lang.IllegalArgumentException();
        }
        if (root == null) {
            return null;
        }
        return root
                .nearest(p, new RectHV(0, 0, 1, 1), Double.POSITIVE_INFINITY, FIRST_SIDE, root.value);
    }

    private class Node {
        private Node left;
        private Node right;
        private final Point2D value;
        private int size;

        public Node(Point2D val) {
            value = val;
            left = null;
            right = null;
            size = 1;
        }

        public void insert(Point2D p, boolean side) {
            if (p.equals(value)) {
                return;
            }
            if (side ? p.x() <= value.x() : p.y() <= value.y()) {
                if (left == null) {
                    left = new Node(p);
                }
                else {
                    left.insert(p, !side);
                }
            }
            else {
                if (right == null) {
                    right = new Node(p);
                }
                else {
                    right.insert(p, !side);
                }
            }

            size = (left == null ? 0 : left.size) + (right == null ? 0 : right.size) + 1;
        }

        public boolean contains(Point2D p, boolean side) {
            if (value.equals(p)) {
                return true;
            }

            if (side ? p.x() <= value.x() : p.y() <= value.y()) {
                if (left != null) {
                    return left.contains(p, !side);
                }
            }
            else {
                if (right != null) {
                    return right.contains(p, !side);
                }
            }
            return false;
        }

        public void draw(double leftMargin, double rightMargin, double upperMargin,
                         double lowerMargin, boolean side) {
            // StdDraw.setPenRadius(0.005);
            StdDraw.setPenColor(0, 0, 0);
            StdDraw.point(value.x(), value.y());
            // StdDraw.setPenRadius(0.001);
            if (side) {
                // StdDraw.setPenColor(255, 0, 0);
                StdDraw.line(value.x(), upperMargin, value.x(), lowerMargin);
                if (left != null) {
                    left.draw(leftMargin, value.x(), upperMargin, lowerMargin, !side);
                }
                if (right != null) {
                    right.draw(value.x(), rightMargin, upperMargin, lowerMargin, !side);
                }
            }
            else {
                // StdDraw.setPenColor(0, 0, 255);
                StdDraw.line(leftMargin, value.y(), rightMargin, value.y());
                if (right != null) {
                    right.draw(leftMargin, rightMargin, upperMargin, value.y(), !side);
                }
                if (left != null) {
                    left.draw(leftMargin, rightMargin, value.y(), lowerMargin, !side);
                }
            }

        }

        public void rectangle(Stack<Point2D> stack, RectHV rectHV, boolean side, RectHV curRect) {
            if (rectHV.contains(value)) {
                stack.push(value);
            }

            if (side) {
                if (left != null) {
                    RectHV newRectLeft = new RectHV(curRect.xmin(), curRect.ymin(), value.x(),
                                                    curRect.ymax());
                    if (rectHV.intersects(newRectLeft)) {
                        left.rectangle(stack, rectHV, !side, newRectLeft);
                    }
                }
                if (right != null) {
                    RectHV newRectRight = new RectHV(value.x(), curRect.ymin(), curRect.xmax(),
                                                     curRect.ymax());
                    if (rectHV.intersects(newRectRight)) {
                        right.rectangle(stack, rectHV, !side, newRectRight);
                    }
                }
            }
            else {
                if (left != null) {
                    RectHV newRectLeft = new RectHV(curRect.xmin(), curRect.ymin(), curRect.xmax(),
                                                    value.y());
                    if (rectHV.intersects(newRectLeft)) {
                        left.rectangle(stack, rectHV, !side, newRectLeft);
                    }
                }
                if (right != null) {
                    RectHV newRectRight = new RectHV(curRect.xmin(), value.y(), curRect.xmax(),
                                                     curRect.ymax());
                    if (rectHV.intersects(newRectRight)) {
                        right.rectangle(stack, rectHV, !side, newRectRight);
                    }
                }
            }

        }

        public Point2D nearest(Point2D queryPoint, RectHV curRect, double minDistance,
                               boolean side, Point2D point) {
            double distance = queryPoint.distanceSquaredTo(value);
            if (distance < minDistance) {
                minDistance = distance;
                point = value;
            }

            if (side) {
                if (queryPoint.x() < value.x()) {
                    point = checkSubtree(queryPoint, minDistance, side, point, left, curRect.xmin(),
                                         value.x(), curRect.ymin(), curRect.ymax());

                    minDistance = queryPoint.distanceSquaredTo(point);

                    point = checkSubtree(queryPoint, minDistance, side, point, right, value.x(),
                                         curRect.xmax(), curRect.ymin(), curRect.ymax());
                }
                else {
                    point = checkSubtree(queryPoint, minDistance, side, point, right, value.x(),
                                         curRect.xmax(), curRect.ymin(), curRect.ymax());

                    minDistance = queryPoint.distanceSquaredTo(point);

                    point = checkSubtree(queryPoint, minDistance, side, point, left, curRect.xmin(),
                                         value.x(), curRect.ymin(), curRect.ymax());
                }
            }
            else {
                if (queryPoint.y() < value.y()) {
                    point = checkSubtree(queryPoint, minDistance, side, point, left, curRect.xmin(),
                                         curRect.xmax(), curRect.ymin(), value.y());

                    minDistance = queryPoint.distanceSquaredTo(point);

                    point = checkSubtree(queryPoint, minDistance, side, point, right,
                                         curRect.xmin(), curRect.xmax(), value.y(), curRect.ymax());
                }
                else {
                    point = checkSubtree(queryPoint, minDistance, side, point, right,
                                         curRect.xmin(), curRect.xmax(), value.y(), curRect.ymax());

                    minDistance = queryPoint.distanceSquaredTo(point);

                    point = checkSubtree(queryPoint, minDistance, side, point, left, curRect.xmin(),
                                         curRect.xmax(), curRect.ymin(), value.y());
                }
            }
            return point;
        }

        private Point2D checkSubtree(Point2D queryPoint, double minDistance,
                                     boolean side,
                                     Point2D point, Node node, double xmin, double xmax,
                                     double ymin, double ymax) {
            if (node != null) {
                RectHV newRectLeft = new RectHV(xmin, ymin, xmax, ymax);
                if (minDistance < newRectLeft.distanceSquaredTo(queryPoint)) {
                    return point;
                }
                if (newRectLeft.distanceSquaredTo(queryPoint) < minDistance) {
                    return node.nearest(queryPoint, newRectLeft, minDistance, !side, point);
                }
            }
            return point;
        }
    }
}
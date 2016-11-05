import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
    
    private class Node {
        private Point2D point;
        
        private Node left;
        private Node right;
        
        private RectHV canvas;
        
        private boolean horizontal;
        
        public Node(Point2D point, boolean horizontal, RectHV canvas) {
            this.point = point;
            this.horizontal = horizontal;
            this.canvas = canvas;
        }
        
        public double xmin() {
            return horizontal ? canvas.xmin() : point.x();
        }
        
        public double ymin() {
            return horizontal ? point.y() : canvas.ymin();
        }
        
        public double xmax() {
            return horizontal ? canvas.xmax() : point.x();
        }
        
        public double ymax() {
            return horizontal ? point.y() : canvas.ymax();
        }
        
        public RectHV lineRect() {
            return new RectHV(xmin(), ymin(), xmax(), ymax());
        }
        
        public RectHV leftNodeCanvas() {
            if (horizontal) {
                return new RectHV(canvas.xmin(), canvas.ymin(), canvas.xmax(), point.y());
            }
            
            return new RectHV(canvas.xmin(), canvas.ymin(), point.x(), canvas.ymax());
        }
        
        public RectHV rightNodeCanvas() {
            if (horizontal) {
                return new RectHV(canvas.xmin(), point.y(), canvas.xmax(), canvas.ymax());
            }
            
            return new RectHV(point.x(), canvas.ymin(), canvas.xmax(), canvas.ymax());
        }
    }
    
    private Node root;
    private int treeSize;
    
    public KdTree() {
        //
    } // construct an empty set of points 
    
    public boolean isEmpty() {
        return root == null;
    } // is the set empty? 
    
    public int size()  {
        return treeSize;
    } // number of points in the set 
    
    public void insert(Point2D p)  {
        if (p == null) {
            throw new NullPointerException();
        }
        
        if (!contains(p)) {
            insertPoint(p);
        }
    } // add the point to the set (if it is not already in the set)
    
    
    private void insertPoint(Point2D p) {
        treeSize ++;
        
        // Check if the root is set
        if (root == null) {
            RectHV canvas = new RectHV(0, 0, 1, 1);
            Node n = new Node(p, false, canvas);
            n.canvas = canvas;
            
            root = n;
            return;
        }
        
        insertPointInNode(p, root);
    }
    
    private void insertPointInNode(Point2D p, Node n) {
        // if the point to be inserted has a smaller x-coordinate than the point at the root, go left; 
        // otherwise go right
        
        // then at the next level, we use the y-coordinate 
        // (if the point to be inserted has a smaller y-coordinate than the point in the node, go left; 
        // otherwise go right)
             
        // then at the next level the x-coordinate, 
        // and so forth.
        
        boolean horizontal = !n.horizontal;
        if (horizontal) {
            if (p.x() < n.point.x()) {
                // Go left
                if (n.left == null) {
                    Node nn = new Node(p, horizontal, n.leftNodeCanvas());
                    n.left = nn;
                } else {
                    insertPointInNode(p, n.left);
                }
            } else {
                // If it's greater than or equal to
                if (n.right == null) {
                    Node nn = new Node(p, horizontal, n.rightNodeCanvas());
                    n.right = nn;
                } else {
                    insertPointInNode(p, n.right);
                }
            }
        } else {
            // The new node will be vertical
            if (p.y() < n.point.y()) {
                // Go left
                if (n.left == null) {
                    Node nn = new Node(p, horizontal, n.leftNodeCanvas());
                    n.left = nn;
                } else {
                    insertPointInNode(p, n.left);
                }
            } else {
                // If it's greater than or equal to
                if (n.right == null) {
                    Node nn = new Node(p, horizontal, n.rightNodeCanvas());
                    n.right = nn;
                } else {
                    insertPointInNode(p, n.right);
                }
            }
        }
    }
    
    public boolean contains(Point2D p)  {
        // Start at the root and go left or righ
        
        boolean found = false;
        Node node = root;
        
        while (node != null) {
            if (node.point.x() == p.x() && node.point.y() == p.y()) {
                found = true;
                break;
            }
            
            if (node.horizontal) {
                if (p.y() < node.point.y()) {
                    node = node.left;
                } else {
                    node = node.right;
                }
            } else {
                if (p.x() < node.point.x()) {
                    node = node.left;
                } else {
                    node = node.right;
                }
            }
        }
   
        return found;  
    } // does the set contain point p? 
    
    public void draw()  {
        drawNode(root);
    } // draw all points to standard draw 
    
    private void drawNode(Node n) {
        if (n == null) return;
        
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        n.point.draw();
        
        StdDraw.setPenColor(n.horizontal ? StdDraw.BLUE : StdDraw.RED);
        StdDraw.setPenRadius();
        StdDraw.line(n.xmin(), n.ymin(), n.xmax(), n.ymax());
        
        drawNode(n.left);
        drawNode(n.right);
    }
    
    public Iterable<Point2D> range(RectHV rect)  {
        if (rect == null) {
            throw new NullPointerException();
        }
       
        SET<Point2D> intersecting = new SET<Point2D>();
        
        if (root == null) {
            return intersecting;
        }
        
        Stack <Node>stack = new Stack<Node>();
        stack.push(root);
        
        while (!stack.isEmpty()) {
            Node n = stack.pop();
            
            boolean shouldAddLeft = false;
            boolean shouldAddRight = false;
            
            // Check the next node and see if it intersect the point
            // or the splitting line
            if (rect.contains(n.point)) {
                // If the rect contains the node add it
                // and add the two children if present
                
                intersecting.add(n.point);
                shouldAddLeft = true;
                shouldAddRight = true;
            } else if (rect.intersects(n.lineRect())) {
                // Check if it intersects the line
                shouldAddLeft = true;
                shouldAddRight = true;
            } else {
                // Nothing intersects,
                // check if we need to go left or right
                if (n.horizontal) {
                    if (rect.ymax() < n.point.y()) shouldAddLeft = true;
                    else shouldAddRight = true;
                } else {
                    if (rect.xmax() < n.point.x()) shouldAddLeft = true;
                    else shouldAddRight = true;
                }
            }
            
            if (shouldAddLeft && n.left != null) {
                stack.push(n.left);
            }
                
            if (shouldAddRight && n.right != null) {
                stack.push(n.right);
            } 
        }
               
        return intersecting;
    } // all points that are inside the rectangle
    
    public Point2D nearest(Point2D p)  {
        if (p == null) {
            throw new NullPointerException();
        }
        
        if (isEmpty()) {
            return null;
        }
                
        return nearest(root, p, root.point);
    } // a nearest neighbor in the set to point p; null if the set is empty 
    
    private Point2D nearest(Node node, Point2D point, Point2D closest) {
        if (node == null) return closest;
        
        // if the current node is closer than the current best, 
        // then it becomes the current best.
        if (node.point.distanceSquaredTo(point) < closest.distanceSquaredTo(point)) {
            closest = node.point;
        }
        
        // True if the canvas of the node is closer than the nearest distance        
        boolean canContainCloserPoints = node.canvas.distanceSquaredTo(point) < closest.distanceSquaredTo(point);
        
        if (canContainCloserPoints) {
            boolean leftFirst = false;
            if ((node.horizontal && (point.y() < node.point.y())) ||
                 (!node.horizontal && (point.x() < node.point.x()))) {
                leftFirst = true;
            }
           
            Node first = leftFirst ? node.left : node.right;
            Node second = leftFirst ? node.right : node.left;
            
            closest = nearest(first, point, closest);
            closest = nearest(second, point, closest);
        }
        
        return closest;
    }
    
    public static void main(String[] args)  {
        // 
    } // unit testing of the methods (optional) 
}
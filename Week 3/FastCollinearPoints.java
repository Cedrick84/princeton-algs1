
import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class FastCollinearPoints {

    private LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {
        if (points == null || Arrays.asList(points).contains(null)) {
            throw new NullPointerException();
        }

        Point[] copiedPoints = Arrays.copyOf(points, points.length);
        Arrays.sort(copiedPoints);

        for (int i = 0; i < copiedPoints.length - 1; i++) {
            Point p1 = copiedPoints[i];
            Point p2 = copiedPoints[i + 1];

            if (p1.compareTo(p2) == 0) {
                throw new IllegalArgumentException();
            }
        }

        processPoints(copiedPoints);
    } // finds all line segments containing 4 points

    private void processPoints(Point[] points) {
        LineSegment[] hits = new LineSegment[points.length];
        int hitCount = 0;

        // Loop through all points
        for (int i = 0; i < points.length; i++) {
            Point p = points[i];

            // Sort all points based on the slope from point P
            Point[] sortedPoints = Arrays.copyOf(points, points.length);
            Arrays.sort(sortedPoints, p.slopeOrder());

            // Iterate over the sorted array and check if there are 3 or more
            // equal values in a row.
            int start = 0;
            int length = 1;

            for (int j = 0; j < sortedPoints.length - 1; j++) {
                // Save the start location and the amount of equal numbers in the row
                
                boolean lastIteration = j == sortedPoints.length - 2;
                boolean nextIsEqual = p.slopeTo(sortedPoints[start]) == p.slopeTo(sortedPoints[j + 1]);
                
                if (nextIsEqual) {
                    length++;
                }
                
                if (!nextIsEqual || lastIteration) {
                    // As soon as the next number doesn't match the previous number
                    // check if the length >= 3, if so we have a set of collinear points
                    if (length >= 3) {
                        // If we have a match we add those points in an array together with 
                        // point P

                        Point[] hitPoints = Arrays.copyOfRange(sortedPoints, start, start + length);
                        Point[] collinear = new Point[length + 1];

                        for (int k = 0; k < hitPoints.length; k++) {
                            collinear[k] = hitPoints[k];
                        }

                        collinear[length] = p;
                        Arrays.sort(collinear);

                        // We sort the array and convert the segment to a string to make sure we don't
                        // add the same sequence twice
                        LineSegment segment = new LineSegment(collinear[0], collinear[length]);

                        boolean added = false;
                        for (int k = 0; k < hitCount; k++) {
                            LineSegment addedSegment = hits[k];
                            if (addedSegment.toString().equals(segment.toString())) {
                                added = true;
                                break;
                            }
                        }

                        if (!added) {
                            hits[hitCount] = segment;
                            hitCount++;
                        }
                    }

                    start = j + 1;
                    length = 1;
                }
            }
        }

        segments = new LineSegment[hitCount];
        for (int i = 0; i < hitCount; i++) {
            segments[i] = hits[i];
        }
    }

    public int numberOfSegments() {
        return segments.length;
    } // the number of line segments

    public LineSegment[] segments() {
        return Arrays.copyOf(segments, segments.length);
    } // the line segments

    public static void main(String[] args) {;
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
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

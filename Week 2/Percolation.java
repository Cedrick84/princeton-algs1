import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    
    private WeightedQuickUnionUF quickFind;
    private WeightedQuickUnionUF fullQuickFind;
    
    private boolean[][] grid;
    private int gridSize;
    
    private int top = 0;
    private int bottom;

  public Percolation(int n) {
      if (n < 1) {
          throw new IllegalArgumentException();
      }
      
      gridSize = n;
      grid = new boolean[n][n];
      
      bottom = n * n + 1;
      
      quickFind = new WeightedQuickUnionUF(n * n + 2);
      fullQuickFind = new WeightedQuickUnionUF(n * n + 1);
  }

  public void open(int i, int j)   {
      checkBounds(i, j);
      
      boolean alreadyOpen = grid[i-1][j-1];
      if (alreadyOpen) {
          return;
      }
      
      grid[i-1][j-1] = true;
      
      boolean firstRow = i == 1;
      boolean lastRow = i == gridSize;
      
      if (firstRow) {
          quickFind.union(indexFor(i, j), top);
          fullQuickFind.union(indexFor(i, j), top);
      } else if (lastRow) {
          quickFind.union(indexFor(i, j), bottom);
      }
      
      boolean topOpen = j > 1 && isOpen(i, j - 1);
      if (topOpen) {
          quickFind.union(indexFor(i, j), indexFor(i, j - 1));
          fullQuickFind.union(indexFor(i, j), indexFor(i, j - 1));
      }
      
      boolean bottomOpen = j < gridSize && isOpen(i, j + 1);
      if (bottomOpen) {
          quickFind.union(indexFor(i, j), indexFor(i, j + 1));
          fullQuickFind.union(indexFor(i, j), indexFor(i, j + 1));
      }
      
      boolean leftOpen = i > 1 && isOpen(i - 1, j);
      if (leftOpen) {
          quickFind.union(indexFor(i, j), indexFor(i - 1, j));
          fullQuickFind.union(indexFor(i, j), indexFor(i - 1, j));   
      }
      
      boolean rightOpen = i < gridSize && isOpen(i + 1, j);
      if (rightOpen) {
          quickFind.union(indexFor(i, j), indexFor(i + 1, j));
          fullQuickFind.union(indexFor(i, j), indexFor(i + 1, j));
      }
  }
  
  public boolean isOpen(int i, int j) {
      checkBounds(i, j);
      return grid[i - 1][j - 1];
  }
  
  public boolean isFull(int i, int j) {
      checkBounds(i, j);
      
      if (!isOpen(i, j)) {
          return false;
      } else if (i == 1) {
          return true;
      }
      
      return fullQuickFind.connected(top, indexFor(i, j));
  }

  public boolean percolates() {
      if (gridSize == 1) {
            return grid[0][0];
      }
    
      return quickFind.connected(top, bottom);
  }
  
  private int indexFor(int i, int j) {
      return (i - 1) * gridSize + j;
  }
  
  private void checkBounds(int i, int j) {
      if (i < 0 || j < 0 || i > gridSize || j > gridSize) {
          throw new IndexOutOfBoundsException();
      }
  }
  
  public static void main(String[] args) {
       Percolation percolation = new Percolation(5);
      
        percolation.open(1, 2);
        percolation.open(2, 2);
        percolation.open(3, 2);
        percolation.open(4, 2);
        percolation.open(5, 2);
        
        System.out.println(percolation.percolates());
        
        percolation.open(1, 5);
        percolation.open(2, 3);
        
        
        System.out.println(percolation.isFull(3, 3));
        System.out.println(percolation.isFull(2, 3));
        System.out.println(percolation.isFull(5, 5));
  }
}
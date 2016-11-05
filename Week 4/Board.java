import java.util.Arrays;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    
    private int[][] blocks;
    
    public Board(int[][] blocks) {
        int[][] copy = new int[blocks.length][blocks.length];
        
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                copy[i][j] = blocks[i][j];
            }
        }
        
        this.blocks = copy;
        
    }// construct a board from an n-by-n array of blocks
                                           // (where blocks[i][j] = block in row i, column j)
    public int dimension() {
        return blocks.length;
    }// board dimension n
    
    public int hamming() {
       int outOfPlace = 0;
       
       for (int i = 0; i < blocks.length; i++) {
           int[] row = blocks[i];
           
           for (int j = 0; j < row.length; j++) {
               int value = row[j];
               if (value == 0) continue;
               
               int inPlaceValue = i * dimension() + j + 1;
               
               if (value != inPlaceValue) {
                   outOfPlace++;
               }
           }
       }
       
        return outOfPlace;
    }// number of blocks out of place
    
    public int manhattan() {
        int manhattan = 0;
        
        for (int i = 0; i < blocks.length; i++) {
           int[] row = blocks[i];
           
           for (int j = 0; j < row.length; j++) {
               int value = row[j];
               if (value == 0) continue;
               
               int inPlaceRow = rowForItem(value);
               int inPlaceColumn = columnForItem(value);
               
               int currentRow = i + 1;
               int currentColumn = j + 1;
               
               int rowDifference = Math.abs(currentRow - inPlaceRow);
               int columnDifference = Math.abs(currentColumn - inPlaceColumn);
               int steps = rowDifference + columnDifference;
               manhattan += steps;
               
               // System.out.println("value "+value+" = ["+currentRow+"]["+currentColumn+"] and should be in ["+inPlaceRow+"]["+inPlaceColumn+"] -> "+steps+" steps");
           }
       }
        
        return manhattan;
    }// sum of Manhattan distances between blocks and goal
    
    
    private int columnForItem(int item) {
        if (item == 0) return 0; 
        
        int d = dimension();
        int mod = item % d;
        
        if (mod == 0) {
            mod = d;
        }
              
        return mod;
    }
    
    private int rowForItem(int item) {
        return (int) Math.ceil((double) item / dimension());
    }
    
    public boolean isGoal() {
        return manhattan() == 0;
    }// is this board the goal board?
    
    public Board twin() {
        int firstIndex = getRandomIndex();
        int secondIndex = getRandomIndex();
        
        while (firstIndex == secondIndex) {
           secondIndex = getRandomIndex();
        }
        
        int[][] copy = boardCopy();
        int temp = blocks[rowForItem(firstIndex) -1][columnForItem(firstIndex) -1];
        copy[rowForItem(firstIndex) -1][columnForItem(firstIndex) -1] = blocks[rowForItem(secondIndex) -1][columnForItem(secondIndex) -1];
        copy[rowForItem(secondIndex) -1][columnForItem(secondIndex) -1] = temp;
        
       return new Board(copy); 
    }// a board that is obtained by exchanging any pair of blocks
    
    private int getRandomIndex() {
        int index = 0;
        
        while (index < 1) {
           int size = dimension() * dimension();
           int randomItem = StdRandom.uniform(size); 
           
           if (randomItem == 0) continue;
           if (blocks[rowForItem(randomItem) -1][columnForItem(randomItem) -1] != 0){
               index = randomItem;
           }
        }
        
        return index;
    }
    
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        
        Board that = (Board) y;
        
        boolean equal = true;
        
        for (int i = 0; i < that.blocks.length; i++) {
           int[] row = that.blocks[i];
           
           for (int j = 0; j < row.length; j++) {
               if(that.blocks.length != blocks.length ||
                  that.blocks[i].length != blocks[i].length ||
                  that.blocks[i][j] != blocks[i][j]) {
                   equal = false;
                   break;
               }
           }
        }
        
        return equal;
    }// does this board equal y?
    
    public Iterable<Board> neighbors() {
        Stack<Board> boards = new Stack<Board>();
        
        outerloop:
        for (int i = 0; i < blocks.length; i++) {
           int[] row = blocks[i];
           
           for (int j = 0; j < row.length; j++) {
               int value = row[j];
               
               if (value == 0) {
                  boolean canGoUp = i > 0;
                  boolean canGoRight = j < row.length - 1;
                  boolean canGoDown = i < dimension() - 1;
                  boolean canGoLeft = j > 0;
                   
                  if (canGoUp) {
                      boards.push(boardByMovingUp(i, j));
                  }
                  
                  if (canGoRight) {
                      boards.push(boardByMovingRight(i, j));
                  }
                  
                  if (canGoDown) {
                      boards.push(boardByMovingDown(i, j));
                  }
                  
                  if (canGoLeft) {
                      boards.push(boardByMovingLeft(i, j));
                  }
                   
                   break outerloop;
               }
           } 
       }
        
        return boards;
    }// all neighboring boards
    
    private int[][] boardCopy() {
        int[][] copy = new int[dimension()][dimension()];
        
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                copy[i][j] = blocks[i][j];
            }
        }
            
        return copy;
    }
    
    private Board boardByMovingUp(int row, int column) {
        int[][] copy = boardCopy();
        
         copy[row][column] = blocks[row -1][column];
         copy[row -1][column] = 0;
         
         return new Board(copy);
    }
    
    private Board boardByMovingRight(int row, int column) {
        int[][] copy = boardCopy();
        
         copy[row][column] = blocks[row][column +1];
         copy[row][column +1] = 0;
         
         return new Board(copy);
    }
    
    private Board boardByMovingDown(int row, int column) {
        int[][] copy = boardCopy();
        
         copy[row][column] = blocks[row+1][column];
         copy[row +1][column] = 0;
         
         return new Board(copy);
    }
    
    private Board boardByMovingLeft(int row, int column) {
        int[][] copy = boardCopy();
        
         copy[row][column] = blocks[row][column -1];
         copy[row][column -1] = 0;
         
         return new Board(copy);
    }
    
    public String toString() {
       String str = dimension() + "\n";
   
       for (int i = 0; i < blocks.length; i++) {
           int[] row = blocks[i];
           
           for (int j = 0; j < row.length; j++) {
               str += "  "+ row[j];
           }
           
           str += "\n";
       }
       
       return str;
    
    }// string representation of this board (in the output format specified below)

    public static void main(String[] args) {
      
        int[][] blocks = new int[][]{
            { 1, 2, 3 },
            { 4, 0, 5 },
            { 6, 7, 8 }};
        
        Board b = new Board(blocks);
        System.out.println(b.hamming());
        System.out.println(b.manhattan());
        System.out.println(b.twin().toString());
        
    }// unit tests (not graded)
}
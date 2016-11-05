import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import java.util.HashSet;

public class Solver {
    
    private HashSet<String> added = new HashSet<String>();
    private HashSet<String> twinAdded = new HashSet<String>();
    
    private Stack<Board> steps = new Stack<Board>();
    private boolean solvable = true;
    
    public Solver(Board initial) {
 
        // To apply the fact, run the A* algorithm on two puzzle instances one with the initial board and 
        // one with the initial board modified by swapping a pair of blocks in lockstep
        Board twin = initial.twin();
        
        // First, insert the initial search node 
        // (the initial board, 0 moves, and a null previous search node) into a priority queue. 
        MinPQ<SearchNode> queue = new MinPQ<SearchNode>();
        MinPQ<SearchNode> twinQueue = new MinPQ<SearchNode>();
        
        queue.insert(new SearchNode(null, initial));
        twinQueue.insert(new SearchNode(null, twin));
         
        SearchNode goal = null;
        SearchNode twinGoal = null;
        
        while (goal == null && twinGoal == null) {
            // Then, delete from the priority queue the search node with the minimum priority, 
            SearchNode node = queue.delMin();
            SearchNode twinNode = twinQueue.delMin();
            
            if (node.board.isGoal()) {
                goal = node;
                break;
            }
            
            if (twinNode.board.isGoal()) {
                twinGoal = twinNode;
                solvable = false;
                break;
            }
            
            // and insert onto the priority queue all neighboring search nodes 
            // (those that can be reached in one move from the dequeued search node). 
            for (Board neighbor : node.board.neighbors()) {
                String str = neighbor.toString();
                if (!added.contains(str)) {
                    queue.insert(new SearchNode(node, neighbor));
                    added.add(str);
                }
            }
            
            for (Board neighbor : twinNode.board.neighbors()) {
                String str = neighbor.toString();
                if (!twinAdded.contains(str)) {
                    twinQueue.insert(new SearchNode(twinNode, neighbor));
                    twinAdded.add(str);
                }
            }

            // Repeat this procedure until the search node dequeued corresponds to a goal board. 
        }
         
         
         // The success of this approach hinges on the choice of priority function for a search node
        while (goal != null) {
            steps.push(goal.board());
            goal = goal.parent();
        }
         
         
    } // find a solution to the initial board (using the A* algorithm)
    
    public boolean isSolvable() {
        return solvable;
    } // is the initial board solvable?
    
    public int moves() {
        return isSolvable() ? steps.size() -1 : -1;
    } // min number of moves to solve initial board; -1 if unsolvable
    
    public Iterable<Board> solution() {
        return isSolvable() ? steps : null;
    } // sequence of boards in a shortest solution; null if unsolvable
    
    public static void main(String[] args) {

    } // solve a slider puzzle (given below)
    
    // We define a search node of the game to be a board, 
    // the number of moves made to reach the board, 
    // and the previous search node
    
     private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moves = 0;
        private SearchNode parent;

        SearchNode(SearchNode parent, Board board) {
            this.parent = parent;
            this.board = board;
            
            if (parent != null) {
                moves = parent.moves() + 1;
            }   
        }
        
        public int compareTo(SearchNode that) {
            return (moves + board.manhattan()) - (that.moves() + that.board().manhattan());
        }
        
        public int moves() {
            return moves;
        }
        
        public Board board() {
            return board;
        }
        
        public SearchNode parent() {
            return parent;
        }
    }
}
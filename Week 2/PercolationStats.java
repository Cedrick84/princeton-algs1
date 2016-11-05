import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    
    private int trials;
    private double[] fractions;
    
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) {
            throw new IllegalArgumentException("inputs should be higher than 1");
        }
        
        fractions = new double[trials];
        this.trials = trials;
        
        for (int k = 0; k < trials; k++) {    
            Percolation percolation = new Percolation(n);
            int opened = 0;
            
            while (!percolation.percolates()) {
                int i = StdRandom.uniform(1, n + 1);
                int j = StdRandom.uniform(1, n + 1);
                
                if (!percolation.isOpen(i, j)) {
                    percolation.open(i, j);
                    opened++;
                }
            }
            
            double fraction = (double) opened / (n * n);
            fractions[k] = fraction;
        }
    }
       
    public double mean() {
        return StdStats.mean(fractions);
    }
    
    public double stddev() {
         return StdStats.stddev(fractions);
    }
    
    public double confidenceLo()  {
        return mean() - ((1.96 * stddev()) / Math.sqrt(trials));
    }
    public double confidenceHi()  {
        return mean() + ((1.96 * stddev()) / Math.sqrt(trials));
    }

   public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, trials);

        String conf = stats.confidenceLo() + ", " + stats.confidenceHi();
        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println("95% confidence interval = " + conf);
    }
}
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final int trials;
    private final int n;
    private double[] experimentResults;
    private final double meanValue;
    private final double stddevValue;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n,
                            int trials) {
        if (n <= 0 || trials <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        this.trials = trials;
        this.n = n;
        experimentResults = new double[trials];

        runExperiments();

        meanValue = StdStats.mean(experimentResults);
        stddevValue = StdStats.stddev(experimentResults);
    }

    private void runExperiments() {
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);

            while (!p.percolates()) {
                p.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
            }
            experimentResults[i] = (double) p.numberOfOpenSites() / (n * n);
        }
    }

    // test client (described below)
    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        Stopwatch s = new Stopwatch();
        PercolationStats stats = new PercolationStats(n, trials);

        StdOut.println(String.format(
                "elapsed time %f\nmean                    = %f\nstddev                  = %f\n95%% confidence interval = [%f, %f]",
                s.elapsedTime(), stats.mean(),
                stats.stddev(), stats.confidenceLo(), stats.confidenceHi()));

    }

    // sample mean of percolation threshold
    public double mean() {
        return meanValue;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddevValue;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - CONFIDENCE_95 * stddev() / java.lang.Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONFIDENCE_95 * stddev() / java.lang.Math.sqrt(trials);
    }
}
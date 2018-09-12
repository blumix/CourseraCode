import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private final int topImagineSite;
    private final int bottomImagineSite;
    private final WeightedQuickUnionUF base; // to know if system percolates
    private final WeightedQuickUnionUF baseFull; //  to know if cell is full
    private int openNumber;
    private boolean[] isOpenArray;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        this.n = n;
        openNumber = 0;
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        base = new WeightedQuickUnionUF(
                this.n * this.n + 2); // + 2 for imaginary sites. they have numbers n*n and n*n+1
        baseFull = new WeightedQuickUnionUF(
                this.n * this.n + 1); // + 1 for imaginary top site. it has number n*n
        isOpenArray = new boolean[this.n * this.n + 2];

        topImagineSite = n * n;
        bottomImagineSite = n * n + 1;

        isOpenArray[topImagineSite] = true;
        isOpenArray[bottomImagineSite] = true;

    }

    // test client (optional)
    public static void main(String[] args) {
        // Percolation p = new Percolation(5);
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        checkRowCol(row, col);

        int index = getIndex(row, col);
        if (isOpenArray[index]) {
            return;
        }

        isOpenArray[index] = true;
        openNumber++;

        if (row > 1) {
            int i = getIndex(row - 1, col);
            if (isOpenArray[i]) {
                base.union(i, index);
                baseFull.union(i, index);
            }
        }
        else {
            base.union(topImagineSite, index);
            baseFull.union(topImagineSite, index);
        }
        if (row < n) {
            int i = getIndex(row + 1, col);
            if (isOpenArray[i]) {
                base.union(i, index);
                baseFull.union(i, index);
            }
        }
        else {
            base.union(bottomImagineSite, index);
        }
        if (col > 1) {
            int i = getIndex(row, col - 1);
            if (isOpenArray[i]) {
                base.union(i, index);
                baseFull.union(i, index);
            }
        }
        if (col < n) {
            int i = getIndex(row, col + 1);
            if (isOpenArray[i]) {
                base.union(i, index);
                baseFull.union(i, index);
            }
        }
    }

    private void checkRowCol(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException();
        }
    }

    private int getIndex(int row, int col) {
        return (row - 1) * n + (col - 1);
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkRowCol(row, col);

        return isOpenArray[getIndex(row, col)];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        checkRowCol(row, col);

        return baseFull.connected(getIndex(row, col), topImagineSite);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return openNumber;
    }

    // does the system percolate?
    public boolean percolates() {
        return base.connected(topImagineSite, bottomImagineSite);
    }
}
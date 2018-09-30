import edu.princeton.cs.algs4.Queue;

public class Board {
    private final int[][] blocks;
    private final int manhattanDst;
    private final int hammingDst;
    private final boolean isGoalVal;
    private final int zeroI;
    private final int zeroJ;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        this.blocks = blocks.clone();
        for (int i = 0; i < this.blocks.length; i++) {
            this.blocks[i] = this.blocks[i].clone();
        }

        int manhattanDst1 = 0;
        int hammingDst1 = 0;
        int zeroI1 = 0;
        int zeroJ1 = 0;
        boolean isGoalVal1 = true;

        int n = this.blocks.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] != 0 && blocks[i][j] != i * n + j + 1) {
                    hammingDst1++;
                    manhattanDst1 += java.lang.Math.abs((blocks[i][j] - 1) % n - j) +
                            java.lang.Math.abs((blocks[i][j] - 1) / n - i);
                    isGoalVal1 = false;
                }
                else {
                    if (blocks[i][j] == 0) {
                        zeroI1 = i;
                        zeroJ1 = j;
                    }
                }
            }
        }

        hammingDst = hammingDst1;
        manhattanDst = manhattanDst1;
        isGoalVal = isGoalVal1;
        zeroI = zeroI1;
        zeroJ = zeroJ1;
    }

    // unit tests (not graded)
    // public static void main(String[] args) {
    // }

    // board dimension n
    public int dimension() {
        return blocks.length;
    }

    // number of blocks out of place
    public int hamming() {
        return hammingDst;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return manhattanDst;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return isGoalVal;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] ret;
        if (blocks[0][1] != 0 && blocks[0][0] != 0) {
            ret = swap(0, 0, 0, 1);
        }
        else {
            ret = swap(1, 0, 1, 1);
        }
        assert ret != null;
        return new Board(ret);
    }

    private int[][] swap(int fromI, int fromJ, int toI, int toJ) {
        int n = blocks.length;
        if (fromI >= n || fromJ >= n || toI >= n || toJ >= n ||
                fromI < 0 || fromJ < 0 || toI < 0 || toJ < 0) {
            return null;
        }

        int[][] ret = blocks.clone();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = ret[i].clone();
        }
        ret[fromI][fromJ] = blocks[toI][toJ];
        ret[toI][toJ] = blocks[fromI][fromJ];
        return ret;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || y.getClass() != this.getClass()) {
            return false;
        }
        Board other = (Board) y;

        if (this.blocks.length != other.blocks.length) {
            return false;
        }

        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                if (blocks[i][j] != other.blocks[i][j]) {
                    return false;
                }
            }
        }
        return true;

    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(blocks.length + "\n");
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                ret.append(String.format("%2d ", blocks[i][j]));
            }
            ret.append("\n");
        }
        return ret.toString();
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> ret = new Queue<Board>();
        int[][] neighbor;

        neighbor = swap(zeroI, zeroJ, zeroI + 1, zeroJ);
        if (neighbor != null) {
            ret.enqueue(new Board(neighbor));
        }
        neighbor = swap(zeroI, zeroJ, zeroI, zeroJ + 1);
        if (neighbor != null) {
            ret.enqueue(new Board(neighbor));
        }
        neighbor = swap(zeroI, zeroJ, zeroI - 1, zeroJ);
        if (neighbor != null) {
            ret.enqueue(new Board(neighbor));
        }
        neighbor = swap(zeroI, zeroJ, zeroI, zeroJ - 1);
        if (neighbor != null) {
            ret.enqueue(new Board(neighbor));
        }
        return ret;
    }
}
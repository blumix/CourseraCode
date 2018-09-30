import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private final boolean hasSolution;
    private final int movesNum;
    private Stack<Board> solution;

    // find a solutionNode to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new java.lang.IllegalArgumentException();
        }
        MinPQ<Node> pq = new MinPQ<>();
        Node solutionNode;

        Node first = new Node(initial, null, 0, false);
        Node twin = new Node(initial.twin(), null, 0, true);
        pq.insert(first);
        pq.insert(twin);

        while (true) {
            if (pq.min().getBoard().isGoal()) {
                if (!pq.min().twin) {
                    hasSolution = true;
                    solutionNode = pq.min();
                    movesNum = solutionNode.move;
                    solution = new Stack<>();
                    while (solutionNode != null) {
                        solution.push(solutionNode.getBoard());
                        solutionNode = solutionNode.getPrev();
                    }
                    return;
                }
                else {
                    hasSolution = false;
                    movesNum = -1;
                    return;
                }
            }
            iter(pq);
        }
    }

    // returns true if we should stop
    private void iter(MinPQ<Node> queue) {
        Node cur;
        cur = queue.delMin();
        for (Board value : cur.getBoard().neighbors()) {
            if (cur.prev == null || !value.equals(cur.prev.getBoard())) {
                queue.insert(new Node(value, cur, cur.move + 1, cur.twin));
            }
        }
    }

    public static void main(String[] args) {

        // create initial board from file
        // In in = new In(args[0]);
        In in = new In("puzzle01.txt");
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solutionNode to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return hasSolution;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return movesNum;
    }

    // sequence of boards in a shortest solutionNode; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    private class Node implements Comparable<Node> {
        private final boolean twin;
        private final int move;
        private final Board board;
        private final Node prev;
        private final int manhattan;

        public Node(Board current, Node prev, int move, boolean twin) {
            this.move = move;
            board = current;
            this.prev = prev;
            this.twin = twin;
            this.manhattan = board.manhattan();
        }

        public Node getPrev() {
            return prev;
        }

        @Override
        public int compareTo(Node other) {
            return this.rank() == other.rank() ?
                   -Integer.compare(move, other.move) :
                   Integer.compare(this.rank(), other.rank());
        }

        public int rank() {
            return move + manhattan;
        }

        public Board getBoard() {
            return board;
        }
    }
}
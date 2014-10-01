package offset.group2;

import java.util.*;

import offset.sim.Pair;
import offset.sim.Point;
import offset.sim.movePair;

/**
 * Fields of base class:
 *
 * int id - the player number
 * Pair pr - pr.p, pr.q those are good paramters
 */
public class Player extends offset.sim.Player {
	
    private static final int size = 32;

    private static final long ONE_SECOND = 1000000000;      /* a second in nanoseconds */
    private static final long ABORT_TIME = 300000000;       /* buffer time to abort any search from to ensure we don't go over time limit */
    private static final int MAX_DEPTH = 16;                /* max depth we would want to reach for alpha beta search */
    private static final movePair FORFEIT_MOVE = new movePair(false, null, null);     /* return this when the agent can't move */
    private final MaxActionValueComparator maxComparator = new MaxActionValueComparator(); /* used for sorting ActionValues in descending order */
    private final MinActionValueComparator minComparator = new MinActionValueComparator(); /* used for sorting ActionValues in ascending order */

    private long moveTimeLimit;                             /* time to select a move in ns */
    private SearchNode<Gomoku> recentStates;                /* essentially a linked list of recent moves in game */
    private GomokuPlayer dummyPlayer;                       /* a dummy player used in the alpha beta search to invoke opponent's moves */
    private HashMap<Gomoku, Double> stateEvaluations;       /* a transition table of Gomoku state to heuristic score */
    private HashMap<Integer, HashMap<Integer, Double>> openStreakPoints; /* map of chain openings [0, 1, or 2] to a map of streak length to point value */

	public Player(Pair prin, int idin) {
		super(prin, idin);


	}

	public void init() {

	}

	public movePair move(Point[] grid, Pair pr, Pair pr0, ArrayList<ArrayList> history) {
		movePair movepr = new movePair();
		movepr.move = false;

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				for (int i_pr=0; i_pr < size; i_pr++) {
					for (int j_pr=0; j_pr < size; j_pr++) {
						movepr.src = grid[i * size + j];
						movepr.target = grid[size*i_pr + j_pr];

						if (validateMove(movepr, pr)) {


							// movepr.move = true;
							// return movepr;
						}
					}
				}
			}
		}

		return movepr;
	}

	public int evaluateBoard(Point[] grid, Pair pr, Pair pr0) {
		return (myPossibleMoves(grid, pr).length - yourPossibleMoves(grid, pr0).length);
	}

	private movePair[] myPossibleMoves(Point[] grid, Pair pr) {
		ArrayList<movePair> moves = new ArrayList<movePair>();
		movePair movepr = new movePair();

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				for (int i_pr=0; i_pr < size; i_pr++) {
					for (int j_pr=0; j_pr < size; j_pr++) {
						movepr.src = grid[i*size + j];
						movepr.target = grid[size*i_pr + j_pr];
						if (validateMove(movepr, pr)) {
							moves.add(movepr);
						}
					}
				}
			}
		}

		movePair[] allMoves = moves.toArray(new movePair[moves.size()]);
		return allMoves;

	}

	private movePair[] yourPossibleMoves(Point[] grid, Pair pr0) {
		ArrayList<movePair> moves = new ArrayList<movePair>();
		movePair movepr = new movePair();

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				for (int i_pr=0; i_pr < size; i_pr++) {
					for (int j_pr=0; j_pr < size; j_pr++) {
						movepr.src = grid[i*size + j];
						movepr.target = grid[size*i_pr + j_pr];
						if (validateMove(movepr, pr0)) {
							moves.add(movepr);
						}
					}
				}
			}
		}

		movePair[] allMoves = moves.toArray(new movePair[moves.size()]);
		return allMoves;
	}

	boolean validateMove(movePair movepr, Pair pr) {	
        if (movepr == null || movepr.src == null || movepr.target == null) return false;

    	Point src = movepr.src;
    	Point target = movepr.target;
    	boolean rightposition = false;

    	if (Math.abs(target.x-src.x)==Math.abs(pr.p) && Math.abs(target.y-src.y)==Math.abs(pr.q)) {
    		rightposition = true;
    	}
    	if (Math.abs(target.x-src.x)==Math.abs(pr.p) && Math.abs(target.y-src.y)==Math.abs(pr.q)) {
    		rightposition = true;
    	}
        if (rightposition && src.value == target.value && src.value >0) {
        	return true;
        }
        else {
        	return false;
        }
    }

    public String toString() {
        return playerName() + " // (" + pr.p + "," + pr.q + ")";
    }

    public String playerName() {
        return "Player " + id;
    }

    /*****
     *
     * HELPFUL PRIVATE CLASSES !!!!!
     *
     */

    /**
     * Helper class used in A-B search to keep track of the value of
     * various moves across various search levels / depths
     */
    private class ActionValue implements Comparable<ActionValue> {
        public double v;
        public movePair move;

        /**
         * Constructs an ActionValue with given value and move
         */
        public ActionValue(double v, movePair move) {
            this.v = v;
            this.move = move;
        }

        /**
         * To string just dumps the value, row, and col.
         */
        public String toString() {
            return v + " --- " move;
        }

        /**
         * Compares the value portions of the two ActionValues
         */
        public int compareTo(ActionValue other) {
            Double V = new Double(v);
            Double otherV = new Double(other.v);
            return V.compareTo(otherV);
        }
    }

    /**
     * Little helper class for sorting that compares two ActionValues by value
     * in descending order.
     */
    private class MaxActionValueComparator implements Comparator<ActionValue> {
        public int compare(ActionValue o1, ActionValue o2) {
            return -1 * o1.compareTo(o2);
        }

        public boolean equals(Object obj) {
            return this == obj;
        }
    }

    /**
     * Little helper class for sorting that compares two ActionValues by value
     * in the traditional (ascending) order.
     */
    private class MinActionValueComparator implements Comparator<ActionValue> {
        public int compare(ActionValue o1, ActionValue o2) {
            return o1.compareTo(o2);
        }

        public boolean equals(Object obj) {
            return this == obj;
        }
    }
}
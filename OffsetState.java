
package offset.group2;

import offset.sim.Pair;
import offset.sim.Point;
import offset.sim.movePair;

import java.util.*;

public class OffsetState {
    
    public static final int size = 32;

    public Point[] grid;
    public Pair pr;
    public Pair pr0;

    public OffsetState(Point[] grid, Pair pair1, Pair pair2) {
        this.grid = grid;
        this.pr = pair1;
        this.pr0 = pair2;
    }

    public void performMove(movePair movepr, int playerID) {
        if (movepr.move) {
            Point src = grid[movepr.src.x * size + movepr.src.y];
            Point target = grid[movepr.target.x * size + movepr.target.y];
            target.value = target.value+src.value;
            src.value = 0;
            target.owner = playerID;
            src.owner = -1;
        }
    }

    public int gameScore(int playerID) {
        int score = 0;

        for (int i = 0; i < 32; i++) {
            for (int j = 0; j < 32; j++) {
                Point p = grid[i * 32 + j];
                if (p.owner == playerID) {
                    score += p.value;
                }
            }
        }

        return score;
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public String toString() {
        return grid.toString() + pr.p + pr.q + pr0.p + pr0.q;
    }

    public void performMove(int[] move, int playerID) {
        performMove(movePairFromIntList(move), playerID);
    }

    public int[] intListFromMovePair(movePair mp) {
        return new int[]{mp.src.x, mp.src.y, mp.target.x, mp.target.y};
    }

    public movePair movePairFromIntList(int[] move) {
        movePair movepr = new movePair();
        movepr.move = false;
        movepr.src = grid[move[0] * size + move[1]];
        movepr.target = grid[size * move[2] + move[3]];
        return movepr;
    }

    public boolean validateMove(movePair movepr, Pair pr) {    
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

    public OffsetState cloneState() {
        Point[] newGrid = new Point[grid.length];
        System.arraycopy(grid, 0, newGrid, 0, grid.length);

        return new OffsetState(newGrid, new Pair(pr.p, pr.q), new Pair(pr0.p, pr0.q));
    }

    public ArrayList<int[]> validMoves(int playerID) {
        ArrayList<int[]> moves = new ArrayList<int[]>();

        Pair relevantPR = pr;
        if (playerID > 0) {
            relevantPR = pr0;
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                 for (int i_pr=0; i_pr < size; i_pr++) {
                     for (int j_pr=0; j_pr < size; j_pr++) {
                        movePair movepr = new movePair();
                        movepr.src = grid[i * size + j];
                        movepr.target = grid[size*i_pr + j_pr];

                        if (validateMove(movepr, relevantPR)) {
                            int[] m = new int[]{movepr.src.x, movepr.src.y, movepr.target.x, movepr.target.y};
                            moves.add(m);
                        }
                    }
                }
            }
        }

        return moves;
    }

    public boolean hasValidMoves(int playerID) {
        return validMoves(playerID).size() > 0;
    }

}
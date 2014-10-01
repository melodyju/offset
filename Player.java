package offset.group2;

import java.util.*;

import offset.sim.Pair;
import offset.sim.Point;
import offset.sim.movePair;

public class Player extends offset.sim.Player {
	
	int size = 32;

	public Player(Pair prin, int idin) {
		super(prin, idin);
		// TODO Auto-generated constructor stub
	}

	public void init() {

	}

	public movePair move(Point[] grid, Pair pr, Pair pr0, ArrayList<ArrayList> history) {
		//System.out.println(history.size());
		movePair movepr = new movePair();
		movepr.move = false;

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				for (int i_pr=0; i_pr < size; i_pr++) {
					for (int j_pr=0; j_pr < size; j_pr++) {

						movepr.src = grid[i*size + j];
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
}
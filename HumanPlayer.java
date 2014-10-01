import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.*;

import offset.sim.Pair;
import offset.sim.Point;
import offset.sim.movePair;

/**
 * Extension of Player meant to be controlled by a human (or anything via stdin, really).
 */
public class HumanPlayer extends offset.sim.Player {

    private BufferedReader moveReader;

    /**
     * Constructs a human player (reading moves from stdin) with given player num and move symbol.
     */
    public HumanPlayer(Pair prin, int idin) {
        super(prin, idin);

        moveReader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Getting the next move involes reading a move from stdin and ensuring it is a valid selection.
     * 
     * Move should be in the format 'row col' where row and col are integers and 0-indexed.
     */
    public movePair move(Point[] grid, Pair pr, Pair pr0, ArrayList<ArrayList> history) {
        System.out.println(playerName() + ", please enter your next move.");
        String moveString;

        movePair mp;

        try {
            while (!validateMove(mp, pr)) {
                moveString = moveReader.readLine();
                String[] moves = moveString.split(" ");

                try {
                    int sx = Integer.parseInt(moves[0]);
                    int sy = Integer.parseInt(moves[1]);
                    int tx = Integer.parseInt(moves[2]);
                    int ty = Integer.parseInt(moves[3]);
                   
                    mp.src = grid[sx * size + sy];
                    mp.target = grid[tx * size + ty];
                }
                catch (Exception e) {
                    System.out.println("Format is <src row> <src col> <target row> <target col>, both integers!");
                    continue;
                }

                if (!validateMove(mp, pr)) {
                    System.out.println("That's not a valid move! Try again.");
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return mp;
    }

}
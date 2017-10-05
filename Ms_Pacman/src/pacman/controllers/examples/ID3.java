package pacman.controllers.examples;

import java.util.Random;

import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ID3 extends Controller<MOVE> {
	private Random rnd=new Random();
	private MOVE[] allMoves=MOVE.values();
	@Override
	public MOVE getMove(Game game, long timeDue) {
		// TODO Auto-generated method stub
		return allMoves[rnd.nextInt(allMoves.length)];
	}

}

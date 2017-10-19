package pacman.controllers.examples;

import java.util.Random;

import dataRecording.DataTuple;
import dataRecording.DataTuple.DiscreteTag;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.controllers.Controller;

/*
 * The Class RandomPacMan.
 */
public final class RandomPacMan extends Controller<MOVE>
{
	private Random rnd=new Random();
	private MOVE[] allMoves=MOVE.values();
	
	/* (non-Javadoc)
	 * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
	 */
	public MOVE getMove(Game game,long timeDue)
	{
		DataTuple dt = new DataTuple(game, MOVE.NEUTRAL);
		DiscreteTag dTag =  dt.discretizeDistance(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.BLINKY)));
		System.out.println("Distans to Blinky: "+dTag);
		System.out.println("PacmanPosition: "+ game.getPacmanCurrentNodeIndex());
		MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
		return allMoves[rnd.nextInt(allMoves.length)];
	}
}
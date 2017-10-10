package pacman.controllers.examples;

import java.util.Random;

import decisiontree.Node;
import decisiontree.TrainModel;
import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ID3 extends Controller<MOVE> {
	private Random rnd=new Random();
	private MOVE[] allMoves=MOVE.values();
	private Node rootNode;
	public ID3(Node rootNode) {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		// TODO Auto-generated method stub
		//game.
		
		return allMoves[rnd.nextInt(allMoves.length)];
		
	}
	
	private MOVE getNextMoveFromDecisionTree(Node n){
		if (n.isLeafNode()){
			return n.getClassData();
		}
		return MOVE.DOWN;
	}

}

package pacman.controllers.examples;

import java.util.Random;

import dataRecording.DataTuple;
import decisiontree.Node;
import decisiontree.TrainModel;
import decisiontree.Constants.LABEL;
import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ID3 extends Controller<MOVE> {
	private Random rnd=new Random();
	private MOVE[] allMoves=MOVE.values();
	private Node rootNode;
	public ID3(Node rootNode) {
		this.rootNode = rootNode;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		// TODO Auto-generated method stub
		//game.
		MOVE m = getNextMoveFromDecisionTree(rootNode, game);
		System.out.println("Move: " + m.toString());
		return allMoves[rnd.nextInt(allMoves.length)];
		
	}
	
	private MOVE getNextMoveFromDecisionTree(Node n, Game g){
		MOVE m= null;
		String gameValue;
		if (n.isLeafNode()){
			m = n.getClassData();
		}else{
			//Get my children
			boolean foundMatch = false;
			for (Node n2: n.getChildren()){
				//there should always be a match between a childs value and a value from the game 
				if ( n2.isLeafNode()){
					m = n.getClassData();
				}
				String value = n2.getAttrValue();
				if (n2.getLabelData()==null){
					
				}
				gameValue = getValueForLabel(g,n2.getLabelData());
				System.out.println("This node: "+ n2.getLabelData().toString()+" value: "+ value + " gameValue "+gameValue);
				if (value.equals(gameValue)){
					foundMatch = true;
					//Go down in this branch
					m = getNextMoveFromDecisionTree(n2, g);
				}
			}
			if (!foundMatch){
				System.out.println("ERROR no match found");
			}
		}
		return m;
	}
	

	
	private  String getValueForLabel(Game g, LABEL attribute ){
		String actValueForAttribute ="";
		DataTuple d = new DataTuple(g,MOVE.NEUTRAL);  //Scrap the move
		if (attribute==null){
			System.out.println("Tjong");
		}
		switch (attribute) {
				case pacmanPosition:
					actValueForAttribute = d.discretizePosition(d.pacmanPosition).toString();
					break;
				case pacmanLivesLeft:
					actValueForAttribute = String.valueOf(d.pacmanLivesLeft);
					break;
				case currentScore:
					actValueForAttribute = d.discretizePosition(d.pacmanPosition).toString();
					break;
				case totalGameTime:
					actValueForAttribute = d.discretizeTotalGameTime(d.totalGameTime).toString();
					break;
				case currentLevelTime:
					actValueForAttribute = d.discretizeCurrentLevelTime(d.currentLevelTime).toString();
					break;
				case numOfPillsLeft:
					actValueForAttribute = d.discretizeNumberOfPills(d.numOfPillsLeft).toString();
					break;
				case numOfPowerPillsLeft:
					actValueForAttribute = d.discretizeNumberOfPowerPills(d.numOfPowerPillsLeft).toString();
					break;
				case isBlinkyEdible:
					actValueForAttribute = String.valueOf(d.isBlinkyEdible);
					break;
				case isInkyEdible:
					actValueForAttribute = String.valueOf(d.isInkyEdible);;
					break;
				case isPinkyEdible:
					actValueForAttribute = String.valueOf(d.isPinkyEdible);
					break;
				case isSueEdible:
					actValueForAttribute = String.valueOf(d.isSueEdible);
					break;
				case blinkyDist:
					actValueForAttribute = d.discretizeDistance(d.blinkyDist).toString();
					break;
				case inkyDist:
					actValueForAttribute = d.discretizeDistance(d.inkyDist).toString();
					break;
				case pinkyDist:
					actValueForAttribute = d.discretizeDistance(d.pinkyDist).toString();
					break;
				case sueDist:
					actValueForAttribute =d.discretizeDistance(d.sueDist).toString();
					break;
				case blinkyDir:
					actValueForAttribute = d.blinkyDir.toString();
					break;
				case inkyDir:
					actValueForAttribute = d.inkyDir.toString();
					break;
				case pinkyDir:
					actValueForAttribute = d.pinkyDir.toString();
					break;
				case sueDir:
					actValueForAttribute = d.sueDir.toString();
					break;
				default:
					System.out.println("Label not found");;
					break;

		}
		return actValueForAttribute;
	}

}

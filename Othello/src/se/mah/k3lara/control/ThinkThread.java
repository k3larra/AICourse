package se.mah.k3lara.control;

import java.util.ArrayList;
import java.util.Arrays;

import se.mah.k3lara.Helpers;
import se.mah.k3lara.Settings;
import se.mah.k3lara.model.Game;
import se.mah.k3lara.model.ItemState;

public class ThinkThread extends Thread {
	/*0=EMPTY, 1=WHITE 2=BLACK
	 * Utility values
	 * Computer wins 10
	 * Deadlock but computer wins 9
	 * Human can't move 7
	 * Equal 5
	 * Computer can't move 4
	 * Deadlock human wins 2
	 * Human wins 1
	*/
	private Controller c;
	private boolean goOn = true;
	private int deepestlevel=1;
	private int shallowestlevel= Integer.MAX_VALUE;
	private int depthThisBranch=1;
	private int nodesInvestigated=0;
	public ThinkThread (){}
	
	public void stopThinkingAndGiveAnAnswer(){
		goOn = false;
	}
	
	@Override
	public void run() {
			long start = System.currentTimeMillis();
			Action a = minMaxDecision(Game.getInstance().getGameStateClone());
			double duration = Math.round((System.currentTimeMillis()-start)/10.0)/100.0;
			if (a!=null){
				Controller.getInstance().printInfo("Ok done thinking it took: "+duration+" seconds.");
				Controller.getInstance().printInfo("Examined "+ nodesInvestigated+" nodes reaching a depth of "+deepestlevel+" layers, shallowest " +shallowestlevel);
				Controller.getInstance().printInfo("Computer will put a WHITE at [ "+ a.getRow()+","+a.getColumn()+" ].");
				Controller.getInstance().nextMove(a.getRow() ,a.getColumn(), null, Settings.computerPlayerMax);
			}else{
					Controller.getInstance().cannotMoveGiveTurnToHuman();
			}
	}
	
	private Action minMaxDecision(int[][] stateAsIntArray){
		int value = Integer.MIN_VALUE;
		//Get all possible moves
		ArrayList<Action> possibleMoves = getAllPossibleMoves(stateAsIntArray,Settings.computerPlayerMax);
		Controller.getInstance().printInfo("There are: "+possibleMoves.size()+" possible moves at the first level");
		nodesInvestigated++;
		Action ultimateMove=null;
		for (Action a : possibleMoves){
			//Get the Max value from all possible
			int actValue = minValue(makeTheMovePlusTurnAllAffectedPieces(stateAsIntArray, a,Settings.computerPlayerMax));
			if (actValue>value){
				value = actValue;
				ultimateMove=a;
			}
			if (possibleMoves.size()>0&&ultimateMove==null){
				Controller.getInstance().printInfo("Strange");
			}
			depthThisBranch=1;
		}
		if(value>5){
			Controller.getInstance().printInfo("PS: you dont have a chance whatever you do (I could be a little wrong still)");
		}
		if (possibleMoves.size()>0){
			return ultimateMove;
		}else{
			return null;
		}
		
	}
	
	private int maxValue(int[][] stateAsIntArray){
		nodesInvestigated++;
		depthThisBranch++;
		int value = Integer.MIN_VALUE;
		ArrayList<Action> possibleMoves = getAllPossibleMoves(stateAsIntArray,Settings.computerPlayerMax);
		//Terminal Test
		if (Rules.getEmptyPositions(stateAsIntArray).size()==0||possibleMoves.size()<1){ 
			//Check if deadend
			if(possibleMoves.size()<1){
				if(getAllPossibleMoves(stateAsIntArray, Settings.humanPlayerMin).size()>0){
					checkdepths(depthThisBranch);
					depthThisBranch--; //The computer can't move thats kinda bad stop here and do not investigate what will happen if the human takes over
					return 4;
				}
			}
			checkdepths(depthThisBranch);
			depthThisBranch--;
			return utilityValue(stateAsIntArray);
		}
		for (Action action : possibleMoves) {
			//Make the move and send down to the roots
			int actValue = minValue(makeTheMovePlusTurnAllAffectedPieces(stateAsIntArray, action, Settings.computerPlayerMax));
			if (actValue>value){
				value = actValue;
			}
		}
		depthThisBranch--;
		return value;
	}
	
	private int minValue(int[][] stateAsIntArray){
		//Terminal test (state) returns a utility value
		depthThisBranch++;
		int value = Integer.MAX_VALUE;
		ArrayList<Action> possibleMoves = getAllPossibleMoves(stateAsIntArray,Settings.humanPlayerMin);
		nodesInvestigated++;
		//Terminal Test
		if (Rules.getEmptyPositions(stateAsIntArray).size()==0||possibleMoves.size()<1){ 
			//If the game is over or the player can't move
			if(possibleMoves.size()<1){
				checkdepths(depthThisBranch);
				depthThisBranch--;
				return 7; //The human can't move thats kinda good let's stop here and do not investigate what will happen if the computer takes over
			}
			checkdepths(depthThisBranch);
			depthThisBranch--;
			return utilityValue(stateAsIntArray);
		}
		
		for (Action action : possibleMoves) {
			int actValue = maxValue(makeTheMovePlusTurnAllAffectedPieces(stateAsIntArray, action, Settings.humanPlayerMin));
			if (actValue<value){
				value = actValue;
			}
		}
		if(depthThisBranch>deepestlevel){
			deepestlevel=depthThisBranch;
		}
		depthThisBranch--;
		return value;
	}
	
	private int utilityValue(int[][] in){
		int computer =0;
		int human = 0;
		boolean deadEnd = false;
		for (int i = 0; i <Settings.nbrRowsColumns; i++){
			for (int j = 0; j<Settings.nbrRowsColumns;j++){
				if(Helpers.getPlayerCorrespondingInt(Settings.computerPlayerMax)==in[i][j]){
					computer++;
				}else if(Helpers.getOpponentPlayerCorrespondingInt(Settings.computerPlayerMax)==in[i][j]){
					human++;
				}
			}
		}
		if(human+computer!=(Settings.nbrRowsColumns*Settings.nbrRowsColumns)){ //Here can't move!!
            deadEnd=true;
		}
		if(computer>human){
			if(deadEnd){
				return 9;
			}else{
				return 10;
			}
		}else if (computer==human){
			System.out.println("HOHo");
			return 5; //perhaps....
		}else{
			if(deadEnd){
				return 2;
			}else{
				return 1;
			}
		}
	}
	

	private ArrayList<Action> getAllPossibleMoves(int[][] intArray, ItemState player){
		ArrayList<Action> emptyPlaces = Rules.getEmptyPositions(intArray);
		ArrayList<Action> possibleMoves = new ArrayList<Action>(); 
		for (Action a : emptyPlaces) {
			if( Rules.getAllTurnablePiecesFromThisNewPiece(intArray, a, player).size()>0){
				possibleMoves.add(a);
			}	
		}
		return possibleMoves;
	}
	
	private int[][] makeTheMovePlusTurnAllAffectedPieces(int[][] in, Action a, ItemState player){
		int[][] result = Helpers.makeDeepCopy(in);
		//Place the piece
		result[a.getRow()][a.getColumn()] = Helpers.getPlayerCorrespondingInt(player);
		//Turn other
		ArrayList<Action> turnThose = Rules.getAllTurnablePiecesFromThisNewPiece(result, a, player);
		for (Action action : turnThose) {
			result[action.getRow()][action.getColumn()] = Helpers.getPlayerCorrespondingInt(player);
		}
		return result;
	}
	
	private void checkdepths(int depthThisBranch){
		if(depthThisBranch>deepestlevel){
			deepestlevel=depthThisBranch;
		}
		if(depthThisBranch<shallowestlevel){
			shallowestlevel=depthThisBranch;
		}
	}
}

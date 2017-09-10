package se.mah.k3lara.control;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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
	private boolean stopAndReturnresult = false;
	private int deepestlevel=1;
	private int shallowestlevel= Integer.MAX_VALUE;
	private int depthThisBranch=1;
	private int nodesInvestigated=0;
	private int leavesChecked=0;
	private int alphaPruningValue = Integer.MIN_VALUE;
	private int betaPruningValue = Integer.MAX_VALUE;
	public ThinkThread (){}
	
	
	@Override
	public void run() {
			long start = System.currentTimeMillis();
			stopAndReturnresult = false;
			alphaPruningValue = Integer.MIN_VALUE;
			betaPruningValue = Integer.MAX_VALUE;
			new Timer().schedule(new TimerTask() {
				  @Override
				  public void run() {
				    System.out.println("Stop");
				    stopAndReturnresult=true;
				  }
				}, Settings.computerMaxThinkingtimeInSeconds*1000);
			Action a = minMaxDecision(Game.getInstance().getGameStateClone());
			double duration = Math.round((System.currentTimeMillis()-start)/10.0)/100.0;
			if (a!=null){
				Controller.getInstance().printInfo("Ok done thinking it took: "+duration+" seconds.");
				Controller.getInstance().printInfo("Examined "+ nodesInvestigated+" nodes reaching a depth of "+deepestlevel+" layers, shallowest " +shallowestlevel);
				Controller.getInstance().printInfo("reached "+ leavesChecked+" leaves.");
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
		Controller.getInstance().printInfo("Computer will select from: "+possibleMoves.size()+" possible moves at the first level");
		
		synchronized (possibleMoves) {
			if(possibleMoves.size()==1){
				//no need to think
				return possibleMoves.get(0);
			}
		}
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
		Controller.getInstance().printInfo("Value on my turn"+value);
		if(value>5){ //killermove
			if(!stopAndReturnresult){
				Controller.getInstance().printInfo("And it should guarantee a victory");
				return ultimateMove;
			}
		}
		if (possibleMoves.size()>0){
			return ultimateMove;
		}else{
			return null;
		}
		
	}
	
	private int maxValue(int[][] stateAsIntArray){
		if(stopAndReturnresult){
			//Estimate a utility here
			//Needs tuning
			return returnEstimateUtility(stateAsIntArray);
		}
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
					leavesChecked++;
					return 4;
				}
			}
			checkdepths(depthThisBranch);
			depthThisBranch--;
			leavesChecked++;
			return utilityValue(stateAsIntArray);
		}
		for (Action action : possibleMoves) {
			//Make the move and send down to the roots
			int actValue = minValue(makeTheMovePlusTurnAllAffectedPieces(stateAsIntArray, action, Settings.computerPlayerMax));
			if (actValue>value){
				value = actValue;
			}
			//pruning 
			if(Settings.pruning){
				if (value>=betaPruningValue){
					depthThisBranch--;
					return value;
				}
				alphaPruningValue = Math.max(alphaPruningValue, value);
			}
		}
		depthThisBranch--;
		return value;
	}
	



	private int minValue(int[][] stateAsIntArray){
		//Terminal test (state) returns a utility value
		if(stopAndReturnresult){
			//Needs tuning
			return returnEstimateUtility(stateAsIntArray);
		}
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
				leavesChecked++;
				return 7; //The human can't move thats kinda good let's stop here and do not investigate what will happen if the computer takes over
			}
			checkdepths(depthThisBranch);
			depthThisBranch--;
			leavesChecked++;
			return utilityValue(stateAsIntArray);
		}
		
		for (Action action : possibleMoves) {
			int actValue = maxValue(makeTheMovePlusTurnAllAffectedPieces(stateAsIntArray, action, Settings.humanPlayerMin));
			if (actValue<value){
				value = actValue;
			}
			if(Settings.pruning){
				if (value<=alphaPruningValue){
					depthThisBranch--;
					return value;
				}
				betaPruningValue = Math.min(betaPruningValue,value);
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
			System.out.println("Even check this");
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
	
	private int returnEstimateUtility(int[][] in) {
		int computer =0;
		int human = 0;
		int computerCorner=0;
		int humanCorner=0;
		int numberPiecesTotal = Settings.nbrRowsColumns*Settings.nbrRowsColumns;
		for (int i = 0; i <Settings.nbrRowsColumns; i++){
			for (int j = 0; j<Settings.nbrRowsColumns;j++){
				if(Helpers.getPlayerCorrespondingInt(Settings.computerPlayerMax)==in[i][j]){
					computer++;
				}else if(Helpers.getOpponentPlayerCorrespondingInt(Settings.computerPlayerMax)==in[i][j]){
					human++;
				}
				
			}
		}
		double estimate = 10*(((computer-human)/numberPiecesTotal)+1);
		if(in[0][0]==Helpers.getPlayerCorrespondingInt(Settings.computerPlayerMax)){
			computerCorner++;
		}
		if(in[0][0]==Helpers.getPlayerCorrespondingInt(Settings.humanPlayerMin)){
			humanCorner++;
		}
		if(in[0][Settings.nbrRowsColumns-1]==Helpers.getPlayerCorrespondingInt(Settings.computerPlayerMax)){
			computerCorner++;
		}
		if(in[0][Settings.nbrRowsColumns-1]==Helpers.getPlayerCorrespondingInt(Settings.humanPlayerMin)){
			humanCorner++;
		}
		if(in[Settings.nbrRowsColumns-1][0]==Helpers.getPlayerCorrespondingInt(Settings.computerPlayerMax)){
			computerCorner++;
		}
		if(in[Settings.nbrRowsColumns-1][0]==Helpers.getPlayerCorrespondingInt(Settings.humanPlayerMin)){
			humanCorner++;
		}
		if(in[Settings.nbrRowsColumns-1][Settings.nbrRowsColumns-1]==Helpers.getPlayerCorrespondingInt(Settings.computerPlayerMax)){
			computerCorner++;
		}
		if(in[Settings.nbrRowsColumns-1][Settings.nbrRowsColumns-1]==Helpers.getPlayerCorrespondingInt(Settings.humanPlayerMin)){
			humanCorner++;
		}
		estimate = estimate +computerCorner - humanCorner;
		if (estimate>9){
			estimate=9;
		}else if(estimate<2){
			estimate=2;
		}
		return (int)estimate;
	}
}

package se.mah.k3lara.control;

import java.util.ArrayList;
import java.util.Arrays;

import se.mah.k3lara.Helpers;
import se.mah.k3lara.Settings;
import se.mah.k3lara.model.Game;
import se.mah.k3lara.model.ItemState;

public class ThinkThread extends Thread {
	//0=EMPTY, 1=WHITE 2=BLACK
	//Utlility If I win a 2 if I lose a 1
	private Controller c;
	private boolean goOn = true;
	private int deepestlevel=1;
	private int depthThisBranch=1;
	private int nodesInvestigated=0;
	public ThinkThread (){
		//this.gameBoardAsIntArray = Game.getInstance().getGameStateClone();
	}
	
	public void stopThinkingAndGiveAnAnswer(){
		goOn = false;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
			/*try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			long start = System.currentTimeMillis();
			//Lets go
			Action a = minMaxDecision(Game.getInstance().getGameStateClone());
			//Done
			double duration = Math.round((System.currentTimeMillis()-start)/10.0)/100.0;
			if (a!=null){
				Controller.getInstance().printInfo("Ok done thinking it took: "+duration+" seconds.");
				Controller.getInstance().printInfo("Examined "+ nodesInvestigated+" nodes reaching a depth of "+deepestlevel+" layers");
				Controller.getInstance().printInfo("Computer will put a WHITE at [ "+ a.getRow()+","+a.getColumn()+" ].");
				Controller.getInstance().nextMove(a.getRow() ,a.getColumn(), null, Settings.computerPlayerMax);
			}else{
				Controller.getInstance().printInfo("Cannot find any empty spot your turn!");
				Controller.getInstance().cannotMoveGiveTurnToHuman();
				//give turn to human Controller.getInstance().nextMove(a.getRow() ,a.getColumn(), null, Settings.computerPlayerMax);
			}
	}
	
	
	//The assignment then....
	private Action minMaxDecision(int[][] stateAsIntArray){
		//Rules rules = new Rules(stateAsIntArray);
		//trash variable
		int value = Integer.MIN_VALUE;
		//Get all possible moves
		ArrayList<Action> possibleMoves = getAllPossibleMoves(stateAsIntArray,Settings.computerPlayerMax);
		//Controller.getInstance().printInfo("Found :"+possibleMoves.size()+" possible moves");
		Controller.getInstance().printInfo("There are: "+possibleMoves.size()+" possible moves at the first level");
		nodesInvestigated++;
		Action ultimateMove=null;
		int i = 1;
		for (Action a : possibleMoves){
			/*Controller.getInstance().printInfo("Alternative: "+i);
			Controller.getInstance().printGameState(newStateAsIntArray);*/
			//Get the Max value from all possible
			int actValue = minValue(makeTheMovePlusTurnAllAffectedPieces(stateAsIntArray, a));
			if (actValue>value){
				value = actValue;
				ultimateMove=a;
			}
			if (possibleMoves.size()>0&&ultimateMove==null){
				System.out.println("Strange");
			}
			if(depthThisBranch>deepestlevel){
				deepestlevel=depthThisBranch;
			}
			System.out.println("Depth branch: "+i+" depththisBranch "+depthThisBranch);
			depthThisBranch=1;
			i++;
		}
		if (possibleMoves.size()>0&&ultimateMove==null){
			ultimateMove = possibleMoves.get(0);  //Here at the end only few thing leftleft...
			System.out.println("Selected one cheeting..");
		}
		if (possibleMoves.size()>0){
			return ultimateMove;
		}else{
			return null;
		}
		
	}
	
	private int maxValue(int[][] stateAsIntArray){
		nodesInvestigated++;
		if (Rules.getPossibleActions(stateAsIntArray).size()==0){ //But if not alla full
			return utilityValue(stateAsIntArray);
		}
		int value = Integer.MIN_VALUE;
		ArrayList<Action> possibleMoves = getAllPossibleMoves(stateAsIntArray,Settings.computerPlayerMax);
		//Controller.getInstance().printInfo("Found :"+possibleMoves.size()+" possible moves on level: "+deepestlevel);
		int i = 1;
		for (Action action : possibleMoves) {
			if(i==1){depthThisBranch++;}
			//Make the move and send further:::
			int actValue = minValue(makeTheMovePlusTurnAllAffectedPieces(stateAsIntArray, action));
			if (actValue>value){
				value = actValue;
			}
			i++;
		}
		return value;
	}
	
	private int minValue(int[][] stateAsIntArray){
		//Terminal test (state) returns a utility value
		nodesInvestigated++;
		if (Rules.getPossibleActions(stateAsIntArray).size()==0){ //If the player can't move what to do??
			return utilityValue(stateAsIntArray);
		}
		int value = Integer.MAX_VALUE;
		ArrayList<Action> possibleMoves = getAllPossibleMoves(stateAsIntArray,Settings.humanPlayerMin);
		//Controller.getInstance().printInfo("Found :"+possibleMoves.size()+" possible moves on level: "+deepestlevel);
		int i = 1;
		for (Action action : possibleMoves) {
			if(i==1){depthThisBranch++;}
			//Make the move and send further:::
			int actValue = maxValue(makeTheMovePlusTurnAllAffectedPieces(stateAsIntArray, action));
			if (actValue<value){
				value = actValue;
			}
			i++;
		}
		return value;
	}
	
	//return -1 if problem 2 if computer wins 1 if human wins
	private int utilityValue(int[][] in){
		int computer =0;
		int human = 0;
		for (int i = 0; i <Settings.nbrRowsColumns; i++){
			for (int j = 0; j<Settings.nbrRowsColumns;j++){
				if(Helpers.getPlayerCorrespondingInt(Settings.computerPlayerMax)==in[i][j]){
					computer++;
				}else if(Helpers.getOpponentPlayerCorrespondingInt(Settings.computerPlayerMax)==in[i][j]){
					human++;
				}
			}
		}
		System.out.println("Utility");
		if(human+computer!=(Settings.nbrRowsColumns*Settings.nbrRowsColumns)){ //Here can't move!!
			System.out.println("problems");
			return 3; //Computer cant mova thats a good thing
		}
		if(computer>human){
			return 2;
		}else{
			return 1;
		}
	}
	/**returns a deep copy  where all pieces that is affected by the move is returned**/
	private ArrayList<Action> getAllPossibleMoves(int[][] intArray, ItemState player){
		ArrayList<Action> emptyPlaces = Rules.getPossibleActions(intArray);
		ArrayList<Action> possibleMoves = new ArrayList<Action>(); //Probably faster ways to do this...
		for (Action a : emptyPlaces) {
			//and check if they are legal I am computer!!!
			if( Rules.getAllTurnablePiecesFromThisNewPiece(intArray, a, player).size()>0){
				possibleMoves.add(a);
			}	
		}
		return possibleMoves;
	}
	
	private int[][] makeTheMovePlusTurnAllAffectedPieces(int[][] in, Action a){
		int[][] result = Helpers.makeDeepCopy(in);
		//Place the piece
		result[a.getRow()][a.getColumn()] = Helpers.getPlayerCorrespondingInt(Settings.computerPlayerMax);
		//Turn other
		ArrayList<Action> turnThose = Rules.getAllTurnablePiecesFromThisNewPiece(result, a, Settings.computerPlayerMax);
		for (Action action : turnThose) {
			result[action.getRow()][action.getColumn()] = Helpers.getPlayerCorrespondingInt(Settings.computerPlayerMax);
		}
		return result;
		
	}
}

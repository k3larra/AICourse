package se.mah.k3lara.control;

import java.util.ArrayList;

import se.mah.k3lara.Helpers;
import se.mah.k3lara.Settings;
import se.mah.k3lara.model.Game;
import se.mah.k3lara.model.GameUpdateInterface;
import se.mah.k3lara.model.ItemState;

public class Controller {
	//0= EMPTY, 1=WHITE 2=BLACK
	private static Controller instance;
	private static ItemState lastPlayer;
	private GameUpdateInterface gameUpdateInteface;
	private boolean pretendToThink = false;
	private boolean gameEnded = false;
	private ThinkThread thinkThread;
	private Controller(){
		//http://www.hannu.se/games/othello/rules.htm
		lastPlayer = ItemState.BLACK;
	}
	
	public static Controller getInstance(){
		if (instance==null){
			instance = new Controller();
		}
		return instance;
	}
	
	public void nextMove(int row, int column, ItemState currentState, ItemState player){
		//Check if legal move
		//If the computer comes with a new draw then ok
		if(!gameEnded){
			if (player == Settings.computerPlayerMax){
				pretendToThink = false;
			}
			if(!pretendToThink){  //Wating for Agent thinking to complete
				if (player == Settings.humanPlayerMin){
					Game.getInstance().setState(row, column, Settings.humanPlayerMin);
					printInfo("Human done computer is up");
					thinkThread = new ThinkThread();
					thinkThread.start();
					pretendToThink = true;
				}else if(player == Settings.computerPlayerMax){  //Computer player
					Game.getInstance().setState(row, column, Settings.computerPlayerMax);
					printInfo("Computer done human is up");
				}
				switchToNextPlayerTurn();
				printGameState(Game.getInstance().getGameStateClone());
			}else{
				printInfo("Cannot move paralyzed while calculating next move");
			}
			gameEnd(false);
		}else{
			printInfo("Game ended");
		}
	}
	
	private void gameEnd(boolean deadend) {
		int numberWhitePieces=0;
		int numberBlackPieces=0;
		int numberEmpty=0;
		int[][] test = Game.getInstance().getGameStateClone();
		for (int i = 0; i <Settings.nbrRowsColumns; i++){
			for (int j = 0; j<Settings.nbrRowsColumns;j++){
				if(test[i][j]==0){
					numberEmpty++;
					if(deadend==true){
						gameEnded=true;
					}
				}else if((test[i][j]==1)){
					numberWhitePieces++;
				}else if((test[i][j]==2)){
					numberBlackPieces++;
				}
			}
		}
		if((numberBlackPieces+numberWhitePieces)==(Settings.nbrRowsColumns*Settings.nbrRowsColumns)||gameEnded){
			if(numberWhitePieces>numberBlackPieces){
				printInfo("White wins "+numberWhitePieces+" pieces black "+ numberBlackPieces + " pieces");
			}else if(numberBlackPieces>numberWhitePieces){
				printInfo("Black wins "+numberBlackPieces+ " pieces white "+numberWhitePieces + "pieces");
			}else{
				printInfo("Equal");
			}
			int total = numberWhitePieces+ numberBlackPieces;
			printInfo("Total "+total + " pieces and " + numberEmpty +" empty.");
			gameEnded=true;
		}
	}

	public void cannotMoveGiveTurnToHuman(){
		printInfo("Computer cannot move human is up");
		//check so human can can move and not end of game
		int[][] emptyPos = Game.getInstance().getGameStateClone();
		ArrayList<Action> emptyPositions = Rules.getEmptyPositions(emptyPos);
		boolean possible = false;
		for (Action a : emptyPositions) {
			if(Rules.getAllTurnablePiecesFromThisNewPiece(emptyPos, a, Settings.humanPlayerMin).size()<1){
				possible = true; 
			}
		}
		if (possible) {
			pretendToThink = false;
		}else{
			gameEnd(true); //Game ended no-one can move board
		}
	}
	
	public void cannotMoveGiveTurnToComputer(){
		printInfo("Human cannot move computer is up");
		//check so human computer can move
		int[][] emptyPos = Game.getInstance().getGameStateClone();
		ArrayList<Action> emptyPositions = Rules.getEmptyPositions(emptyPos);
		boolean possible = false;
		for (Action a : emptyPositions) {
			if(Rules.getAllTurnablePiecesFromThisNewPiece(emptyPos, a, Settings.computerPlayerMax).size()>0){
				possible = true;
			}
		}	
		if (possible) {
			thinkThread = new ThinkThread();
			thinkThread.start();
			pretendToThink = true;
		}else{
			gameEnd(true); //Game ended no-one can move board
		}
	}
	

	private void switchToNextPlayerTurn(){
		switch (lastPlayer) {
		case BLACK:
			lastPlayer = ItemState.WHITE;
			break;
		case WHITE:
			lastPlayer = ItemState.BLACK;
			break;
		default:
			break;
		}
	}

	public void setGameRef(GameUpdateInterface gameUpdateInterface) {
		this.gameUpdateInteface = gameUpdateInterface;
		
	}
	
	protected void printInfo(String txt){
		if (gameUpdateInteface!=null){
			gameUpdateInteface.printInformation(txt);
		}
	}
	
	public void printGameState (int[][] stateAsInt){
		String s="";
		printInfo("****Current gamestate black=2 white=1 empty=0****");
		for (int i = 0; i <Settings.nbrRowsColumns; i++){
			s="";
			for (int j = 0; j<Settings.nbrRowsColumns;j++){
			switch (stateAsInt[i][j]) {
				case 2:
					s=s+" 2 ";
					break;
				case 1:
					s=s+" 1 ";
					break;
				case 0:
					s=s+" 0 ";
					break;
				default:
					s=s+" ? ";
					break;
				}
			}
			printInfo(s);
		}
		printInfo("********");
	}
	

}

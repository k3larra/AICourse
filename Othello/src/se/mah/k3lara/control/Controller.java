package se.mah.k3lara.control;

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
			printInfo("Cannot move paralyzed while pretending to think");
		}
		if(gameEnd()){}
	}
	
	private boolean gameEnd() {
		int numberWhitePieces=0;
		int numberBlackPieces=0;
		int numberEmpty=0;
		int[][] test = Game.getInstance().getGameStateClone();
		for (int i = 0; i <Settings.nbrRowsColumns; i++){
			for (int j = 0; j<Settings.nbrRowsColumns;j++){
				if(test[i][j]==0){
					numberEmpty++;
					return false;
				}else if((test[i][j]==1)){
					numberWhitePieces++;
				}else if((test[i][j]==2)){
					numberBlackPieces++;
				}
			}
		}
		if(numberWhitePieces>numberBlackPieces){
			printInfo("White wins "+numberWhitePieces+" pieces black "+ numberBlackPieces + " pieces");
		}else if(numberBlackPieces>numberWhitePieces){
			printInfo("Black wins "+numberBlackPieces+ " pieces white "+numberWhitePieces + "pieces");
		}else{
			printInfo("Equal");
		}
		int total = numberWhitePieces+ numberBlackPieces;
		printInfo("Total "+total + " pieces and " + numberEmpty +" empty.");
		return true;
	}

	public void cannotMoveGiveTurnToHuman(){
		printInfo("Computer cannot move human is up");
			pretendToThink = false;
	}
	
	public void cannotMoveGiveTurnToComputer(){
		printInfo("Human cannot move computer is up");
		thinkThread = new ThinkThread();
		thinkThread.start();
		pretendToThink = true;
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

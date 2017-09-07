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
		System.out.println("nextMove "+System.currentTimeMillis());
		if (player == Settings.computerPlayerMax){
			pretendToThink = false;
		}
		if(!pretendToThink){  //Wating for Agent thinking to complete
			if (player == Settings.humanPlayerMin){
				Game.getInstance().setState(row, column, Settings.humanPlayerMin);
				System.out.println("Human done computer is up");
				thinkThread = new ThinkThread();
				thinkThread.start();
				pretendToThink = true;
			}else if(player == Settings.computerPlayerMax){  //Computer player
				System.out.println("computer done"+row+":"+column);
				Game.getInstance().setState(row, column, Settings.computerPlayerMax);
			}
			switchToNextPlayerTurn();
			printGameState(Game.getInstance().getGameStateClone());
		}else{
			printInfo("Cannot move paralyzed while pretending to think");
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
		printInfo("****Begin Current gamestate****");
		printInfo("****black=2 white=1 empty=0****");
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
		printInfo("****End Current gamestate****");
	}
}

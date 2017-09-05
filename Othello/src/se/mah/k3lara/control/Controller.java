package se.mah.k3lara.control;

import se.mah.k3lara.Helpers;
import se.mah.k3lara.Settings;
import se.mah.k3lara.model.Game;
import se.mah.k3lara.model.GameUpdateInterface;
import se.mah.k3lara.model.ItemState;

public class Controller {
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
		if(!pretendToThink){
			if (player == Settings.humanPlayerMin){
				Game.getInstance().setState(row, column, Settings.humanPlayerMin);
				//Ok start thinkingOOOO
				thinkThread = new ThinkThread();
				thinkThread.start();
				pretendToThink = true;
			}else if(player == Settings.computerPlayerMax){
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
	
	public void printGameState (ItemState[][] state){
		String s="";
		printInfo("********");
		for (int i = 0; i <Settings.nbrRowsColumns; i++){
			s="";
			for (int j = 0; j<Settings.nbrRowsColumns;j++){
			switch (state[i][j]) {
				case BLACK:
					s=s+" B ";
					break;
				case WHITE:
					s=s+" W ";
					break;
				case EMPTY:
					s=s+" 0 ";
					break;
				default:
					s=s+" ? ";
					break;
				}
			}
			printInfo(s);
		}
		

	}
}

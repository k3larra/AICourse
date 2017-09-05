package se.mah.k3lara.control;

import se.mah.k3lara.Helpers;
import se.mah.k3lara.Settings;
import se.mah.k3lara.model.Game;
import se.mah.k3lara.model.GameUpdateInterface;
import se.mah.k3lara.model.State;

public class Controller {
	private static Controller instance;
	private static State lastPlayer;
	private GameUpdateInterface gameUpdateInteface; 
	private Controller(){
		//http://www.hannu.se/games/othello/rules.htm
		lastPlayer = State.BLACK;
	}
	
	public static Controller getInstance(){
		if (instance==null){
			instance = new Controller();
		}
		return instance;
	}
	
	public void nextMove(int row, int column, State currentState, State player){
		//Check if legal move
		if (player == Settings.humanPlayerMin){
			Game.getInstance().setState(row, column, Settings.humanPlayerMin);
			//Ok start thinkingOOOO
			nextMove(Helpers.getRandomColumnRowNumber(),Helpers.getRandomColumnRowNumber(),State.EMPTY,Settings.computerPlayerMax);
		}else if(player == Settings.computerPlayerMax){
			Game.getInstance().setState(row, column, Settings.computerPlayerMax);
		}
		switchToNextPlayerTurn();
		printGameState(Game.getInstance().getGameStateClone());
	}
	

	private void switchToNextPlayerTurn(){
		switch (lastPlayer) {
		case BLACK:
			lastPlayer = State.WHITE;
			break;
		case WHITE:
			lastPlayer = State.BLACK;
			break;
		default:
			break;
		}
	}

	public void setGameRef(GameUpdateInterface gameUpdateInterface) {
		this.gameUpdateInteface = gameUpdateInterface;
		
	}
	
	private void printInfo(String txt){
		if (gameUpdateInteface!=null){
			gameUpdateInteface.printInformation(txt);
		}
	}
	
	public void printGameState (State[][] state){
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

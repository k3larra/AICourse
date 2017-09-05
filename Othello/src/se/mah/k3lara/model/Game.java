package se.mah.k3lara.model;

import se.mah.k3lara.Settings;

public class Game {
	private static Game instance;
	private static State[][] gameBoard;
	private GameUpdateInterface gameUpdateInterface;
	private Game(){
		
	}
	
	public static Game getInstance(){
		if (instance==null){
			instance = new Game();
			gameBoard = new State[Settings.nbrRowsColumns][Settings.nbrRowsColumns];
			clearGameBoard();
		}
		return instance;
	}
	
	public boolean setState(int row,int column, State state){
		if (Settings.isColumnOrRowValueLegal(row)&&Settings.isColumnOrRowValueLegal(column)){
			gameBoard[row][column] = state;
			gameUpdateInterface.setGamePiece(row, column, state);
			return true;
		}else{
			return false;
		}
	}
	
	public State getState(int row, int column){
		if (Settings.isColumnOrRowValueLegal(row)&&Settings.isColumnOrRowValueLegal(column)){
			return gameBoard[row][column];
		}else{
		   return null;
		}
	}
	
	private static void clearGameBoard(){
		for (int i = 0; i <Settings.nbrRowsColumns; i++){
			for (int j = 0; j<Settings.nbrRowsColumns;j++){
				gameBoard[i][j]=State.EMPTY;
			}
		}
	}
	public State[][] getGameStateClone(){
		return gameBoard.clone();
	}

	public void setGameRef(GameUpdateInterface gameUpdateInterface) {
		this.gameUpdateInterface = gameUpdateInterface;
		
	}

}

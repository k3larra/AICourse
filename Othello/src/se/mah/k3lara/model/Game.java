package se.mah.k3lara.model;

import se.mah.k3lara.Settings;

public class Game {
	private static Game instance;
	private static ItemState[][] gameBoard;
	private GameUpdateInterface gameUpdateInterface;
	private Game(){
		
	}
	
	public static Game getInstance(){
		if (instance==null){
			instance = new Game();
			gameBoard = new ItemState[Settings.nbrRowsColumns][Settings.nbrRowsColumns];
			clearGameBoard();
		}
		return instance;
	}
	
	public boolean setState(int row,int column, ItemState state){
		if (Settings.isColumnOrRowValueLegal(row)&&Settings.isColumnOrRowValueLegal(column)){
			gameBoard[row][column] = state;
			gameUpdateInterface.setGamePiece(row, column, state);
			return true;
		}else{
			return false;
		}
	}
	
	public ItemState getState(int row, int column){
		if (Settings.isColumnOrRowValueLegal(row)&&Settings.isColumnOrRowValueLegal(column)){
			return gameBoard[row][column];
		}else{
		   return null;
		}
	}
	
	private static void clearGameBoard(){
		for (int i = 0; i <Settings.nbrRowsColumns; i++){
			for (int j = 0; j<Settings.nbrRowsColumns;j++){
				gameBoard[i][j]=ItemState.EMPTY;
			}
		}
	}
	
	//Return as int array...... for speed
	public int[][] getGameStateClone(){
		int[][]  stateIntClone = new int[Settings.nbrRowsColumns][Settings.nbrRowsColumns];
		for (int i = 0; i <Settings.nbrRowsColumns; i++){
			for (int j = 0; j<Settings.nbrRowsColumns;j++){
					switch (gameBoard[i][j]) {
					case EMPTY:
						stateIntClone[i][j]=0;
						break;
					case WHITE:
						stateIntClone[i][j]=1;
					break;
					case BLACK:
						stateIntClone[i][j]=2;
					break;
					default:
						stateIntClone[i][j]=0;
					break;
				}
			}
		}
		return stateIntClone;
	}

	public void setGameRef(GameUpdateInterface gameUpdateInterface) {
		this.gameUpdateInterface = gameUpdateInterface;
		
	}

}

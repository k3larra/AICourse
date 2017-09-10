package se.mah.k3lara.model;

import java.util.ArrayList;
import se.mah.k3lara.Settings;
import se.mah.k3lara.control.Action;
import se.mah.k3lara.control.Rules;

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
	
	public boolean setState(int row,int column, ItemState nextPlayerState){
		//Valid row and empty spot?
		if (Settings.isColumnOrRowValueLegal(row)&&Settings.isColumnOrRowValueLegal(column)&&gameBoard[row][column]==ItemState.EMPTY){
			//Here the piece is placed both in GUI and in model
			gameBoard[row][column] = nextPlayerState;
			gameUpdateInterface.setGamePiece(row, column, nextPlayerState);
			//get the result of other pieces turned...
			if (nextPlayerState != ItemState.EMPTY){
				ArrayList<Action> changed = Rules.getAllTurnablePiecesFromThisNewPiece(getGameStateClone(), new Action(row,column), nextPlayerState);
				for (Action action : changed) {
					gameBoard[action.getRow()][action.getColumn()] = nextPlayerState;
					gameUpdateInterface.setGamePiece(action.getRow(), action.getColumn(), nextPlayerState);
				}
			}
			return true;
		}else{
			return false;
		}
	}
	
	public void setStateUp(int row,int column, ItemState nextPlayerState){
		gameBoard[row][column] = nextPlayerState;
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
	public synchronized int[][] getGameStateClone(){ //Shit 4h.....
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
	
	public void clearAndReset(){
		instance=null;
	}
	

}

package se.mah.k3lara.model;

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
			System.out.println("setState "+System.currentTimeMillis());
			gameBoard[row][column] = nextPlayerState;
			System.out.println("setState "+System.currentTimeMillis());
			gameUpdateInterface.setGamePiece(row, column, nextPlayerState);
			System.out.println("setState "+System.currentTimeMillis());
			//get the result of other pieces turned...
			if (nextPlayerState != ItemState.EMPTY){
				int[][] newBoard = Rules.turnAllPiecesFromThisNewPiece(getGameStateClone(), new Action(row,column), nextPlayerState);
				System.out.println("#####newBoard");
				for (int i = 0; i <Settings.nbrRowsColumns; i++){
					String s ="";
					for (int j = 0; j<Settings.nbrRowsColumns;j++){
						s=s+newBoard[i][j];
					}
					System.out.println(s);
				}
				System.out.println("#####end newBoard");
				//And crappy code turn everything again!!!
				for (int i = 0; i <Settings.nbrRowsColumns; i++){
					for (int j = 0; j<Settings.nbrRowsColumns;j++){
						switch(newBoard[i][j]){
						case 0:
							gameBoard[i][j] = ItemState.EMPTY;
							gameUpdateInterface.setGamePiece(i, j, ItemState.EMPTY);
							break;
						case 1:
							gameBoard[i][j] = ItemState.WHITE;
							gameUpdateInterface.setGamePiece(i, j, ItemState.WHITE);
							break;
						case 2:
							gameBoard[i][j] = ItemState.BLACK;
							gameUpdateInterface.setGamePiece(i, j, ItemState.BLACK);
							break;
						}
					}
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
		/*System.out.println("#####in getGameStateClone");
		for (int i = 0; i <Settings.nbrRowsColumns; i++){
			String s ="";
			for (int j = 0; j<Settings.nbrRowsColumns;j++){
				s=s+stateIntClone[i][j];
			}
			System.out.println(s);
		}
		System.out.println("#####end In getGameStateClone");*/
		return stateIntClone;
	}

	public void setGameRef(GameUpdateInterface gameUpdateInterface) {
		this.gameUpdateInterface = gameUpdateInterface;
		
	}

}

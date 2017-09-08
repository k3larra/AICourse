package se.mah.k3lara;

import se.mah.k3lara.model.ItemState;

public class Helpers {
 public static int getRandomColumnRowNumber(){
	 return (int)(Math.random()*Settings.nbrRowsColumns);
 }
 //Converts the enum empty black etc to int
 //0=EMPTY, 1=WHITE 2=BLACK
	public static int getPlayerCorrespondingInt(ItemState itemState){
		int intState=0;
		switch (itemState) {
		case BLACK:
			intState = 2;
			break;
		case WHITE:
			intState = 1;
			break;
		case EMPTY:
			intState = 0;
			break;
		default:
			break;
		}
		return intState;
	}
	
	public static int getOpponentPlayerCorrespondingInt(ItemState itemState){
		int intState=0;
		switch (itemState) {
		case BLACK:
			intState = 1;
			break;
		case WHITE:
			intState = 2;
			break;
		case EMPTY:
			intState = 0;
			break;
		default:
			break;
		}
		return intState;
	}
	
	public static synchronized int[][] makeDeepCopy(int[][] inArray){
		int[][] outArray = new int[Settings.nbrRowsColumns][Settings.nbrRowsColumns];
		 for (int i = 0; i <Settings.nbrRowsColumns; i++){
				for (int j = 0; j<Settings.nbrRowsColumns;j++){
					outArray[i][j] = inArray[i][j];
				}
			}
		 return outArray;
	}
}

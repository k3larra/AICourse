package se.mah.k3lara.control;

import java.util.ArrayList;

import se.mah.k3lara.Helpers;
import se.mah.k3lara.Settings;
import se.mah.k3lara.model.ItemState;

public class CheckTurn{
	  private int[][] gameStateInt;
	  //private boolean turn = false;
	  private boolean legalMove=false;
	  private ArrayList<Action> changed;
	  public CheckTurn(int[][] gameStateInt, int row, int column, ItemState itemStateCurrentPlayer){
		  this.gameStateInt = gameStateInt;
		  //this.turn = turn;
		  changed = new ArrayList<Action>();
		  checkOrTurnCenter(row, column, itemStateCurrentPlayer);
	  }
	
	public int[][] getGameStateInt() {
		return this.gameStateInt;
	}
	public ArrayList<Action> getAllChanged() {
		return this.changed;
	}

	public boolean wasItALegalMove(){
	  return this.legalMove;
	}
	
	private void checkOrTurnCenter(int row,int column, ItemState itemStateCurrentPlayer){
		for (double f = 0; f<=2*Math.PI;f=f+Math.PI/4){
			 int x=Math.round((float)Math.cos(f));
			 int y=Math.round((float)Math.sin(f));
			 checkOrTurn(row+y,column+x,itemStateCurrentPlayer, x,y);
		 }
		 
	}
	
	private boolean checkOrTurn(int row,int column, ItemState itemStateCurrentPlayer, int x, int y) {
		  //have we reached the edge of the board?
		  if(column==Settings.nbrRowsColumns||column<0){
			  return false; //Get the hell out
		  }
		  if(row==Settings.nbrRowsColumns||row<0){ 
			  return false; //Get the hell out
		  }
		  if (gameStateInt[row][column]==Helpers.getOpponentPlayerCorrespondingInt(itemStateCurrentPlayer)){ //Still another color
			  if(checkOrTurn(row+y,column+x, itemStateCurrentPlayer, x, y)){ //continue check next
					 changed.add(new Action(row,column));
				  return true;
			  }else{
				  return false;
			  }
		  }else if (gameStateInt[row][column]==Helpers.getPlayerCorrespondingInt(itemStateCurrentPlayer)){
			  return true; //found same color
		  }else{
			  return false; //found grass
		  }
	  }
	
	  
}

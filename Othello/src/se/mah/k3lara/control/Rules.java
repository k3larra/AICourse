package se.mah.k3lara.control;

import java.util.ArrayList;

import javax.swing.JButton;

import se.mah.k3lara.Helpers;
import se.mah.k3lara.Settings;
import se.mah.k3lara.model.ItemState;
import se.mah.k3lara.view.Piece;

public class Rules {
	//0= EMPTY, 1=WHITE 2=BLACK
  //private int[][] stateAsIntArray;
 // public Rules(int[][] stateAsIntArray){
//	  this.stateAsIntArray = stateAsIntArray;
//  }
  
  public static ArrayList<Action> getPossibleActions(int[][] stateAsIntArray){
	  ArrayList<Action> myActions = new ArrayList<Action>();
	  //Start with getting all possible actions
	  for (int i = 0; i <Settings.nbrRowsColumns; i++){
			for (int j = 0; j<Settings.nbrRowsColumns;j++){
				if (stateAsIntArray[i][j] == 0){
					myActions.add(new Action(i,j));
				}
			}
		}
	  Controller.getInstance().printInfo("Found :"+myActions.size()+" empty places");
	  return myActions;
  }
  
  public static int[][] turnAllPiecesFromThisNewPiece(int[][] gameStateInt, Action a, ItemState itemStateCurrentPlayer){
	  CheckTurn ch = new CheckTurn(gameStateInt, true,a.getRow(),a.getColumn(),itemStateCurrentPlayer);
	  return ch.getGameStateInt();
  }
  
  /*
  private class CheckTurn{
	  private int[][] gameStateInt;
	  private boolean turn;
	  public CheckTurn(int[][] gameStateInt, boolean turn){
		  this.gameStateInt = gameStateInt;
		  this.turn = turn;
	  }
	public int[][] getGameStateInt() {
		return this.gameStateInt;
	}
	//Returns true if it is a legal placement
	public boolean checkOrTurnCenter(int[][] gameStateInt, int row,int column, ItemState itemStateCurrentPlayer,boolean turn){
		  boolean turnthis = false;
		  //direction
		  //East
		  checkOrTurn(row,column, itemStateCurrentPlayer, 0);

		  return turnthis;
	  }
	
	public boolean checkOrTurn(int row,int column, ItemState itemStateCurrentPlayer, int direction) {
		  //have we reached the edge of the board?
		  if(column==Settings.nbrRowsColumns){ //East
			  return false; //Get the hell out
		  }
		  if (gameStateInt[row][column]==Helpers.getOpponentPlayerCorrespondingInt(itemStateCurrentPlayer)){ //Still another color
			  if(checkOrTurn(row,column+1, itemStateCurrentPlayer, 0)){ //continue check next
				  if(turn){//shall we turn this when we come back?
					  gameStateInt[row][column] = Helpers.getPlayerCorrespondingInt(itemStateCurrentPlayer);
				  }
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
  */
}

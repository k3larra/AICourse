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
	  if (gameStateInt[a.getRow()][a.getColumn()]==0){
		  gameStateInt[a.getRow()][a.getColumn()] = Helpers.getPlayerCorrespondingInt(itemStateCurrentPlayer);
		  //go north?
		  if(a.getRow()>0&&gameStateInt[a.getRow()][a.getColumn()]==Helpers.getOpponentPlayerCorrespondingInt(itemStateCurrentPlayer)){
			  checkAndTurnNorth(gameStateInt, a.getColumn()-1,a.getRow(),itemStateCurrentPlayer);
		  }
		  for (int i = a.getColumn(); 1 >=0; i=i-1){
			  if(gameStateInt[a.getRow()][a.getColumn()]==Helpers.getOpponentPlayerCorrespondingInt(itemStateCurrentPlayer)){
				  
			  }
		  }
	  }else{
		  Controller.getInstance().printInfo("Trying to place a piece on a non empty place");
	  }
	  return gameStateInt;
  }
  
  public static boolean checkAndTurnNorth(int[][] gameStateInt, int row,int column, ItemState itemStateCurrentPlayer){
	  boolean turn = false;
	  if (row>0&&gameStateInt[row][column]==Helpers.getOpponentPlayerCorrespondingInt(itemStateCurrentPlayer)) { //still a opponent check next
			  if(checkAndTurnNorth(gameStateInt, row-1,column,itemStateCurrentPlayer)){ //ok 
				  turn=true;
			  }
	  }
	  if (row>0&&gameStateInt[row][column]==Helpers.getPlayerCorrespondingInt(itemStateCurrentPlayer)){ //Found another of me
		  turn=true;
	  }
	  return turn;
  }
  
}

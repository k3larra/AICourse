package se.mah.k3lara.control;

import java.util.ArrayList;

import javax.swing.JButton;

import se.mah.k3lara.Helpers;
import se.mah.k3lara.Settings;
import se.mah.k3lara.model.ItemState;
import se.mah.k3lara.view.Piece;

public class Rules {
	//http://www.hannu.se/games/othello/rules.htm
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
	  return myActions;
  }
  
  public static ArrayList<Action> getAllTurnablePiecesFromThisNewPiece(int[][] gameStateInt, Action a, ItemState itemStateCurrentPlayer){
	  CheckTurn ch = new CheckTurn(gameStateInt,a.getRow(),a.getColumn(),itemStateCurrentPlayer);
	  return ch.getAllChanged();
  }
}

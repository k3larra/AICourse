package se.mah.k3lara.control;

import java.util.ArrayList;

import javax.swing.JButton;

import se.mah.k3lara.Settings;
import se.mah.k3lara.model.ItemState;
import se.mah.k3lara.view.Piece;

public class Rules {
  private ItemState[][] state;
  public Rules(ItemState[][] state){
	  this.state = state;
  }
  
  public ArrayList<Action> getPossibleActions(){
	  ArrayList<Action> myActions = new ArrayList<Action>();
	  //Start with getting all possible actions
	  for (int i = 0; i <Settings.nbrRowsColumns; i++){
			for (int j = 0; j<Settings.nbrRowsColumns;j++){
				if (state[i][j] == ItemState.EMPTY){
					myActions.add(new Action(i,j));
				}
			}
		}
	  Controller.getInstance().printInfo("Found :"+myActions.size()+" empty places");
	  return myActions;
  }
}

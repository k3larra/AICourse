package se.mah.k3lara.control;

import se.mah.k3lara.Helpers;
import se.mah.k3lara.Settings;
import se.mah.k3lara.model.Game;
import se.mah.k3lara.model.ItemState;

public class ThinkThread extends Thread {
	private Controller c;
	private  se.mah.k3lara.model.ItemState[][] gameBoard;
	private boolean goOn = true;
	
	public ThinkThread (){
		this.gameBoard = Game.getInstance().getGameStateClone();
		Controller.getInstance().printInfo("Length: "+this.gameBoard.length);
	}
	
	public void stopThinkingAndGiveAnAnswer(){
		goOn = false;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Controller.getInstance().printInfo("Ok done thinking");
			Controller.getInstance().printInfo("Examined 185 nodes reaching a depth of 5 layers");
			Controller.getInstance().printInfo("Computer will put a White Here");
			Action a = minMaxDecision(Game.getInstance().getGameStateClone());
			Controller.getInstance().nextMove(a.getRow() ,a.getColumn(), null, Settings.computerPlayerMax);
	}
	
	
	//The assignment then....
	private Action minMaxDecision(ItemState[][] state){
		Rules rules = new Rules(state);
		ItemState[][] newState;
		int value = Integer.MIN_VALUE;
		//Get all possible placements and chose max of them
		for (Action a : rules.getPossibleActions()) {
			newState = state.clone();
			newState[a.getRow()][a.getColumn()] = Settings.computerPlayerMax;
			int actValue = minValue(newState);
			if (actValue>value){
				value = actValue;
			}
		}
		return new Action(Helpers.getRandomColumnRowNumber(), Helpers.getRandomColumnRowNumber());
	}
	
	private int maxValue(ItemState[][] state){
		
		return 3;
	}
	
	private int minValue(ItemState[][] state){
		
		return 3;
	}
	
	
	
}

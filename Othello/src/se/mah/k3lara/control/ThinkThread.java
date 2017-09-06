package se.mah.k3lara.control;

import java.util.Arrays;

import se.mah.k3lara.Helpers;
import se.mah.k3lara.Settings;
import se.mah.k3lara.model.Game;
import se.mah.k3lara.model.ItemState;

public class ThinkThread extends Thread {
	//0=EMPTY, 1=WHITE 2=BLACK
	private Controller c;
	private  int[][] gameBoardAsIntArray;
	private boolean goOn = true;
	
	public ThinkThread (){
		this.gameBoardAsIntArray = Game.getInstance().getGameStateClone();
		Controller.getInstance().printInfo("Length: "+this.gameBoardAsIntArray.length);
	}
	
	public void stopThinkingAndGiveAnAnswer(){
		goOn = false;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
			/*try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			long start = System.currentTimeMillis();
			//Lets go
			Action a = minMaxDecision(Game.getInstance().getGameStateClone());
			//Done
			double duration = Math.round((System.currentTimeMillis()-start)/10.0)/100.0;
			Controller.getInstance().printInfo("Ok done thinking it took: "+duration+" seconds.");
			Controller.getInstance().printInfo("Examined X nodes reaching a depth of Y layers");
			Controller.getInstance().printInfo("Computer will put a COLOR at [ "+ a.getRow()+","+a.getColumn()+" ]");
			Controller.getInstance().nextMove(a.getRow() ,a.getColumn(), null, Settings.computerPlayerMax);
	}
	
	
	//The assignment then....
	private Action minMaxDecision(int[][] stateAsIntArray){
		//Rules rules = new Rules(stateAsIntArray);
		//trash variable
		int[][] newStateAsIntArray;
		int value = Integer.MIN_VALUE;
		try {
		Thread.sleep(1701);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		//Get all possible placements and chose max of them
		for (Action a : Rules.getPossibleActions(stateAsIntArray)) {
			//Get a new instance of gameState
			newStateAsIntArray = stateAsIntArray.clone();
			//Place the piece 
			newStateAsIntArray[a.getRow()][a.getColumn()] = Helpers.getPlayerCorrespondingInt(Settings.computerPlayerMax);
			int actValue = minValue(newStateAsIntArray);
			if (actValue>value){
				value = actValue;
			}
		}
		return new Action(Helpers.getRandomColumnRowNumber(), Helpers.getRandomColumnRowNumber());
	}
	
	private int maxValue(int[][] StateAsIntArray){
		
		return 3;
	}
	
	private int minValue(int[][] stateAsIntArray){
		
		return 4;
	}
	
	
}

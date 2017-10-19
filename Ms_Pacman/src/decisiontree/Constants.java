package decisiontree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;
import decisiontree.Constants.LABEL;
import pacman.game.Constants.MOVE;

public class Constants {
	public static enum LABEL {DirectionChosen, mazeIndex, currentLevel,pacmanPosition,pacmanLivesLeft,currentScore,totalGameTime,currentLevelTime,numOfPillsLeft,numOfPowerPillsLeft,
		isBlinkyEdible,isInkyEdible,isPinkyEdible,isSueEdible,blinkyDist,inkyDist,pinkyDist,sueDist,blinkyDir,inkyDir,pinkyDir,sueDir,numberOfNodesInLevel,
		numberOfTotalPillsInLevel,numberOfTotalPowerPillsInLevel};
	public static enum LEAF {UP,DOWN,LEFT,RIGHT,NEUTRAL}; 
	
	public static Node decisionTree;
	
	public static ArrayList<LABEL> getAllLabels(){
		return new ArrayList<LABEL>(Arrays.asList(LABEL.values()));
	}

	public enum STRATEGY 
	{
		ATTACK,	
		RUN, 	
		EAT_PILLS,		
		EAT_POWER_PILLS, NOSTRATEGY		
	};
	
	public static final String[][] tupleMatrix = generateStringMatrixFromDataTuple(DataSaverLoader.LoadPacManData());
	public static final String[][] testMatrix = generateStringMatrixFromDataTuple(DataSaverLoader.LoadPacManTestData());
	private static String[][] generateStringMatrixFromDataTuple(DataTuple[] tuples){
//		System.out.println(LABEL.valueOf("blinkyDir").ordinal());
//		LABEL[] l= LABEL.values();
		String[][] all = new String[LABEL.values().length][tuples.length];
//		for (int i = 0; i<LABEL.values().length;i++){
//			all[i][0] = LABEL.values()[i].toString();
//		}
//		System.out.println("all[0][70]"+all[0][70]);
//		System.out.println(tuples[70].DirectionChosen.toString());
		for (int i=0; i<all.length; i++ ){
			for (int j = 0; j<all[0].length;j++){
			  switch (i) {
				case 0:
					 all[i][j] = tuples[j].DirectionChosen.toString();
					break;
				case 1:
					all[i][j] = String.valueOf(tuples[j].mazeIndex);
					break;
				case 2:
					all[i][j] = String.valueOf(tuples[j].currentLevel);
					break;
				case 3:
					all[i][j] = tuples[j].discretizePosition(tuples[j].pacmanPosition).toString();
					break;
				case 4:
					all[i][j] = String.valueOf(tuples[j].pacmanLivesLeft);
					break;
				case 5:
					all[i][j] = tuples[j].discretizeCurrentScore(tuples[j].currentScore).toString();
					break;
				case 6:
					all[i][j] = tuples[j].discretizeTotalGameTime(tuples[j].totalGameTime).toString();
					break;
				case 7:
					all[i][j] = tuples[j].discretizeCurrentLevelTime(tuples[j].currentLevelTime).toString();
					break;
				case 8:
					all[i][j] = tuples[j].discretizeNumberOfPills(tuples[j].numOfPillsLeft).toString();
					break;
				case 9:
					all[i][j] = tuples[j].discretizeNumberOfPowerPills(tuples[j].numOfPowerPillsLeft).toString();
					break;
				case 10:
					all[i][j] = String.valueOf(tuples[j].isBlinkyEdible);
					break;
				case 11:
					all[i][j] = String.valueOf(tuples[j].isInkyEdible);
					break;
				case 12:
					all[i][j] = String.valueOf(tuples[j].isPinkyEdible);
					break;
				case 13:
					all[i][j] = String.valueOf(tuples[j].isSueEdible);
					break;
				case 14:
					all[i][j] = tuples[j].discretizeDistance(tuples[j].blinkyDist).toString();
					break;
				case 15:
					all[i][j] = tuples[j].discretizeDistance(tuples[j].inkyDist).toString();
					break;
				case 16:
					all[i][j] = tuples[j].discretizeDistance(tuples[j].pinkyDist).toString();
					break;
				case 17:
					all[i][j] = tuples[j].discretizeDistance(tuples[j].sueDist).toString();
					break;
				case 18:
					all[i][j] = tuples[j].blinkyDir.toString();
					break;
				case 19:
					all[i][j] = tuples[j].inkyDir.toString();
					break;
				case 20:
					all[i][j] = tuples[j].pinkyDir.toString();
					break;
				case 21:
					all[i][j] = tuples[j].sueDir.toString();
					break;
				case 22:
					all[i][j] =  String.valueOf(tuples[j].numberOfNodesInLevel);
					break;
				case 23:
					all[i][j] =  String.valueOf(tuples[j].numberOfTotalPillsInLevel);
					break;
				case 24:
					all[i][j] =  String.valueOf(tuples[j].numberOfTotalPowerPillsInLevel);
					break;
				default:
					all[i][j] = "las: "+i+":"+j;
					break;
				}
			 
			}
		}
		//all[0]

		return all;
	}
}

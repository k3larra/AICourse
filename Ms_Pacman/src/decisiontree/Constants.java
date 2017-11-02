package decisiontree;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
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
		EAT_POWER_PILLS, 
		NOSTRATEGY		
	};
	
	public static final String[][] tupleMatrix = generateStringMatrixFromDataTuple(DataSaverLoader.LoadPacManData());
	public static final String[][] testMatrix = generateStringMatrixFromDataTuple(DataSaverLoader.LoadPacManTestData());
	public static final String[][]trainingMatrixRaw = generateRawNumberMatrixFromDataTuple(DataSaverLoader.LoadPacManData());
	public static final String[][] testMatrixRaw = generateRawNumberMatrixFromDataTuple(DataSaverLoader.LoadPacManTestData());
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
	
	private static String[][] generateRawNumberMatrixFromDataTuple(DataTuple[] tuples){
		String[][] all = new String[LABEL.values().length][tuples.length];
		for (int i=0; i<all.length; i++ ){
			for (int j = 0; j<all[0].length;j++){
			  switch (i) {
				case 0:
					 switch (tuples[j].DirectionChosen) {
						case NEUTRAL:
							all[i][j]="0";
							break;
						case RIGHT:
							all[i][j]="1";
							break;
						case UP:
							all[i][j]="2";
							break;
						case LEFT:
							all[i][j]="3";
							break;
						case DOWN:
							all[i][j]="4";
							break;
						}
					break;
				/*case 1:
					all[i][j] = String.valueOf(tuples[j].mazeIndex);
					break;*/
				/*case 2:
					all[i][j] = String.valueOf(tuples[j].currentLevel);
					break;*/
				case 3:
					all[i][j] = String.valueOf(tuples[j].pacmanPosition);
					break;
				/*case 4:
					all[i][j] = String.valueOf(tuples[j].pacmanLivesLeft);
					break;*/
				/*case 5:
					all[i][j] = String.valueOf(tuples[j].currentScore);
					break;*/
				/*case 6:
					all[i][j] = String.valueOf(tuples[j].totalGameTime);
					break;*/
				/*case 7:
					all[i][j] = String.valueOf(tuples[j].currentLevelTime);
					break;*/
				/*case 8:
					all[i][j] = String.valueOf(tuples[j].numOfPillsLeft);
					break;
				case 9:
					all[i][j] = String.valueOf(tuples[j].numOfPowerPillsLeft);
					break;*/
				case 10:
					if (tuples[j].isBlinkyEdible){
						all[i][j] = "1";
					}else{
						all[i][j] = "0";
					}
					break;
				case 11:
					if (tuples[j].isInkyEdible){
						all[i][j] = "1";
					}else{
						all[i][j] = "0";
					}
					break;
				case 12:
					if (tuples[j].isPinkyEdible){
						all[i][j] = "1";
					}else{
						all[i][j] = "0";
					}
					break;
				case 13:
					if (tuples[j].isSueEdible){
						all[i][j] = "1";
					}else{
						all[i][j] = "0";
					}
					break;
				case 14:
					all[i][j] = String.valueOf(tuples[j].blinkyDist);
					break;
				case 15:
					all[i][j] = String.valueOf(tuples[j].inkyDist);
					break;
				case 16:
					all[i][j] = String.valueOf(tuples[j].pinkyDist);
					break;
				case 17:
					all[i][j] = String.valueOf(tuples[j].sueDist);
					break;
				case 18:
					switch (tuples[j].blinkyDir) {
					case NEUTRAL:
						all[i][j]="0";
						break;
					case RIGHT:
						all[i][j]="1";
						break;
					case UP:
						all[i][j]="2";
						break;
					case LEFT:
						all[i][j]="3";
						break;
					case DOWN:
						all[i][j]="4";
						break;
					}
					break;
				case 19:
					switch (tuples[j].inkyDir) {
					case NEUTRAL:
						all[i][j]="0";
						break;
					case RIGHT:
						all[i][j]="1";
						break;
					case UP:
						all[i][j]="2";
						break;
					case LEFT:
						all[i][j]="3";
						break;
					case DOWN:
						all[i][j]="4";
						break;
					}
					break;
				case 20:
					switch (tuples[j].pinkyDir) {
					case NEUTRAL:
						all[i][j]="0";
						break;
					case RIGHT:
						all[i][j]="1";
						break;
					case UP:
						all[i][j]="2";
						break;
					case LEFT:
						all[i][j]="3";
						break;
					case DOWN:
						all[i][j]="4";
						break;
					}
					break;
				case 21:
					switch (tuples[j].sueDir) {
					case NEUTRAL:
						all[i][j]="0";
						break;
					case RIGHT:
						all[i][j]="1";
						break;
					case UP:
						all[i][j]="2";
						break;
					case LEFT:
						all[i][j]="3";
						break;
					case DOWN:
						all[i][j]="4";
						break;
					}
					break;
				/*case 22:
					all[i][j] =  String.valueOf(tuples[j].numberOfNodesInLevel);
					break;
				case 23:
					all[i][j] =  String.valueOf(tuples[j].numberOfTotalPillsInLevel);
					break;
				case 24:
					all[i][j] =  String.valueOf(tuples[j].numberOfTotalPowerPillsInLevel);
					break;*/
				}
			 
			}
		}
		//all[0]
		int numbernulls=0;
		for (int i =0;i<all.length;i++){
			if (all[i][0]==null){
				numbernulls++;
			}
		}
		System.out.println("numbernulls: "+numbernulls);
		System.out.println("all[0].length: "+all[0].length);
		System.out.println("all.length: "+all.length);
		String[][] allSmall = new String[LABEL.values().length-numbernulls][tuples.length];
		System.out.println("allSmall[0].length: "+allSmall[0].length);
		System.out.println("allSmall.length: "+allSmall.length);
		for (int j = 0; j<all[0].length;j++){
			int k=0;
			for (int i=0; i<all.length; i++ ){
				if (all[i][j]!=null){
					allSmall[k][j] = all[i][j];
					k++;
				}
			}	
		}

		return allSmall;
	}
	
	public static void exportDataRawAsNumberString(String[][] input,String filename){
		String path = "../DecisionTreeTest/"+filename;
		File f = new File(path);
		if(f.exists() && !f.isDirectory()) { 
		    f.delete();
		}
		Writer output;
		try {
			output = new BufferedWriter(new FileWriter(path,true));
			for (int i=0; i<input[0].length; i++ ){
				String s = "";
				for (int j = 0; j<input.length;j++){
					String k = input[j][i];
					s=s+k;
					if(j<input.length-1){
						s=s+",";
					}
				}
				s=s+"\n";
					output.append(s);
			}
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

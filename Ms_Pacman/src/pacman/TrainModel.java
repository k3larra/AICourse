package pacman;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;
import decisiontreeid3.TreeNode;
import decisiontreeid3.Constants.LABEL;

public class TrainModel {
    private Collection<LABEL> Labels = new ArrayList<LABEL>(); 
    private String[][] tuplesAsStrings;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DataTuple[] tuples = DataSaverLoader.LoadPacManData();
		int i =0;
		/*for (DataTuple t: tuples){
			System.out.println("CurrentScore:"+t.currentScore + " ScoreNorm: "+t.normalizeCurrentScore(t.currentScore));
			if (i>100){
				break;
			}
		}	*/
		TrainModel t = new TrainModel();
		String[][] sMatrix = t.generateStringMatrixFromDataTuple(tuples);
		t.printMatrix(sMatrix,4);
		String[][] tMatrix = t.removeLabelAndData(sMatrix, LABEL.currentLevel);
		tMatrix = t.removeLabelAndData(tMatrix, LABEL.mazeIndex);
		tMatrix = t.removeLabelAndData(tMatrix, LABEL.totalGameTime);
		tMatrix = t.removeLabelAndData(tMatrix, LABEL.numberOfNodesInLevel);
		tMatrix = t.removeLabelAndData(tMatrix, LABEL.numberOfTotalPillsInLevel);
		tMatrix = t.removeLabelAndData(tMatrix, LABEL.numberOfTotalPowerPillsInLevel);
		t.printMatrix(tMatrix,500);
	}
	
	private void generateTree(DataTuple[] tuples){
		// nodeTree = new TreeNode(LABEL.pacmanLivesLeft);
		
	}
	
	private String[][] removeLabelAndData(String[][] tuplesAsStrings, LABEL l){
		int removeThis = -1;
		for (int i = 0; i<tuplesAsStrings.length;i++){
			if (l.toString().equals(tuplesAsStrings[i][0])){
				removeThis = i;
			}
		}
		System.out.println("removeThis"+removeThis);
		String[][] result = null;
		if (removeThis<=tuplesAsStrings.length&&removeThis>-1){
			result = new String[tuplesAsStrings.length-1][tuplesAsStrings[0].length];
			System.out.println("result cols: "+ result.length+" result rows: "+ result[0].length);
			System.out.println("cols: "+ tuplesAsStrings.length+" rows: "+ tuplesAsStrings[0].length);
			System.out.println(removeThis);
			for (int i = 0; i<result[0].length; i++ ){ //Max en massa
				for (int j = 0; j<result.length;j++){ //max några
					if (j<removeThis){
						result[j][i] = tuplesAsStrings[j][i];
					}else if(j>=removeThis){
						result[j][i] = tuplesAsStrings[j+1][i];
					}else{
						//Remove this
					}
				}
			}
		}
		return result;
	}
	
	String[][] deepCopy(String[][] matrix) {
	    return java.util.Arrays.stream(matrix).map(el -> el.clone()).toArray($ -> matrix.clone());
	}
	
	private  String[][] generateStringMatrixFromDataTuple(DataTuple[] tuples){
		System.out.println(LABEL.valueOf("blinkyDir").ordinal());
		LABEL[] l= LABEL.values();
		String[][] all = new String[LABEL.values().length][tuples.length+1];
		for (int i = 0; i<LABEL.values().length;i++){
			all[i][0] = LABEL.values()[i].toString();
		}
		System.out.println("all[0][70]"+all[0][70]);
		System.out.println(tuples[70].DirectionChosen.toString());
		for (int i=0; i<all.length; i++ ){
			for (int j = 1; j<all[0].length;j++){
			  switch (i) {
				case 0:
					 all[i][j] = tuples[j-1].DirectionChosen.toString();
					break;
				case 1:
					all[i][j] = String.valueOf(tuples[j-1].mazeIndex);
					break;
				case 2:
					all[i][j] = String.valueOf(tuples[j-1].currentLevel);
					break;
				case 3:
					all[i][j] = tuples[j-1].discretizePosition(tuples[j-1].pacmanPosition).toString();
					break;
				case 4:
					all[i][j] = String.valueOf(tuples[j-1].pacmanLivesLeft);
					break;
				case 5:
					all[i][j] = tuples[j-1].discretizeCurrentScore(tuples[j-1].currentScore).toString();
					break;
				case 6:
					all[i][j] = tuples[j-1].discretizeTotalGameTime(tuples[j-1].totalGameTime).toString();
					break;
				case 7:
					all[i][j] = tuples[j-1].discretizeCurrentLevelTime(tuples[j-1].currentLevelTime).toString();
					break;
				case 8:
					all[i][j] = tuples[j-1].discretizeNumberOfPills(tuples[j-1].numOfPillsLeft).toString();
					break;
				case 9:
					all[i][j] = tuples[j-1].discretizeNumberOfPowerPills(tuples[j-1].numOfPowerPillsLeft).toString();
					break;
				case 10:
					all[i][j] = String.valueOf(tuples[j-1].isBlinkyEdible);
					break;
				case 11:
					all[i][j] = String.valueOf(tuples[j-1].isInkyEdible);
					break;
				case 12:
					all[i][j] = String.valueOf(tuples[j-1].isPinkyEdible);
					break;
				case 13:
					all[i][j] = String.valueOf(tuples[j-1].isSueEdible);
					break;
				case 14:
					all[i][j] = tuples[j-1].discretizeDistance(tuples[j-1].blinkyDist).toString();
					break;
				case 15:
					all[i][j] = tuples[j-1].discretizeDistance(tuples[j-1].inkyDist).toString();
					break;
				case 16:
					all[i][j] = tuples[j-1].discretizeDistance(tuples[j-1].pinkyDist).toString();
					break;
				case 17:
					all[i][j] = tuples[j-1].discretizeDistance(tuples[j-1].sueDist).toString();
					break;
				case 18:
					all[i][j] = tuples[j-1].blinkyDir.toString();
					break;
				case 19:
					all[i][j] = tuples[j-1].inkyDir.toString();
					break;
				case 20:
					all[i][j] = tuples[j-1].pinkyDir.toString();
					break;
				case 21:
					all[i][j] = tuples[j-1].sueDir.toString();
					break;
				case 22:
					all[i][j] =  String.valueOf(tuples[j-1].numberOfNodesInLevel);
					break;
				case 23:
					all[i][j] =  String.valueOf(tuples[j-1].numberOfTotalPillsInLevel);
					break;
				case 24:
					all[i][j] =  String.valueOf(tuples[j-1].numberOfTotalPowerPillsInLevel);
					break;
				default:
					all[i][j] = "las: "+i+":"+j;
					break;
				}
			 
			}
		}
		//all[0]
		System.out.println("all[0][3]"+all[0][3]);
		return all;
	}
	
	private void printMatrix(String[][] stringMatrix,int rowsToPrint){
		System.out.println("cols: "+ stringMatrix.length+" rows: "+ stringMatrix[0].length);
		String fillout="";
		int k=0;
		if (stringMatrix[0].length<rowsToPrint){
			rowsToPrint = stringMatrix[0].length;
		}
		for (int i=0; i<rowsToPrint; i++ ){
			for (int j = 0; j<stringMatrix.length;j++){
				if(i != 0){
					fillout="";
					k = stringMatrix[j][0].length() - stringMatrix[j][i].length();
					if(k>0){
						for (int l = 0; l<k; l++){
							fillout = fillout + " ";
						}
					}
					System.out.print(stringMatrix[j][i]+fillout+"|");
				}else{
					System.out.print(stringMatrix[j][i]+"|");
				}
			}
			System.out.println();
		}
	}

}

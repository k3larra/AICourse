package pacman;

import java.util.ArrayList;

import decisiontree.Constants;
import decisiontree.Constants.LABEL;
import decisiontree.ID3AttributeSelectionMethod;

public class testID3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<LABEL> attribute_list = Constants.getAllLabels();
		/*String[][] tMatrix = removeLabelData(Constants.tupleMatrix, LABEL.currentLevel, attribute_list);
		attribute_list.remove(LABEL.currentLevel);
		tMatrix = removeLabelData(tMatrix, LABEL.mazeIndex,attribute_list);
		attribute_list.remove(LABEL.mazeIndex);
		tMatrix = removeLabelData(tMatrix, LABEL.totalGameTime,attribute_list);
		attribute_list.remove(LABEL.totalGameTime);
		tMatrix = removeLabelData(tMatrix, LABEL.numberOfNodesInLevel,attribute_list);
		attribute_list.remove(LABEL.numberOfNodesInLevel);
		tMatrix = removeLabelData(tMatrix, LABEL.numberOfTotalPillsInLevel,attribute_list);
		attribute_list.remove(LABEL.numberOfTotalPillsInLevel);
		tMatrix = removeLabelData(tMatrix, LABEL.numberOfTotalPowerPillsInLevel,attribute_list);
		attribute_list.remove(LABEL.numberOfTotalPowerPillsInLevel);
		System.out.println("attribute_list.size(): "+attribute_list.size());*/
		ID3AttributeSelectionMethod id3 = new ID3AttributeSelectionMethod();
		id3.method(Constants.tupleMatrix, attribute_list);
		for (LABEL label : id3.getAttributesWithZeroGain()) {
			System.out.println(label.toString());
		}
	}
	
	private static String[][] removeLabelData(String[][] tuplesAsStrings, LABEL l, ArrayList<LABEL> attribute_list ){
		int removeThis = attribute_list.indexOf(l);
		String[][] result = null;
		if (removeThis<=tuplesAsStrings.length&&removeThis>-1){
			result = new String[tuplesAsStrings.length-1][tuplesAsStrings[0].length];
//			System.out.println("result cols: "+ result.length+" result rows: "+ result[0].length);
//			System.out.println("cols: "+ tuplesAsStrings.length+" rows: "+ tuplesAsStrings[0].length);
//			System.out.println(removeThis);
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

}

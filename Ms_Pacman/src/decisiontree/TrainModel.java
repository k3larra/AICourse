package decisiontree;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;
import decisiontree.Constants.LABEL;

import pacman.game.Constants.MOVE;

//Ask Jose why not all static
//Nothing is said about were to store C class The leaf nodes
//And nothing about were to store the end nodes from C which are different form the other nodes  (they are attributes).
//Remove the class from training data?

public class TrainModel {
    private Collection<LABEL> Labels = new ArrayList<LABEL>(); 
    private String[][] tuplesAsStrings;
    public static Node rootNode; 
    private int currentLevel;
    private int deepestLevel;
	/**
	 * 
	 */
	public TrainModel() {
		DataTuple[] tuples = DataSaverLoader.LoadPacManData();
		int i =0;
		/*for (DataTuple t: tuples){
			System.out.println("CurrentScore:"+t.currentScore + " ScoreNorm: "+t.normalizeCurrentScore(t.currentScore));
			if (i>100){
				break;
			}
		}	*/
		//TrainModel t = new TrainModel();
		//String[][] sMatrix = generateStringMatrixFromDataTuple(tuples);
		
        ArrayList<LABEL> attribute_list = Constants.getAllLabels();
//		System.out.println("attribute_list.size()"+attribute_list.size());
//		//printMatrix(Constants.tupleMatrix,4,attribute_list);
//		//Clean none useful data
        printMatrix(Constants.tupleMatrix,50,attribute_list);
		String[][] tMatrix = removeLabelData(Constants.tupleMatrix, LABEL.currentLevel, attribute_list);
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
		System.out.println("attribute_list.size()"+attribute_list.size());
		//printMatrix(tMatrix,5,attribute_list);
//		String [] attributes = getLabelAttributeValues(tMatrix, LABEL.blinkyDist, attribute_list);
//		for (String string : attributes) {
//			System.out.println(string);
//		}
//		System.out.println(getMajorityClass(tMatrix).toString());
//		String[][] xMatrix = selectAttributeData(tMatrix, LABEL.isPinkyEdible, attribute_list, "true");
//		System.out.println(getMajorityClass(xMatrix).toString());
		printMatrix(tMatrix,50,attribute_list);
		rootNode = generateDecisionTree(tMatrix, attribute_list, new ID3AttributeSelectionMethod());
		rootNode.printAllLowerNodes();
		System.out.println("DeepestLevel: "+deepestLevel);
		SaveTree.saveTree(rootNode);
	}
	


	private Node generateDecisionTree(String[][] d, ArrayList<LABEL> attribute_list, AttributeSelectionMethod att ){
//		//THis should not be first but removing all zero gain attributes before is great!!
		LABEL l = att.method(d, attribute_list);
		//remove LABELS with zero gain
		System.out.println("BEfore");
		printMatrix(d, 20, attribute_list);
		/*ArrayList<LABEL> labelsToRemove = att.getAttributesWithZeroGain();
		for (LABEL label : labelsToRemove) {
			d = removeLabelData(d, label,attribute_list);
			attribute_list.remove(label);
			System.out.println("Removed: "+ label.toString());
		}
		System.out.println("After");
		printMatrix(d, 20, attribute_list);*/
//		1: Create node N.
		Node n = new Node();
		currentLevel++;
		n.setNodelevel(currentLevel);
		if (currentLevel>deepestLevel){
			deepestLevel = currentLevel;
		}
		//System.out.println();
		//System.out.println("********************** Entering new node ***************************");
		//printMatrix(d,20,attribute_list);
//		2. If every tuple in D has the same class C, return N as a leaf node labeled as C.
		MOVE leaf = doesAllNodesHaveSameClass(d);
		if(leaf!=null){
			n.setAsLeafNode(leaf);
			//System.out.println("All nodes have same class return node as: "+ n.getClassData().toString()+" : attirbute "+n.getAttrValue());
			System.out.println("Nodelevel: "+currentLevel);
			currentLevel--;
			n.printNodeInfo();
			return n;
		}else{
			//System.out.println("The nodes does not have the same class");
		}
//		3. Otherwise, if the attribute list is empty, return N as a leaf node labeled with the majority class in D.
		if(attribute_list.size()==1){  //This never happends and that is strange
			MOVE m = getMajorityClass(d);
			n.setAsLeafNode(m);
			System.out.println("Nodelevel: "+currentLevel);
			currentLevel--;
			System.out.println("*****TJO Attribute list is empty so return node in majority vote as : "+ n.getClassData().toString() +" : attribute "+n.getAttrValue());
			n.printNodeInfo();
			return n;
		}
//		4. Otherwise:
			
		//1. Call the attribute selection method on D and the attribute list, in order to choose the current attribute A:
//				LABEL l = att.method(d, attribute_list);
				//remove LABELS with zero gain
//				ArrayList<LABEL> labelsToRemove = att.getAttributesWithZeroGain();
//				for (LABEL label : labelsToRemove) {
//					attribute_list.remove(label);
//					System.out.println("Removed: "+ label.toString());
//				}
		//System.out.println("Selected randomly attribute to remove fake method: "+l.toString());
//				S(D, attribute list) -> A.
//		2. Label N as A and remove A from the attribute list.
		String[] attributeValues = getLabelAttributeValues(d, l, attribute_list);
		//System.out.println("Attribute to create children for node type: "+l.toString());
//		for (String string : attributeValues) {
//			System.out.print(string+" : ");
//		}
//		System.out.println();
		n.setNodeLabel(l);
		ArrayList<LABEL> cloneAttributeList = (ArrayList<LABEL>) attribute_list.clone(); //Remove form attributelist But keep a copy
		cloneAttributeList.remove(l);
		//3. For each value aj in attribute A:
		for (String attributeValue : attributeValues) {
//			a) Separate all tuples in D so that attribute A takes the value aj, creating the subset Dj.
			String[][] dj = selectAttributeValueData(d,l,attribute_list,attributeValue);
			//System.out.println("All tuples that has: "+attributeValue+" as value.");
			//printMatrix(dj,20,cloneAttributeList);
//			b) If Dj is empty, add a child node to N labeled with the majority class in D.
			if(dj.length<2){  //Only one column the class column is left
				Node child = new Node();
				child.setAsLeafNode(getMajorityClass(d));
				child.setAttributeValue(attributeValue);
				child.setNodelevel(currentLevel+1);
				if (currentLevel+1>deepestLevel){
					deepestLevel = currentLevel+1;
				}
				n.addChildNode(child);
				//System.out.println("Dj is empty adding child as majority vote node the child has class: "+child.getClassData().toString());
			}else{
//			c) Otherwise, add the resulting node from calling Generate_Tree(Dj, attribute) as a child node to N.
				//remove all attributes with zero gain.
				Node n3 = generateDecisionTree(dj, cloneAttributeList, att);
				n3.setAttributeValue(attributeValue);
				n.addChildNode(n3);
			}
		}	
		//attribute_list.remove(l);
//		4. Return N.
		//System.out.println("************** returning node labled with attribute: "+n.getLabelData()+" *******************");
		//System.out.println("CurrentLevel: "+currentLevel);
		//System.out.println("Created node with ");
		System.out.println("Nodelevel: "+currentLevel);
		n.printNodeInfo();
		currentLevel--;
		return n; 
		
	}
	

	private MOVE doesAllNodesHaveSameClass(String[][] d) {
		MOVE l = null;
		if (d.length>0){
			l=MOVE.valueOf(d[0][0]); //get the first class
		}
		for (int i = 1; i<d[0].length;i++){
			if (!l.toString().equals(d[0][i])){ //break if not equal
				l=null;
				break;
			}
		}
		return l;
	}

	private String[][] removeLabelData(String[][] tuplesAsStrings, LABEL l, ArrayList<LABEL> attribute_list ){
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
	
	private String[][] selectAttributeValueData(String[][] tuplesAsStrings, LABEL l, ArrayList<LABEL> attribute_list, String attribute){
		String[][] result = null;
		int column = attribute_list.indexOf(l);
		int rows= 0;
		for (int i = 0; i<tuplesAsStrings[0].length; i++){
			if (tuplesAsStrings[column][i].equals(attribute)){
				rows++;
			}
		}
		if (column>-1&&tuplesAsStrings!=null){
				result = new String[tuplesAsStrings.length][rows];
	//			System.out.println("result cols: "+ result.length+" result rows: "+ result[0].length);
	//			System.out.println("cols: "+ tuplesAsStrings.length+" rows: "+ tuplesAsStrings[0].length);
	//			System.out.println(removeThis);
				int m=0;
				for (int i = 0; i<tuplesAsStrings[0].length; i++ ){ 
					if (tuplesAsStrings[column][i].equals(attribute)){
						for(int j=0;j<attribute_list.size();j++){
							result[j][m] = tuplesAsStrings[j][i];
						}
						m++;
					}
				}
				result = removeLabelData(result, l, attribute_list);
		}
		return result;
	}
	
	private String[] getLabelAttributeValues(String[][] tuplesAsStrings, LABEL l, ArrayList<LABEL> attribute_list){
		ArrayList<String> result = new ArrayList<String>();
		int column = attribute_list.indexOf(l);
		if (column>-1&&tuplesAsStrings!=null){
			for (int i = 0; i<tuplesAsStrings[0].length; i++ ){ //Max en massa
				if (!result.contains(tuplesAsStrings[column][i])){
					result.add(tuplesAsStrings[column][i]);
				}
			}
		}
		return result.toArray(new String[result.size()]);
	}
	
	private MOVE getMajorityClass(String[][] tuplesAsStrings){
		MOVE result = null;
		int numberUp=0;
		int numberDown=0;
		int numberLeft=0;
		int numberRight=0;
		int numberNeutral=0;
		if (tuplesAsStrings!=null && tuplesAsStrings.length>0){
			for (int i = 0; i<tuplesAsStrings[0].length;i++){
				switch (MOVE.valueOf(tuplesAsStrings[0][i])) {
				case UP:
					numberUp++;
					break;
				case DOWN:
					numberDown++;
					break;	
				case LEFT:
					numberLeft++;
					break;	
				case RIGHT:
					numberRight++;
					break;	
				case NEUTRAL:
					numberNeutral++;
					break;	
				default:
					break;
				}
			}
			int[] all = {numberUp,numberDown,numberLeft,numberRight,numberNeutral};
			int index = -1;
			for (int i=0; i <all.length;i++) {
				if (all[i]>index){
					index = i;
				}
			}
			switch (index) {
			case 0:
				result = MOVE.UP;
				break;
			case 1:
				result = MOVE.DOWN;
				break;
			case 2:
				result = MOVE.LEFT;
				break;
			case 3:
				result = MOVE.RIGHT;
				break;
			case 4:
				result = MOVE.NEUTRAL;
				break;	
			default:
				result = null;
				break;
			}
		}
		return result;
	}
	

	
	String[][] deepCopy(String[][] matrix) {
	    return java.util.Arrays.stream(matrix).map(el -> el.clone()).toArray($ -> matrix.clone());
	}
	
//	private  String[][] generateStringMatrixFromDataTuple(DataTuple[] tuples){
////		System.out.println(LABEL.valueOf("blinkyDir").ordinal());
////		LABEL[] l= LABEL.values();
//		String[][] all = new String[LABEL.values().length][tuples.length];
////		for (int i = 0; i<LABEL.values().length;i++){
////			all[i][0] = LABEL.values()[i].toString();
////		}
////		System.out.println("all[0][70]"+all[0][70]);
////		System.out.println(tuples[70].DirectionChosen.toString());
//		for (int i=0; i<all.length; i++ ){
//			for (int j = 0; j<all[0].length;j++){
//			  switch (i) {
//				case 0:
//					 all[i][j] = tuples[j].DirectionChosen.toString();
//					break;
//				case 1:
//					all[i][j] = String.valueOf(tuples[j].mazeIndex);
//					break;
//				case 2:
//					all[i][j] = String.valueOf(tuples[j].currentLevel);
//					break;
//				case 3:
//					all[i][j] = tuples[j].discretizePosition(tuples[j].pacmanPosition).toString();
//					break;
//				case 4:
//					all[i][j] = String.valueOf(tuples[j].pacmanLivesLeft);
//					break;
//				case 5:
//					all[i][j] = tuples[j].discretizeCurrentScore(tuples[j].currentScore).toString();
//					break;
//				case 6:
//					all[i][j] = tuples[j].discretizeTotalGameTime(tuples[j].totalGameTime).toString();
//					break;
//				case 7:
//					all[i][j] = tuples[j].discretizeCurrentLevelTime(tuples[j].currentLevelTime).toString();
//					break;
//				case 8:
//					all[i][j] = tuples[j].discretizeNumberOfPills(tuples[j].numOfPillsLeft).toString();
//					break;
//				case 9:
//					all[i][j] = tuples[j].discretizeNumberOfPowerPills(tuples[j].numOfPowerPillsLeft).toString();
//					break;
//				case 10:
//					all[i][j] = String.valueOf(tuples[j].isBlinkyEdible);
//					break;
//				case 11:
//					all[i][j] = String.valueOf(tuples[j].isInkyEdible);
//					break;
//				case 12:
//					all[i][j] = String.valueOf(tuples[j].isPinkyEdible);
//					break;
//				case 13:
//					all[i][j] = String.valueOf(tuples[j].isSueEdible);
//					break;
//				case 14:
//					all[i][j] = tuples[j].discretizeDistance(tuples[j].blinkyDist).toString();
//					break;
//				case 15:
//					all[i][j] = tuples[j].discretizeDistance(tuples[j].inkyDist).toString();
//					break;
//				case 16:
//					all[i][j] = tuples[j].discretizeDistance(tuples[j].pinkyDist).toString();
//					break;
//				case 17:
//					all[i][j] = tuples[j].discretizeDistance(tuples[j].sueDist).toString();
//					break;
//				case 18:
//					all[i][j] = tuples[j].blinkyDir.toString();
//					break;
//				case 19:
//					all[i][j] = tuples[j].inkyDir.toString();
//					break;
//				case 20:
//					all[i][j] = tuples[j].pinkyDir.toString();
//					break;
//				case 21:
//					all[i][j] = tuples[j].sueDir.toString();
//					break;
//				case 22:
//					all[i][j] =  String.valueOf(tuples[j].numberOfNodesInLevel);
//					break;
//				case 23:
//					all[i][j] =  String.valueOf(tuples[j].numberOfTotalPillsInLevel);
//					break;
//				case 24:
//					all[i][j] =  String.valueOf(tuples[j].numberOfTotalPowerPillsInLevel);
//					break;
//				default:
//					all[i][j] = "las: "+i+":"+j;
//					break;
//				}
//			 
//			}
//		}
//		//all[0]
//
//		return all;
//	}
	
	private void printMatrix(String[][] stringMatrix,int rowsToPrint, ArrayList<LABEL> attribute_list ){
		if(stringMatrix==null){
			System.out.println("Oups empty matrix");
		}else{
			//System.out.println("cols: "+ stringMatrix.length+" rows: "+ stringMatrix[0].length);
			String fillout="";
			int k=0;
			if (stringMatrix[0].length<rowsToPrint){
				rowsToPrint = stringMatrix[0].length;
			}
			//System.out.println("rowsToPrint: "+rowsToPrint);
			//Print headlines
			for (int i= 0; i<attribute_list.size();i++){
				System.out.print(attribute_list.get(i).toString()+"|");
			}
			System.out.println();
			for (int i=0; i<rowsToPrint; i++ ){
				for (int j = 0; j<attribute_list.size();j++){
							fillout="";
							if (stringMatrix[j][i]!=null){
								 k = attribute_list.get(j).toString().length() - stringMatrix[j][i].length();
							}else{
								k = attribute_list.get(j).toString().length()-"null".length();
							}
								 if(k>0){
									for (int l = 0; l<k; l++){
										fillout = fillout + " ";
									}
								 }
							
							System.out.print(stringMatrix[j][i]+fillout+"|");
					}
				System.out.println();
			}
		}
	}
}



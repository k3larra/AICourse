package decisiontree;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;
import dataRecording.DataTuple.DiscreteTag;
import decisiontree.Constants.LABEL;
import decisiontree.Constants.STRATEGY;
import pacman.game.Constants.MOVE;

//Ask Jose why not all static
//Nothing is said about were to store C class The leaf nodes
//And nothing about were to store the end nodes from C which are different form the other nodes  (they are attributes).
//Remove the class from training data?

public class TrainModel {
    public static Node rootNode; 
    private int currentLevel;
    private int deepestLevel;
	private int nodeCount;
	/**
	 * 
	 */
	public TrainModel() {
        ArrayList<LABEL> attribute_list = Constants.getAllLabels();
        //Run a test on Gain on top level for the dataset.
        String[][] tMatrix = Constants.tupleMatrix;
        printMatrix(tMatrix,5,attribute_list);
        new ID3AttributeSelectionMethod(true).method(tMatrix, attribute_list);
        System.out.println();
        //Build Decision Tree
		tMatrix = removeLabelData(Constants.tupleMatrix, LABEL.currentLevel, attribute_list);
		attribute_list.remove(LABEL.currentLevel);
		tMatrix = removeLabelData(tMatrix, LABEL.mazeIndex,attribute_list);
		attribute_list.remove(LABEL.mazeIndex);
		tMatrix = removeLabelData(tMatrix, LABEL.pacmanPosition,attribute_list);
		attribute_list.remove(LABEL.pacmanPosition);
		tMatrix = removeLabelData(tMatrix, LABEL.pacmanLivesLeft,attribute_list);
		attribute_list.remove(LABEL.pacmanLivesLeft);
		tMatrix = removeLabelData(tMatrix, LABEL.currentScore,attribute_list);
		attribute_list.remove(LABEL.currentScore);
		tMatrix = removeLabelData(tMatrix, LABEL.totalGameTime,attribute_list);
		attribute_list.remove(LABEL.totalGameTime);
		/*tMatrix = removeLabelData(tMatrix, LABEL.currentLevelTime,attribute_list);
		attribute_list.remove(LABEL.currentLevelTime);*/
		/*tMatrix = removeLabelData(tMatrix, LABEL.numOfPillsLeft,attribute_list);
		attribute_list.remove(LABEL.numOfPillsLeft);*/
		tMatrix = removeLabelData(tMatrix, LABEL.numOfPowerPillsLeft,attribute_list);
		attribute_list.remove(LABEL.numOfPowerPillsLeft);
		/*tMatrix = removeLabelData(tMatrix, LABEL.isInkyEdible,attribute_list);
		attribute_list.remove(LABEL.isInkyEdible);
		tMatrix = removeLabelData(tMatrix, LABEL.isBlinkyEdible,attribute_list);
		attribute_list.remove(LABEL.isBlinkyEdible);
		tMatrix = removeLabelData(tMatrix, LABEL.isPinkyEdible,attribute_list);
		attribute_list.remove(LABEL.isPinkyEdible);
		tMatrix = removeLabelData(tMatrix, LABEL.isSueEdible,attribute_list);
		attribute_list.remove(LABEL.isSueEdible);*/
		tMatrix = removeLabelData(tMatrix, LABEL.numberOfNodesInLevel,attribute_list);
		attribute_list.remove(LABEL.numberOfNodesInLevel);
		tMatrix = removeLabelData(tMatrix, LABEL.numberOfTotalPillsInLevel,attribute_list);
		attribute_list.remove(LABEL.numberOfTotalPillsInLevel);
		tMatrix = removeLabelData(tMatrix, LABEL.numberOfTotalPowerPillsInLevel,attribute_list);
		attribute_list.remove(LABEL.numberOfTotalPowerPillsInLevel);
		//remove all rows that are distant....
		//tMatrix = removeAllTuplesWereAllGostsAreFarAway(tMatrix, attribute_list);
		String[][] q = addStrategyClass(tMatrix,attribute_list);
		printMatrix(q,5,attribute_list);
		for (String string : getLabelAttributeValues(q, LABEL.DirectionChosen, attribute_list)) {
			System.out.println("LABEL.DirectionChosen: "+string);
		}
		System.out.println("Attribute gain for test set containing "+ tMatrix[0].length+" rows." );
		new ID3AttributeSelectionMethod(true).method(tMatrix, attribute_list);
		printMatrix(tMatrix,5,attribute_list);
		rootNode = generateDecisionTree(tMatrix, attribute_list, new ID3AttributeSelectionMethod(false));
		System.out.println("DeepestLevel: "+deepestLevel+ " number of nodes: "+nodeCount);
		SaveTree.saveTree(rootNode);
		//Test accuracy on test set
		attribute_list = Constants.getAllLabels();
		String[][] xMatrix = Constants.testMatrix;		
		xMatrix = removeLabelData(Constants.testMatrix, LABEL.currentLevel, attribute_list);
		attribute_list.remove(LABEL.currentLevel);
		xMatrix = removeLabelData(xMatrix, LABEL.mazeIndex,attribute_list);
		attribute_list.remove(LABEL.mazeIndex);
		xMatrix = removeLabelData(xMatrix, LABEL.pacmanPosition,attribute_list);
		attribute_list.remove(LABEL.pacmanPosition);
		xMatrix = removeLabelData(xMatrix, LABEL.pacmanLivesLeft,attribute_list);
		attribute_list.remove(LABEL.pacmanLivesLeft);
		xMatrix = removeLabelData(xMatrix, LABEL.currentScore,attribute_list);
		attribute_list.remove(LABEL.currentScore);
		/*xMatrix = removeLabelData(xMatrix, LABEL.numOfPillsLeft,attribute_list);
		attribute_list.remove(LABEL.numOfPillsLeft);*/
		xMatrix = removeLabelData(xMatrix, LABEL.numOfPowerPillsLeft,attribute_list);
		attribute_list.remove(LABEL.numOfPowerPillsLeft);
		xMatrix = removeLabelData(xMatrix, LABEL.totalGameTime,attribute_list);
		attribute_list.remove(LABEL.totalGameTime);
		/*xMatrix = removeLabelData(xMatrix, LABEL.currentLevelTime,attribute_list);
		attribute_list.remove(LABEL.currentLevelTime);*/
		/*xMatrix = removeLabelData(xMatrix, LABEL.isInkyEdible,attribute_list);
		attribute_list.remove(LABEL.isInkyEdible);
		xMatrix = removeLabelData(xMatrix, LABEL.isBlinkyEdible,attribute_list);
		attribute_list.remove(LABEL.isBlinkyEdible);
		xMatrix = removeLabelData(xMatrix, LABEL.isPinkyEdible,attribute_list);
		attribute_list.remove(LABEL.isPinkyEdible);
		xMatrix = removeLabelData(xMatrix, LABEL.isSueEdible,attribute_list);
		attribute_list.remove(LABEL.isSueEdible);*/
		xMatrix = removeLabelData(xMatrix, LABEL.numberOfNodesInLevel,attribute_list);
		attribute_list.remove(LABEL.numberOfNodesInLevel);
		xMatrix = removeLabelData(xMatrix, LABEL.numberOfTotalPillsInLevel,attribute_list);
		attribute_list.remove(LABEL.numberOfTotalPillsInLevel);
		xMatrix = removeLabelData(xMatrix, LABEL.numberOfTotalPowerPillsInLevel,attribute_list);
		attribute_list.remove(LABEL.numberOfTotalPowerPillsInLevel);
		//remove all rows that are distant....
		xMatrix = removeAllTuplesWereAllGostsAreFarAway(xMatrix, attribute_list);
		System.out.println("Attribute gain for test set containing "+ xMatrix[0].length+" rows." );
		new ID3AttributeSelectionMethod(true).method(xMatrix, attribute_list);
		 printMatrix(xMatrix,5,attribute_list);
		System.out.println("Accuracy: "+new CalculateAccuracy(SaveTree.loadTree(),xMatrix,attribute_list).getAccuracy());
	}
	


	private Node generateDecisionTree(String[][] d, ArrayList<LABEL> attribute_list, AttributeSelectionMethod att ){
//		1: Create node N.
		
		Node n = new Node();
		currentLevel++;
		nodeCount++;
		n.setNodelevel(currentLevel);
		if (currentLevel>deepestLevel){
			deepestLevel = currentLevel;
		}	
		MOVE leaf = doesAllNodesHaveSameClass(d);
		if(leaf!=null){
			//2. If every tuple in D has the same class C, return N as a leaf node labeled as C.
			n.setAsLeafNode(leaf);
		}else if(attribute_list.size()==1){ 
//			3. Otherwise, if the attribute list is empty, return N as a leaf node labeled with the majority class in D.
			MOVE m = getMajorityClass(d);
			n.setAsLeafNode(m);
		}else{
			//4. Otherwise:
				//1. Call the attribute selection method on D and the attribute list, in order to choose the current attribute A:
				LABEL l = att.method(d, attribute_list);
				//If l==null then there is no gain in selecting any attribute fall back on majority vote,
				if(l==null){
					MOVE m = getMajorityClass(d);
					n.setAsLeafNode(m);
				}else{
					//S(D, attribute list) -> A.
					//2. Label N as A and remove A from the attribute list.
					String[] attributeValues = getLabelAttributeValues(d, l, attribute_list);
					n.setNodeLabel(l);
					ArrayList<LABEL> cloneAttributeList = (ArrayList<LABEL>) attribute_list.clone(); //Remove form attributelist But keep a copy
					cloneAttributeList.remove(l);
					//3. For each value aj in attribute A:
					for (String attributeValue : attributeValues) {
						//a) Separate all tuples in D so that attribute A takes the value aj, creating the subset Dj.
						String[][] dj = selectAttributeValueData(d,l,attribute_list,attributeValue);
						//printMatrix(dj,20,cloneAttributeList);
						//b) If Dj is empty, add a child node to N labeled with the majority class in D.
						if(dj.length<2){  //Only one column the class column is left
							Node child = new Node();
							child.setAsLeafNode(getMajorityClass(d));
							child.setAttributeValue(attributeValue);
							child.setNodelevel(currentLevel+1);
							if (currentLevel+1>deepestLevel){
								deepestLevel = currentLevel+1;
							}
							n.addChildNode(child);
						}else{
							//c) Otherwise, add the resulting node from calling Generate_Tree(Dj, attribute) as a child node to N.
							//remove all attributes with zero gain.
							if(nodeCount==8){
								int k=3;
							}
							Node n3 = generateDecisionTree(dj, cloneAttributeList, att);
							n3.setAttributeValue(attributeValue);
							n.addChildNode(n3);
						}
					}
				}
		}
		if(n.getClassData()==null&&n.getLabelData()==null){
			System.out.println("Node error: "+nodeCount);
		}
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
	
	/** Selects and returns all tuples were a attribute from attribute has a certain value */
	private String[][] selectAttributeValueData(String[][] tuplesAsStrings, LABEL l, ArrayList<LABEL> attribute_list, String value){
		String[][] result = null;
		int column = attribute_list.indexOf(l);
		int rows= 0;
		for (int i = 0; i<tuplesAsStrings[0].length; i++){
			if (tuplesAsStrings[column][i].equals(value)){
				rows++;
			}
		}
		if (column>-1&&tuplesAsStrings!=null){
				result = new String[tuplesAsStrings.length][rows];
				int m=0;
				for (int i = 0; i<tuplesAsStrings[0].length; i++ ){ 
					if (tuplesAsStrings[column][i].equals(value)){
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
	
	
	/** returns all unique values for a label*/ 
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
	
	
   private String[][] addStrategyClass(String[][] d, ArrayList<LABEL> attribute_list) {
	   String[][] d2 = new String[d.length][d[0].length];
	   String[] tuple=new  String[attribute_list.size()];
	   for (int i = 0; i<d[0].length; i++){
				for (int j=0;j<attribute_list.size();j++){
					d2[j][i]= d[j][i];
					tuple[j]=d[j][i];
				}
				//if all ghosts are far away HIGH or VERY_HIGH and not edible return eat pills
				if (ghostsFarAway(tuple, attribute_list)){
					d2[0][i] = STRATEGY.EAT_PILLS.name();
				}else if(allGhostsedible(tuple, attribute_list)){ //if GOASTS is edible ATTACK
					d2[0][i] = STRATEGY.ATTACK.name();
				}else if(anyGhostClose(tuple, attribute_list)){//if some ghosts is VERY_LOW and not edible RUN	
					d2[0][i] = STRATEGY.RUN.name();
				}else{
					d2[0][i] = STRATEGY.NOSTRATEGY.name();
				}
				
				
				//if some ghosts is MEDIUN eat POWERPILL RUN		
				//if GOASTS is edible ATTACK
			}
	   return d2;
   }
   
   
   //ToDo
   private boolean anyGhostClose(String[] tuple, ArrayList<LABEL> attribute_list) {
	   boolean result = false;
	   int inkyColumn = attribute_list.indexOf(LABEL.isInkyEdible);
	   int blinkyColumn = attribute_list.indexOf(LABEL.isBlinkyEdible);
	   int pinkyColumn = attribute_list.indexOf(LABEL.isPinkyEdible);
	   int sueColumn = attribute_list.indexOf(LABEL.isSueEdible);
	   if(tuple[inkyColumn].equals("true")&&tuple[blinkyColumn].equals("true")&&tuple[pinkyColumn].equals("true")&&tuple[sueColumn].equals("true")){
		   result = true;
	   }
	return result;
}



private boolean allGhostsedible(String[] tuple, ArrayList<LABEL> attribute_list) {
	   boolean result = false;
	   int inkyColumn = attribute_list.indexOf(LABEL.isInkyEdible);
	   int blinkyColumn = attribute_list.indexOf(LABEL.isBlinkyEdible);
	   int pinkyColumn = attribute_list.indexOf(LABEL.isPinkyEdible);
	   int sueColumn = attribute_list.indexOf(LABEL.isSueEdible);
	   if(tuple[inkyColumn].equals("true")&&tuple[blinkyColumn].equals("true")&&tuple[pinkyColumn].equals("true")&&tuple[sueColumn].equals("true")){
		   result = true;
	   }
	return result;
}



private boolean ghostsFarAway(String[] tuple, ArrayList<LABEL> attribute_list){
	   boolean result = false;
	   int inkyColumn = attribute_list.indexOf(LABEL.inkyDist);
	   int blinkyColumn = attribute_list.indexOf(LABEL.blinkyDist);
	   int pinkyColumn = attribute_list.indexOf(LABEL.pinkyDist);
	   int sueColumn = attribute_list.indexOf(LABEL.sueDist);
	   boolean inkyFarAway = false;
	   boolean blinkyFarAway = false;
	   boolean pinkyFarAway = false;
	   boolean sueFarAway = false;
	   if(tuple[inkyColumn].equals("VERY_HIGH")||tuple[inkyColumn].equals("HIGH")||tuple[inkyColumn].equals("NONE")){
		   inkyFarAway = true;
	   }
	   if(tuple[blinkyColumn].equals("VERY_HIGH")||tuple[blinkyColumn].equals("HIGH")||tuple[blinkyColumn].equals("NONE")){
		   blinkyFarAway = true;
	   }
	   if(tuple[pinkyColumn].equals("VERY_HIGH")||tuple[pinkyColumn].equals("HIGH")||tuple[pinkyColumn].equals("NONE")){
		   pinkyFarAway = true;
	   }
	   if(tuple[sueColumn].equals("VERY_HIGH")||tuple[sueColumn].equals("HIGH")||tuple[sueColumn].equals("NONE")){
		   sueFarAway = true;
	   }
	   if(inkyFarAway&&blinkyFarAway&&pinkyFarAway&&sueFarAway){
		   result = true;
	   }
	  return result;
   }
   private String[][] removeAllTuplesWereAllGostsAreFarAway(String[][] d, ArrayList<LABEL> attribute_list){
	   String[][] result =null;
	   int inkyColumn = attribute_list.indexOf(LABEL.inkyDist);
	   int blinkyColumn = attribute_list.indexOf(LABEL.blinkyDist);
	   int pinkyColumn = attribute_list.indexOf(LABEL.pinkyDist);
	   int sueColumn = attribute_list.indexOf(LABEL.sueDist);
		int rows= 0;
		for (int i = 0; i<d[0].length; i++){
			if (d[inkyColumn][i].equals("VERY_LOW")||d[blinkyColumn][i].equals("VERY_LOW")||d[pinkyColumn][i].equals("VERY_LOW")||d[sueColumn][i].equals("VERY_LOW")){
				rows++;
			}
		}
		result = new String[attribute_list.size()][rows];
		int k =0;
		for (int i = 0; i<d[0].length; i++){
			if (d[inkyColumn][i].equals("VERY_LOW")||d[blinkyColumn][i].equals("VERY_LOW")||d[pinkyColumn][i].equals("VERY_LOW")||d[sueColumn][i].equals("VERY_LOW")){
				for (int j=0;j<attribute_list.size();j++){
					result[j][k]=d[j][i];	
				}
				k++;
			}
		}
	   return result;
   }
	
//	String[][] deepCopy(String[][] matrix) {
//	    return java.util.Arrays.stream(matrix).map(el -> el.clone()).toArray($ -> matrix.clone());
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
		System.out.println("Number rows in total: "+stringMatrix[0].length+ " number columns: "+stringMatrix.length);
	}
}



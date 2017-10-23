package decisiontree;

import java.awt.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;
import dataRecording.DataTuple.DiscreteTag;
import decisiontree.Constants.LABEL;
import decisiontree.Constants.STRATEGY;
import pacman.game.Constants.MOVE;

public class TrainModel {
    public static Node rootNode; 
    private int currentLevel;
    private int deepestLevel;
	private int nodeCount;
	private double[] learningRate;
	private int[] numberTuples;
	/**
	 * 
	 */
	public TrainModel() {
        ArrayList<LABEL> attribute_list = Constants.getAllLabels();
        //Run a test on Gain on top level for the dataset.
        String[][] tMatrix = Constants.tupleMatrix;
        tMatrix = addStrategyClass(tMatrix,attribute_list);
		//printMatrix(tMatrix,5,attribute_list);
		/*for (String string : getLabelAttributeValues(tMatrix, LABEL.DirectionChosen, attribute_list)) {
			System.out.println("LABEL.DirectionChosen: "+string);
		}
        printMatrix(tMatrix,5,attribute_list);
        new ID3AttributeSelectionMethod(true).method(tMatrix, attribute_list);
        System.out.println();
        */
        //Build Decision Tree
		tMatrix = removeLabelData(tMatrix, LABEL.currentLevel, attribute_list);
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
		tMatrix = removeLabelData(tMatrix, LABEL.currentLevelTime,attribute_list);
		attribute_list.remove(LABEL.currentLevelTime);
		tMatrix = removeLabelData(tMatrix, LABEL.numOfPillsLeft,attribute_list);
		attribute_list.remove(LABEL.numOfPillsLeft);
		tMatrix = removeLabelData(tMatrix, LABEL.numOfPowerPillsLeft,attribute_list);
		attribute_list.remove(LABEL.numOfPowerPillsLeft);
		tMatrix = removeLabelData(tMatrix, LABEL.inkyDir,attribute_list);
		attribute_list.remove(LABEL.inkyDir);
		tMatrix = removeLabelData(tMatrix, LABEL.blinkyDir,attribute_list);
		attribute_list.remove(LABEL.blinkyDir);
		tMatrix = removeLabelData(tMatrix, LABEL.pinkyDir,attribute_list);
		attribute_list.remove(LABEL.pinkyDir);
		tMatrix = removeLabelData(tMatrix, LABEL.sueDir,attribute_list);
		attribute_list.remove(LABEL.sueDir);
		tMatrix = removeLabelData(tMatrix, LABEL.numberOfNodesInLevel,attribute_list);
		attribute_list.remove(LABEL.numberOfNodesInLevel);
		tMatrix = removeLabelData(tMatrix, LABEL.numberOfTotalPillsInLevel,attribute_list);
		attribute_list.remove(LABEL.numberOfTotalPillsInLevel);
		tMatrix = removeLabelData(tMatrix, LABEL.numberOfTotalPowerPillsInLevel,attribute_list);
		attribute_list.remove(LABEL.numberOfTotalPowerPillsInLevel);
		//remove all rows that are distant....
		printMatrix(tMatrix,50,attribute_list);
		/*
		for (String string : getLabelAttributeValues(tMatrix, LABEL.DirectionChosen, attribute_list)) {
			System.out.println("LABEL.DirectionChosen: "+string);
		}
		System.out.println("Attribute gain for training set containing "+ tMatrix[0].length+" rows." );
		new ID3AttributeSelectionMethod(true).method(tMatrix, attribute_list);
		printMatrix(tMatrix,5,attribute_list);
		 */
		//Test accuracy on test set
		 ArrayList<LABEL> attribute_list2 = Constants.getAllLabels();
		String[][] testMatrix = Constants.testMatrix;	
		testMatrix = addStrategyClass(testMatrix,attribute_list2);
		printMatrix(testMatrix,5,attribute_list2);
		for (String string : getLabelAttributeValues(testMatrix, LABEL.DirectionChosen, attribute_list2)) {
			System.out.println("LABEL.DirectionChosen: "+string);
		}
		testMatrix = removeLabelData(testMatrix, LABEL.currentLevel, attribute_list2);
		attribute_list2.remove(LABEL.currentLevel);
		testMatrix = removeLabelData(testMatrix, LABEL.mazeIndex,attribute_list2);
		attribute_list2.remove(LABEL.mazeIndex);
		testMatrix = removeLabelData(testMatrix, LABEL.pacmanPosition,attribute_list2);
		attribute_list2.remove(LABEL.pacmanPosition);
		testMatrix = removeLabelData(testMatrix, LABEL.pacmanLivesLeft,attribute_list2);
		attribute_list2.remove(LABEL.pacmanLivesLeft);
		testMatrix = removeLabelData(testMatrix, LABEL.currentScore,attribute_list2);
		attribute_list2.remove(LABEL.currentScore);
		testMatrix = removeLabelData(testMatrix, LABEL.numOfPillsLeft,attribute_list2);
		attribute_list2.remove(LABEL.numOfPillsLeft);
		testMatrix = removeLabelData(testMatrix, LABEL.numOfPowerPillsLeft,attribute_list2);
		attribute_list2.remove(LABEL.numOfPowerPillsLeft);
		testMatrix = removeLabelData(testMatrix, LABEL.totalGameTime,attribute_list2);
		attribute_list2.remove(LABEL.totalGameTime);
		testMatrix = removeLabelData(testMatrix, LABEL.currentLevelTime,attribute_list2);
		attribute_list2.remove(LABEL.currentLevelTime);
		testMatrix = removeLabelData(testMatrix, LABEL.inkyDir,attribute_list2);
		attribute_list2.remove(LABEL.inkyDir);
		testMatrix = removeLabelData(testMatrix, LABEL.blinkyDir,attribute_list2);
		attribute_list2.remove(LABEL.blinkyDir);
		testMatrix = removeLabelData(testMatrix, LABEL.pinkyDir,attribute_list2);
		attribute_list2.remove(LABEL.pinkyDir);
		testMatrix = removeLabelData(testMatrix, LABEL.sueDir,attribute_list2);
		attribute_list2.remove(LABEL.sueDir);
		testMatrix = removeLabelData(testMatrix, LABEL.numberOfNodesInLevel,attribute_list2);
		attribute_list2.remove(LABEL.numberOfNodesInLevel);
		testMatrix = removeLabelData(testMatrix, LABEL.numberOfTotalPillsInLevel,attribute_list2);
		attribute_list2.remove(LABEL.numberOfTotalPillsInLevel);
		testMatrix = removeLabelData(testMatrix, LABEL.numberOfTotalPowerPillsInLevel,attribute_list2);
		attribute_list2.remove(LABEL.numberOfTotalPowerPillsInLevel);
		System.out.println("Attribute gain for test set containing "+ testMatrix[0].length+" rows." );
		new ID3AttributeSelectionMethod(true).method(testMatrix, attribute_list2);
		 printMatrix(testMatrix,5,attribute_list2);
		 //
		 /*
		 //exportData(tMatrix,attribute_list);
		 String[][] trainingMatrix = getRandomOfRows(100,tMatrix );
		 printMatrix(trainingMatrix,20,attribute_list);
		 String[][] selectedTestMatrix = getRandomOfRows(10,testMatrix );
		 printMatrix(trainingMatrix,20,attribute_list);
		 rootNode = generateDecisionTree(trainingMatrix, attribute_list, new ID3AttributeSelectionMethod(false));
			System.out.println("DeepestLevel: "+deepestLevel+ " number of nodes: "+nodeCount);
			SaveTree.saveTree(rootNode);
			System.out.println("Accuracy: "+new CalculateAccuracy(SaveTree.loadTree(),selectedTestMatrix,attribute_list2).getAccuracy());
			createAndClearFile();
			for (int i = 10;i<tMatrix[0].length/10;i=i+20){
				trainingMatrix = getRandomOfRows(i,tMatrix);
				rootNode = generateDecisionTree(trainingMatrix, attribute_list, new ID3AttributeSelectionMethod(false));
				System.out.println("DeepestLevel: "+deepestLevel+ " number of nodes: "+nodeCount);
				SaveTree.saveTree(rootNode);
				int testRows = i*2/10;
				if (testRows>testMatrix[0].length){
					testRows = testMatrix[0].length;
				}
				selectedTestMatrix = getRandomOfRows(testRows,testMatrix );
				double accuracy = new CalculateAccuracy(SaveTree.loadTree(),selectedTestMatrix,attribute_list2).getAccuracy();
				System.out.println("Accuracy: "+accuracy);
				printResultToFile(i,accuracy,nodeCount,deepestLevel);
			}*/
			
		exportData(tMatrix,attribute_list,"trainingData.txt");
		exportData(testMatrix,attribute_list,"testData.txt");
	}
	
	
	private void exportData(String[][] input,ArrayList<LABEL> attribute_list,String filename){
		String path = "../DecisionTreeTest/"+filename;
//		try {
//			Files.deleteIfExists(Paths.get(path));
//		}catch (IOException e) {
//		    System.out.println(e.toString());
//		}
//		try {
//			Files.createFile(Paths.get(path));
//		}catch (IOException e) {
//		    System.out.println(e.toString());
//		}
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
					String k = String.valueOf(convertToNumber(input[j][i],attribute_list.get(j)));
					s=s+k;
					if(j<input.length-1){
						s=s+",";
					}
				}
				s=s+"\n";
				//try {
					output.append(s);
				   // Files.write(Paths.get(path), s.getBytes(), StandardOpenOption.APPEND);
				//}catch (IOException e) {
				 //   System.out.println(e.toString());
				//}
			}
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private int convertToNumber(String value, LABEL label) {
		int result = -1;
		if (label.equals(LABEL.inkyDist)||label.equals(LABEL.pinkyDist)||label.equals(LABEL.blinkyDist)||label.equals(LABEL.sueDist)){
			switch (value) {
			case "VERY_LOW":
				result=1;
				break;
			case "LOW":
				result=2;
				break;
			case "MEDIUM":
				result=3;
				break;
			case "HIGH":
				result=4;
				break;
			case "VERY_HIGH":
				result=5;
				break;
			case "NONE":
				result=0;
				break;
			default:
				result=-1;
				break;
			}
		}
		if (label.equals(LABEL.isInkyEdible)||label.equals(LABEL.isPinkyEdible)||label.equals(LABEL.isBlinkyEdible)||label.equals(LABEL.isSueEdible)){
			switch (value) {
			case "false":
				result=0;
				break;
			case "true":
				result=1;
				break;
			default:
				result=-1;
				break;
			}
		}
		if (label.equals(LABEL.DirectionChosen)){
			switch (value) {
			case "ATTACK":
				result = 1;
				break;
			case "EAT_PILLS":
				result = 2;
				break;	
			case "EAT_POWER_PILLS":
				result = 3;		
				break;	
			case "RUN":
				result = 4;
				break;	
			case "NOSTRATEGY":
				result = 5;
				break;	
			default:
				result=-1;
				break;
			}
		}
		return result;
	}


	private void createAndClearFile() {
		try {
			Files.createFile(Paths.get("myData/katjong.txt"));
		}catch (IOException e) {
		    System.out.println(e.toString());
		}
	} 
	private void printResultToFile(int numberNodes, double accuracy, int nodeCount, int deepestLevel) {
		String s =""+numberNodes+","+accuracy+","+nodeCount+","+deepestLevel+"\n";
		try {
		    Files.write(Paths.get("myData/katjong.txt"), s.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {
		    System.out.println(e.toString());
		}
	}


	private String[][] getRandomOfRows(int rows, String[][] d){
		Random rnd=new Random();
		ArrayList<Integer> selected = new ArrayList<Integer>();
		int selectedrow = 0;
		String[][] result = new String[d.length][rows];
		while (selectedrow<rows){
			int rndInt = rnd.nextInt(d[0].length);
			if(rows<(d[0].length*3/4)){
				while (selected.contains(rndInt)){
					rndInt = rnd.nextInt(d[0].length); //Danger
				}
				selected.add(rndInt);
			}
			for(int i=0; i<d.length;i++){
				result[i][selectedrow] = d[i][rndInt];
			}
			selectedrow++;
		}
		return result;
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
		STRATEGY leaf = doesAllNodesHaveSameClass(d);
		if(leaf!=null){
			//2. If every tuple in D has the same class C, return N as a leaf node labeled as C.
			n.setAsLeafNode(leaf);
		}else if(attribute_list.size()==1){ 
//			3. Otherwise, if the attribute list is empty, return N as a leaf node labeled with the majority class in D.
			STRATEGY m = getMajorityClass(d);
			n.setAsLeafNode(m);
		}else{
			//4. Otherwise:
				//1. Call the attribute selection method on D and the attribute list, in order to choose the current attribute A:
				LABEL l = att.method(d, attribute_list);
				//If l==null then there is no gain in selecting any attribute fall back on majority vote,
				if(l==null){
					STRATEGY m = getMajorityClass(d);
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
	

	private STRATEGY doesAllNodesHaveSameClass(String[][] d) {
		STRATEGY l = null;
		if (d.length>0){
			l=STRATEGY.valueOf(d[0][0]); //get the first class
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
	
	private STRATEGY getMajorityClass(String[][] tuplesAsStrings){
		STRATEGY result = null;
		int numberAttack=0;
		int numberEatPills=0;
		int numberEatPowerPills=0;
		int numberRun=0;
		int numberNoStrategy=0;
		if (tuplesAsStrings!=null && tuplesAsStrings.length>0){
			for (int i = 0; i<tuplesAsStrings[0].length;i++){
				switch (STRATEGY.valueOf(tuplesAsStrings[0][i])) {
				case ATTACK:
					numberAttack++;
					break;
				case EAT_PILLS:
					numberEatPills++;
					break;	
				case EAT_POWER_PILLS:
					numberEatPowerPills++;
					break;	
				case RUN:
					numberRun++;
					break;	
				case NOSTRATEGY:
					numberNoStrategy++;
					break;	
				default:
					break;
				}
			}
			int[] all = {numberAttack,numberEatPills,numberEatPowerPills,numberRun,numberNoStrategy};
			int index = -1;
			for (int i=0; i <all.length;i++) {
				if (all[i]>index){
					index = i;
				}
			}
			switch (index) {
			case 0:
				result = STRATEGY.ATTACK;
				break;
			case 1:
				result = STRATEGY.EAT_PILLS;
				break;
			case 2:
				result = STRATEGY.EAT_POWER_PILLS;
				break;
			case 3:
				result = STRATEGY.RUN;
				break;
			case 4:
				result = STRATEGY.NOSTRATEGY;
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
				if (ghostsFarAway(tuple, attribute_list)){
					d2[0][i] = STRATEGY.EAT_PILLS.name();
				}else if(allGhostsedible(tuple, attribute_list)){ //if GOASTS is edible ATTACK
					d2[0][i] = STRATEGY.ATTACK.name();
				}else if(anyGhostClose(tuple, attribute_list)){//if some ghosts is VERY_LOW and not edible RUN	
					d2[0][i] = STRATEGY.RUN.name();
				}else{
					d2[0][i] = STRATEGY.NOSTRATEGY.name();
				}
			}
	   return d2;
   }
   
   
   //ToDo
   private boolean anyGhostClose(String[] tuple, ArrayList<LABEL> attribute_list) {
	   boolean result = false;
	   int inkyColumn = attribute_list.indexOf(LABEL.inkyDist);
	   int blinkyColumn = attribute_list.indexOf(LABEL.blinkyDist);
	   int pinkyColumn = attribute_list.indexOf(LABEL.pinkyDist);
	   int sueColumn = attribute_list.indexOf(LABEL.sueDist);
	   boolean inkyClose = false;
	   boolean blinkyClose = false;
	   boolean pinkyClose = false;
	   boolean sueClose = false;
	   if(tuple[inkyColumn].equals("VERY_LOW")||tuple[inkyColumn].equals("LOW")){
		   inkyClose = true;
	   }
	   if(tuple[blinkyColumn].equals("VERY_LOW")||tuple[blinkyColumn].equals("LOW")){
		   blinkyClose = true;
	   }
	   if(tuple[pinkyColumn].equals("VERY_LOW")||tuple[pinkyColumn].equals("LOW")){
		   pinkyClose = true;
	   }
	   if(tuple[sueColumn].equals("VERY_LOW")||tuple[sueColumn].equals("LOW")){
		   sueClose = true;
	   }
	   if(inkyClose||blinkyClose||pinkyClose||sueClose){
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
				String s = attribute_list.get(i).toString();
				if (s.equals("DirectionChosen")){
					s="StrategyClass";
				}
				System.out.print(s+"|");
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



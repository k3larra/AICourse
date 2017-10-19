package decisiontree;

import java.util.ArrayList;

import decisiontree.Constants.LABEL;
import pacman.game.Constants.MOVE;

public class CalculateAccuracy {
	private String[][] testSet;
	private Node tree;
	private ArrayList<LABEL> attribute_list;
	public CalculateAccuracy(Node tree,String[][] testSet, ArrayList<LABEL> attribute_list ) {
		this.testSet = testSet;
		this.tree = tree;
		this.attribute_list = attribute_list;
	}
	
	//HEre accuracy means that DirectionsChosen is not the same as to a ghost with distance VERY_LOW
	public double getAccuracy(){
		String[] row;
		int hits = 0;
		int inkyColumn = attribute_list.indexOf(LABEL.inkyDist);
		int blinkyColumn = attribute_list.indexOf(LABEL.blinkyDist);
		int pinkyColumn = attribute_list.indexOf(LABEL.pinkyDist);
		int sueColumn = attribute_list.indexOf(LABEL.sueDist);
		for (int i = 0; i<testSet[0].length;i++){
			row = new String[testSet.length];
			for (int j = 0; j <testSet.length; j++){
				row[j]=testSet[j][i];
			}
			MOVE m = getMove(tree,row);
			if (m!=null){
				//find closest Ghost
				
				if((row[0]).equals(m.toString())){
					hits++;
				}
			}
		}
		double result = (double)hits/(double)testSet[0].length;
		long resultInt =  Math.round(result*100);
		return result = (double)resultInt/100;
	}
	
	private MOVE getMove(Node n,String[] row){
		MOVE m=null;
		if(n.isLeafNode()){
			m=n.getClassData();
		}else{
			int attribute = attribute_list.indexOf(n.getLabelData());
			for (Node n2 : n.getChildren()) {
				if(row[attribute].equals(n2.getAttrValue())){
					m= getMove(n2,row);
				}
			}
		}
		return m;
	}
}

package decisiontree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import dataRecording.DataTuple;
import decisiontree.Constants.LABEL;
import decisiontree.Constants.LEAF;
import pacman.game.Constants.MOVE;

public class Node implements Serializable{
	 private LABEL labelData;
	 private MOVE classData;
	 private Collection<Node> myChildren = new ArrayList<Node>();
	 public Node() {
	 }

	 public boolean isLeafNode(){
		 if(classData!=null){
			 return true;
		 }else {
			 return false;
		 }
	 }
	 
	 public MOVE getClassData(){
		 return classData;
	 }
	 
	 public LABEL getLabelData(){
		 return labelData;
	 }
	
	 public Collection<Node> getChildren(){
		 return myChildren;
	 }
	 
	 public void setChildren(Collection<Node> children){
		 myChildren = children;
	 }
	 
	 public void setAsLeafNode(MOVE leaf){
		 classData = leaf;
	 }

	public void setNodeLabel(LABEL l) {
		labelData = l;
		
	}

	public void addChildNode(Node child) {
		myChildren.add(child);
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "HEpp0";
	}
	
	
	public void printAllLowerNodes() {
		if (this.isLeafNode()){
			System.out.print("|Class: ");
			System.out.println(this.getClassData());
		}else{
			System.out.print("|Attribute: ");
			System.out.println(this.getLabelData());
			for (Node node : myChildren) {
				node.printAllLowerNodes();
			}
		}
		
	}
	
	
}

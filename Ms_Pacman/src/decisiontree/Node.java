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
	 private String attrValue;
	 private int nodelevel;
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
	}
	
	public void addChildNodeAndAttributeValue(Node child, String attrValue) {
		myChildren.add(child);
		this.attrValue= attrValue;
	}
	
	public String getAttrValue() {
			return attrValue;
	}

	@Override
	public String toString() {
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

	public void setAttributeValue(String attributeValue) {
		this.attrValue = attributeValue;
	}

	public int getNodelevel() {
		return nodelevel;
	}

	public void setNodelevel(int nodelevel) {
		this.nodelevel = nodelevel;
	}

	public void printNodeInfo() {
		// TODO Auto-generated method stubif (this.isLeafNode()){
		if (this.isLeafNode()){
			System.out.print("LEAF node: ");
			System.out.println(this.getClassData());
		}else{
			System.out.print("Attribute node: ");
			System.out.println(this.getLabelData());
			System.out.print("Attribute values for children: ");
			for (Node node : myChildren) {
				System.out.print(node.getAttrValue()+ " : ");
			}
			System.out.println();
		}
	}
	
	
	
}

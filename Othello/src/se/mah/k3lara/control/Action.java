package se.mah.k3lara.control;


public class Action {
	private int row;
	private int column;
	public Action(int row,int column){
		this.row = row;
		this.column = column;
	}
	public int getRow() {
		return this.row;
	}
	public int getColumn() {
		return this.column;
	}
}
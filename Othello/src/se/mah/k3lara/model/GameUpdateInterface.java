package se.mah.k3lara.model;

public interface GameUpdateInterface {
	public void setGamePiece(int row,int column, ItemState state);
	public void printInformation(String txt);
}

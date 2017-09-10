package se.mah.k3lara.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import se.mah.k3lara.Settings;
import se.mah.k3lara.control.Action;
import se.mah.k3lara.control.Controller;
import se.mah.k3lara.control.Rules;
import se.mah.k3lara.model.Game;
import se.mah.k3lara.model.ItemState;

public class Piece implements ActionListener, MouseListener{
	private GameBoard gameBoard;
	private ItemState state = ItemState.EMPTY;
	private JButton jButton;
	private int row;
	private int column;
	private boolean mouseOver = false;
	public Piece(GameBoard gameBoard, JButton jButton,int row, int column){
		this.gameBoard = gameBoard;
		this.jButton = jButton;
		this.jButton.setBorderPainted(false);
		this.jButton.addActionListener(this);
		this.jButton.addMouseListener(this);
		if(Settings.nbrRowsColumns==8){
			this.jButton.setIcon(new ImageIcon(Game.class.getResource("/se/mah/k3lara/resources/grassmall.jpg")));
		}else{
			this.jButton.setIcon(new ImageIcon(Game.class.getResource("/se/mah/k3lara/resources/grass.jpg")));
		}
		Game.getInstance().setStateUp(row, column, ItemState.EMPTY);
		//Crap
		this.jButton.setBackground(new Color(71,130,12));
		this.row = row;
		this.column = column;
		if(Settings.nbrRowsColumns/2-1==row&&Settings.nbrRowsColumns/2-1==column){
			if(Settings.nbrRowsColumns==8){
				this.jButton.setIcon(new ImageIcon(Game.class.getResource("/se/mah/k3lara/resources/whitesmall.jpg")));
			}else{
				this.jButton.setIcon(new ImageIcon(Game.class.getResource("/se/mah/k3lara/resources/white.jpg")));
			}
			Game.getInstance().setStateUp(row, column, ItemState.WHITE);
			state= ItemState.WHITE;
		}
		if(Settings.nbrRowsColumns/2==row&&Settings.nbrRowsColumns/2==column){
			if(Settings.nbrRowsColumns==8){
				this.jButton.setIcon(new ImageIcon(Game.class.getResource("/se/mah/k3lara/resources/whitesmall.jpg")));
			}else{
				this.jButton.setIcon(new ImageIcon(Game.class.getResource("/se/mah/k3lara/resources/white.jpg")));
			}
			Game.getInstance().setStateUp(row, column, ItemState.WHITE);
			state= ItemState.WHITE;
		}
		if(Settings.nbrRowsColumns/2-1==row&&Settings.nbrRowsColumns/2==column){
			if(Settings.nbrRowsColumns==8){
				this.jButton.setIcon(new ImageIcon(Game.class.getResource("/se/mah/k3lara/resources/blacksmall.jpg")));
			}else{
				this.jButton.setIcon(new ImageIcon(Game.class.getResource("/se/mah/k3lara/resources/black.jpg")));
			}
			Game.getInstance().setStateUp(row, column, ItemState.BLACK);
			state= ItemState.BLACK;
		}
		if(Settings.nbrRowsColumns/2==row&&Settings.nbrRowsColumns/2-1==column){
			if(Settings.nbrRowsColumns==8){
				this.jButton.setIcon(new ImageIcon(Game.class.getResource("/se/mah/k3lara/resources/blacksmall.jpg")));
			}else{
				this.jButton.setIcon(new ImageIcon(Game.class.getResource("/se/mah/k3lara/resources/black.jpg")));
			}
			Game.getInstance().setStateUp(row, column, ItemState.BLACK);
			state= ItemState.BLACK;
		}
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {

		if(this.state==ItemState.EMPTY||mouseOver){
			if(Rules.getAllTurnablePiecesFromThisNewPiece(Game.getInstance().getGameStateClone(),new Action(row,column), Settings.humanPlayerMin).size()>0){
				     Controller.getInstance().nextMove(row, column,state,Settings.humanPlayerMin);	
						mouseOver=false;
			 }else{
				 gameBoard.printInfo("Looks like you have nowhere to move press the button...");
					mouseOver=false;
			 }
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		if(Game.getInstance().getGameStateClone()[row][column]==0){
			if(Game.getInstance().getGameStateClone()[row][column]!=0){
				System.out.println("hoppsan");
			}
			if(Rules.getAllTurnablePiecesFromThisNewPiece(Game.getInstance().getGameStateClone(),new Action(row,column), Settings.humanPlayerMin).size()>0){
				switch(Settings.humanPlayerMin){
					case BLACK:
						setBlack();
						mouseOver = true;
					break;
					case WHITE:
						setWhite();
						mouseOver  = true;
					break;
					case EMPTY:
					break;
				}
			}
		}else{
			mouseOver = false;
		}
	}
	@Override
	public void mouseExited(MouseEvent e) {
		if(mouseOver){
			setEmpty();
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void setBlack(){
		if(Settings.nbrRowsColumns==8){
			this.jButton.setIcon(new ImageIcon(Game.class.getResource("/se/mah/k3lara/resources/blacksmall.jpg")));
		}else{
			this.jButton.setIcon(new ImageIcon(Game.class.getResource("/se/mah/k3lara/resources/black.jpg")));
		}
		state= ItemState.BLACK;
	}
	
	public void setWhite(){
		if(Settings.nbrRowsColumns==8){
			this.jButton.setIcon(new ImageIcon(Game.class.getResource("/se/mah/k3lara/resources/whitesmall.jpg")));
		}else{
			this.jButton.setIcon(new ImageIcon(Game.class.getResource("/se/mah/k3lara/resources/white.jpg")));
		}
		state= ItemState.WHITE;
	}
	public void setEmpty(){
		if(Settings.nbrRowsColumns==8){
			this.jButton.setIcon(new ImageIcon(Game.class.getResource("/se/mah/k3lara/resources/grassmall.jpg")));
		}else{
			this.jButton.setIcon(new ImageIcon(Game.class.getResource("/se/mah/k3lara/resources/grass.jpg")));
		}
		state= ItemState.EMPTY;
	}
}

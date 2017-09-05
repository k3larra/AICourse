package se.mah.k3lara.view;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.invoke.SwitchPoint;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import se.mah.k3lara.Settings;
import se.mah.k3lara.control.Controller;
import se.mah.k3lara.control.OutputLevel;
import se.mah.k3lara.model.Game;
import se.mah.k3lara.model.ItemState;
import se.mah.k3lara.model.tryit;

public class Piece implements ActionListener, MouseListener{
	private GameBoard gameBoard;
	private ItemState state = ItemState.EMPTY;
	private JButton jButton;
	private int row;
	private int column;
	public Piece(GameBoard gameBoard, JButton jButton,int row, int column){
		this.gameBoard = gameBoard;
		this.jButton = jButton;
		this.jButton.addActionListener(this);
		this.jButton.addMouseListener(this);
		this.jButton.setIcon(new ImageIcon(Game.class.getResource("/se/mah/k3lara/resources/grass.jpg")));
		//Crap
		this.jButton.setBackground(new Color(71,130,12));
		this.row = row;
		this.column = column;
		Game.getInstance().setState(row, column, ItemState.EMPTY);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		 //gameBoard.printInfo("Clicked row:"+row+" column: "+column);
		if(this.state==ItemState.EMPTY){
		 Controller.getInstance().nextMove(row, column,state,Settings.humanPlayerMin);
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		  //gameBoard.printInfo("Entered row:"+row+" column: "+column);
		  //setWhite();
	}
	@Override
	public void mouseExited(MouseEvent e) {
		 //gameBoard.printInfo("Exited row:"+row+" column: "+column);
		 //setEmpty();
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
		//Image not centered!!!!!
			this.jButton.setIcon(getImage(ItemState.BLACK));
			state= ItemState.BLACK;
	}
	
	public void setWhite(){
		//Image not centered!!!!!
		this.jButton.setIcon(getImage(ItemState.WHITE));
		state= ItemState.WHITE;
	}
	public void setEmpty(){
		//Image not centered!!!!!
		this.jButton.setIcon(getImage(ItemState.EMPTY));
		state= ItemState.EMPTY;
	}
	
	private ImageIcon getImage(ItemState state){
		 BufferedImage bI = null;
		 Image i2 = null;
		 ImageIcon ia = null;
			try {
				switch (state) {
				case EMPTY:
					bI = ImageIO.read(Game.class.getResource("/se/mah/k3lara/resources/grass.jpg"));
					break;
				case BLACK:
					bI = ImageIO.read(Game.class.getResource("/se/mah/k3lara/resources/black.jpg"));
					break;
				case WHITE:
					bI = ImageIO.read(Game.class.getResource("/se/mah/k3lara/resources/white.jpg"));
					break;
				default:
					break;
				}
				i2 = bI.getScaledInstance(this.jButton.getWidth(), this.jButton.getHeight(), java.awt.Image.SCALE_SMOOTH);
				ia = new ImageIcon(i2);
			} catch (IOException e2) {
				gameBoard.printInfo("No image found");
			}
			return ia;
	}
}

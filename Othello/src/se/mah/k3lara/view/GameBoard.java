package se.mah.k3lara.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import se.mah.k3lara.Settings;
import se.mah.k3lara.control.Controller;
import se.mah.k3lara.control.OutputLevel;
import se.mah.k3lara.model.Game;
import se.mah.k3lara.model.GameUpdateInterface;
import se.mah.k3lara.model.ItemState;

import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.JScrollPane;


public class GameBoard extends JFrame implements GameUpdateInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel gamePanel;
	private JPanel controlPanel;
	private JTextArea outputArea;
	private static Piece[][] gameBoardUI;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameBoard frame = new GameBoard();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GameBoard() {
		//Give GAme a ref
		gameBoardUI = new Piece[Settings.nbrRowsColumns][Settings.nbrRowsColumns];
		Game.getInstance().setGameRef(this);
		Controller.getInstance().setGameRef(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 840);
		this.setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		//Create the GameBoard
		gamePanel = new JPanel();
	    //gamePanel.setSize(800,800);
		//gamePanel.setMinimumSize(new Dimension(800,800));
		//gamePanel.setMaximumSize(new Dimension(800,800));
		//gamePanel.setPreferredSize(new Dimension(800,800));
		gamePanel.setBackground(Color.BLACK);
		contentPane.add(gamePanel, BorderLayout.CENTER);
		gamePanel.setLayout(new GridLayout(Settings.nbrRowsColumns, Settings.nbrRowsColumns, 2, 2));
		JButton jButton;
		for (int i = 0; i <Settings.nbrRowsColumns; i++){
			for (int j = 0; j<Settings.nbrRowsColumns;j++){
				jButton = new JButton("i:"+i+" j:"+j);
				gamePanel.add(jButton);
				gameBoardUI[i][j] = new Piece(this,jButton,i,j);
			}
		}
		
		
		//Infopanel
		controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(400, 600));
		controlPanel.setMinimumSize(new Dimension(400, 600));
		controlPanel.setBackground(Color.PINK);
		//contentPane.add(panel, BorderLayout.WEST);
		controlPanel.setLayout(new BorderLayout(0, 0));
		outputArea = new JTextArea();
		//textArea.setRows(4);
		JScrollPane scrollPane = new JScrollPane(outputArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		controlPanel.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(controlPanel, BorderLayout.EAST);
		printInfo("Game started");

		
	}


	public void printInfo(final String text) {
		  SwingUtilities.invokeLater(new Runnable() {
			    @Override
			    public void run() {
			    	outputArea.append(" > "+text+"\n");
			    	outputArea.setCaretPosition(outputArea.getDocument().getLength());
			    }
			});
	}

	@Override
	public void setGamePiece(int row, int column, ItemState state) {
		System.out.println("setGamePiece "+System.currentTimeMillis());
		switch (state) {
		case BLACK:
			gameBoardUI[row][column].setBlack();
			break;
		case WHITE:
			gameBoardUI[row][column].setWhite();;
			break;
		default:
			break;
		}
	}

	@Override
	public void printInformation(String txt) {
		printInfo(txt);
		
	}




}

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
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;
import java.awt.Font;


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
	private JPanel settings;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JButton btnNewButton;
	private JComboBox<String> comboBox;

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
		gamePanel.setBackground(Color.BLACK);
		contentPane.add(gamePanel, BorderLayout.CENTER);
		gamePanel.setLayout(new GridLayout(Settings.nbrRowsColumns, Settings.nbrRowsColumns, 2, 2));
		JButton jButton;
		for (int i = 0; i <Settings.nbrRowsColumns; i++){
			for (int j = 0; j<Settings.nbrRowsColumns;j++){
				jButton = new JButton("");
				gamePanel.add(jButton);
				gameBoardUI[i][j] = new Piece(this,jButton,i,j);
			}
		}
		
		
		
		
		//Infopanel
		controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(400, 600));
		//controlPanel.setMinimumSize(new Dimension(400, 600));
		controlPanel.setBackground(Color.PINK);
		//contentPane.add(panel, BorderLayout.WEST);
		controlPanel.setLayout(new BorderLayout(0, 0));
		outputArea = new JTextArea();
		outputArea.setEditable(false);
		//textArea.setRows(4);
		JScrollPane scrollPane = new JScrollPane(outputArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		controlPanel.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(controlPanel, BorderLayout.EAST);
		//settings
		settings = new JPanel();
		settings.setBackground(Color.LIGHT_GRAY);
		settings.setPreferredSize(new Dimension(200,200));
		controlPanel.add(settings, BorderLayout.NORTH);
		settings.setLayout(null);
		//scrollPane.setColumnHeaderView(settings);
		
		lblNewLabel = new JLabel("Othello");
		lblNewLabel.setBackground(Color.LIGHT_GRAY);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(12, 9, 150, 16);
		settings.add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("Gamesize");
		lblNewLabel_1.setBounds(12, 112, 56, 16);
		settings.add(lblNewLabel_1);
		
		btnNewButton = new JButton("New game");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Restart all;
				Game.getInstance().clearAndReset();
			}
		});
		btnNewButton.setBounds(12, 162, 135, 25);
		settings.add(btnNewButton);
		
		comboBox = new JComboBox();
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBox.setToolTipText("seet");
		comboBox.setBounds(80, 108, 67, 22);
		comboBox.addItem(new String ("4x4"));
		comboBox.addItem(new String ("8x8"));
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"4x4", "8x8"}));
		comboBox.setSelectedIndex(1);

		settings.add(comboBox);
		
		JTextArea txtrYouAreBlack = new JTextArea();
		txtrYouAreBlack.setLineWrap(true);
		txtrYouAreBlack.setRows(2);
		txtrYouAreBlack.setText("You are black and starts, if unable to move press computers turn.");
		txtrYouAreBlack.setBounds(12, 34, 228, 57);
		settings.add(txtrYouAreBlack);
		
		JButton btnYourTurn = new JButton("I cannot move your turn Computer...");
		btnYourTurn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Controller.getInstance().cannotMoveGiveTurnToComputer();
			}
		});
		btnYourTurn.setBounds(166, 163, 171, 25);
		settings.add(btnYourTurn);		
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
		switch (state) {
		case BLACK:
			gameBoardUI[row][column].setBlack();
			break;
		case WHITE:
			gameBoardUI[row][column].setWhite();
			break;
		case EMPTY:
			gameBoardUI[row][column].setEmpty();;
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

package cis.project1;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import javax.swing.border.BevelBorder;


/**********************************************************************
 * This class is the View/Controller portion of the
 * Model-View-Controller design for a CanadianSalad simulator. It 
 * establishes the panels to display the game board, and listener 
 * logic to control game play.
 *
 * @author Andrew Weston and Christian Thompson
 * @version Summer 2019
 *********************************************************************/
public class Panel extends JPanel {


	
	/** This is the game model the panel depicts. */
	private CanadianSaladModel model;
	
	/** This listener functions as the Controller. */
	private Listener listener;
	
	/** This button array represents the user's hand. */
	private JButton[] handButtons;
	
	/** This button array represents the current trick. */
	private JButton[] trickButtons;
	
	/** This button restarts the game. */
	private JButton newGameButton;
	
	/** This button exits the program. */
	private JButton quitButton;
	
	/** This label array represents the player's scores. */
	private JLabel[] playerScores;
	
	/** This variable represents a full hand with five players. */
	private static final int FULL_HAND_SIZE = 10;
	
	/** This map pairs Card objects to their ImageIcons. */
	private HashMap<Card, ImageIcon> cardToImage;
	
	/** This panel contains game information. */
	private JPanel scorePanel;
	
	/** This panel contains the current trick. */
	private JPanel trickPanel;
	
	/** This panel contains the play area. */
	private JPanel playAreaPanel;
	
	/** This panel represents the user's hand. */
	private JPanel handPanel;
	
	/** This is the number of players in the CanadianSalad game. */
	private int numPlayers;
	
	/** This Card object represents the Card selected from the
	 * user's hand. */
	private Card selectedCard;
	
	/** The Serial Version UID is 1L. */
	private static final long serialVersionUID = 1L;
	
	/** Each ImageIcon represents a card in a standard deck. */
	private ImageIcon c2, c3, c4, c5, c6, c7, c8, c9, c10, cJ, cQ, cK, cA;
	
	/** Each ImageIcon represents a card in a standard deck. */
	private ImageIcon d2, d3, d4, d5, d6, d7, d8, d9, d10, dJ, dQ, dK, dA;
	
	/** Each ImageIcon represents a card in a standard deck. */
	private ImageIcon h2, h3, h4, h5, h6, h7, h8, h9, h10, hJ, hQ, hK, hA;
	
	/** Each ImageIcon represents a card in a standard deck. */
	private ImageIcon s2, s3, s4, s5, s6, s7, s8, s9, s10, sJ, sQ, sK, sA;
	
	/** Each ImageIcon represents a card in a standard deck. */
	private ImageIcon emptyCard;
	
	
	 /*****************************************************************
	 * This class is the View/Controller portion of the
	 * Model-View-Controller design for a CanadianSalad simulator. It 
	 * establishes the panels to display the game board, and listener 
	 * logic to control game play.
     *
     * @param theGame is the CanadianSaladModel model of the game.
     *****************************************************************/
	public Panel(final CanadianSaladModel theGame) {
		model = theGame;

		// store number of players for easy access
		numPlayers = model.getPlayers().size();

		final int rows = 4;
		final int cols = 12;
		this.setLayout(new GridLayout(rows, cols));

		listener = new Listener();

		setupImages();
		setupMap();

		setupPanels();
		addPanels();
	}

	
	/*****************************************************************
     * This helper method adds the component panels to "this" panel.
     *****************************************************************/
	private void addPanels() {
		this.add(scorePanel);
		this.add(trickPanel);
		this.add(playAreaPanel);
		this.add(handPanel);
	}

	
	/*****************************************************************
     * This helper method configures the component panels.
     *****************************************************************/
	private void setupPanels() {
		setupScorePanel();
		setupTrickPanel();
		setupPlayAreaPanel();
		setupHandPanel();
	}

	
	/*****************************************************************
     * This helper method signals the panel should simulate game play.
     *****************************************************************/
	protected void autoPlay() {
		while (!model.isGameComplete()) {
			while (!model.isTrickComplete()) {
				model.nextTurn();
				displayBoard();
				sleep();
				if (model.isUsersTurn()) {
					sleep();
				}
			}
			sleep();
			displayBoard();
			model.scoreHandNoTricks();
			model.setLoserLeadsNextTrick();

			setBlank();
			sleep();
			while (model.isUsersTurn()) {
				// System.out.println("Still user's turn");
				if (model.isGameComplete()) {
					break;
				}
				if (model.getCurrentTrick().getStarter() 
						== model.getUserPlayer()) {
					break;
				}
			}
			if (!model.isGameComplete()) {
				model.nextTurn();
				displayBoard();
				sleep();
			}
		}
	}

	
	/*****************************************************************
     * This helper method simulates delay between turns for thinking
     * and playing.
     *****************************************************************/
	private void sleep() {
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (Exception e) {
			System.out.println("Blew up after sleep");
		}
	}

	
	/*****************************************************************
     * This helper method sets the trick panel buttons to blank cards
     * at the completion of a trick.
     *****************************************************************/
	private void setBlank() {
		for (int e = 0; e < model.getPlayers().size(); e++) {
			trickButtons[e].setIcon(emptyCard);
		}
		trickPanel.repaint();
		this.repaint();
	}

	
	/*****************************************************************
     * This helper method updates the board based on the model state.
     *****************************************************************/
	private void displayBoard() {
		updateHandIcons();
		removeHandButtons();

		if (((model.getCurrentTrick() == null) 
				|| (model.trickCards() == null))) {
			setBlank();
		} else {
			displayButtons();
		}
		updatePlayerScores();
		scorePanel.repaint();
		handPanel.repaint();
		trickPanel.repaint();
		this.repaint();
	}

	
	/*****************************************************************
     * This helper method updates the player scores on the score panel.
     *****************************************************************/
	private void updatePlayerScores() {
		for (int i = 0; i < numPlayers; i++) {
			playerScores[i].setText(model.getPlayers().get(i).
					getName() + ": " + model.getPlayers()
					.get(i).getScore());
			scorePanel.repaint();
		}
	}

	
	/*****************************************************************
     * This helper removes buttons from the handPanel as the user
     * plays cards.
     *****************************************************************/
	private void removeHandButtons() {
		for (int d = model.getUsersHand().size();
				d < FULL_HAND_SIZE; d++) {
			handPanel.remove(handButtons[d]);
		}
	}

	
	/*****************************************************************
     * This helper method updates the handPanel buttons based on the
     * model's state.
     *****************************************************************/
	private void updateHandIcons() {
		for (int c = 0; c < model.getUsersHand().size(); c++) {
			handButtons[c].setIcon(
					cardToImage.get(
							model.getUsersHand()
							.get(c)));
		}
	}

	
	/*****************************************************************
     * This helper method updates the buttons on the trick panel.
     *****************************************************************/
	private void displayButtons() {
		for (int c = model.getPlayers().indexOf(
				model.getCurrentTrick().getStarter());
				c < model.getPlayers().indexOf(
						model.getCurrentTrick(
								).getStarter()) 
				+ model.trickCards().size(); c++) {
			trickButtons[(c % model.getPlayers().size())].
			setIcon(cardToImage.get(model.trickCards().get(
					c - model.getPlayers().indexOf(
							model.getCurrentTrick(
									).
							getStarter()))));
		}

		for (int c = ((model.getPlayers().indexOf(
				model.getCurrentTrick().getStarter())
				+ model.trickCards().size())
				% model.getPlayers().size());
				c < model.getPlayers().indexOf(
						model.
						getCurrentTrick()
						.getStarter());
				c++) {
			trickButtons[c].setIcon(emptyCard);
		}
	}

	
	/*****************************************************************
     * This method creates and configures the scorePanel components.
     *****************************************************************/
	private void setupScorePanel() {
		final int scorePanelSpace = 30;

		scorePanel = new JPanel();
		scorePanel.setLayout(
				new BoxLayout(scorePanel, BoxLayout.X_AXIS));
		scorePanel.setBackground(Color.GREEN);

		newGameButton = new JButton("New Game");
		newGameButton.setBackground(Color.LIGHT_GRAY);
		newGameButton.setForeground(Color.BLACK);
		newGameButton.setBorder(new BevelBorder(0));
		newGameButton.setPreferredSize(new Dimension(80, 20));
		newGameButton.addActionListener(listener);
		scorePanel.add(Box.createHorizontalGlue());
		scorePanel.add(newGameButton);
		scorePanel.add(Box.createRigidArea(
				new Dimension(scorePanelSpace, 0)));

		playerScores = new JLabel[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			playerScores[i] = new JLabel(
					model.getPlayers().get(i).getName() 
					+ ": " 
		+ model.getPlayers().get(i).getScore());
			scorePanel.add(playerScores[i]);
			scorePanel.add(Box.createRigidArea(
					new Dimension(scorePanelSpace, 0)));
		}
		quitButton = new JButton("Quit");
		quitButton.setBackground(Color.LIGHT_GRAY);
		quitButton.setForeground(Color.BLACK);
		quitButton.setBorder(new BevelBorder(0));
		quitButton.setPreferredSize(new Dimension(80, 20));
		quitButton.addActionListener(listener);
		scorePanel.add(quitButton);
		scorePanel.add(Box.createHorizontalGlue());
	}

	
	/*****************************************************************
     * This method creates and configures the trickPanel components.
     *****************************************************************/
	private void setupTrickPanel() {
		configureTrickPanel();
		trickPanel.add(Box.createHorizontalGlue());
		addTrickButtons();
		trickPanel.add(Box.createHorizontalGlue());
	}

	/*****************************************************************
     * This helper method creates and configures the trickPanel 
     * components.
     *****************************************************************/
	private void configureTrickPanel() {
		trickPanel = new JPanel();
		trickPanel.setBackground(Color.GREEN);
		trickPanel.setLayout(
				new BoxLayout(trickPanel, BoxLayout.X_AXIS));
	}

	
	/*****************************************************************
     * This helper method adds the trickButtons to the trickPanel.
     *****************************************************************/
	private void addTrickButtons() {
		trickButtons = new JButton[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			trickButtons[i] = new JButton(null, emptyCard);
			trickButtons[i].addActionListener(listener);
			trickButtons[i].setBackground(Color.GREEN);
			trickPanel.add(trickButtons[i]);
		}
	}

	
	/*****************************************************************
     * This helper method updates all panels.
     *****************************************************************/
	private void repaintAll() {
		scorePanel.repaint();
		trickPanel.repaint();
		playAreaPanel.repaint();
		handPanel.repaint();
		this.repaint();
	}

	
	/*****************************************************************
     * This method removes all component panels.
     *****************************************************************/
	private void removePanels() {
		this.remove(scorePanel);
		this.remove(trickPanel);
		this.remove(playAreaPanel);
		this.remove(handPanel);
	}

	
	/*****************************************************************
     * This method creates and configures the playAreaPanel.
     *****************************************************************/
	private void setupPlayAreaPanel() {
		playAreaPanel = new JPanel();
		playAreaPanel.setBackground(Color.GREEN);
	}

	
	/*****************************************************************
     * This method creates and configures the handPanel components.
     *****************************************************************/
	private void setupHandPanel() {
		handPanel = new JPanel();
		handPanel.setLayout(new BoxLayout(handPanel,
				BoxLayout.X_AXIS));
		handPanel.setBackground(Color.green);
		handPanel.add(Box.createHorizontalGlue());

		handButtons = new JButton[FULL_HAND_SIZE];
		for (int i = 0; i < model.getUsersHand().size(); i++) {
			handButtons[i] = new JButton(null, 
					cardToImage.get(
					model.getUsersHand().get(i)));
			handButtons[i].addActionListener(listener);
			handButtons[i].setBackground(Color.WHITE);
			handPanel.add(handButtons[i]);
		}
		handPanel.add(Box.createHorizontalGlue());
	}

	
	/******************************************************************
	 * This class is the Controller portion of the Model-View-Controller 
	 * design for a CanadianSalad simulator. It adjusts game play based
	 * on the user's input.
	 *
	 * @author Andrew Weston and Christian Thompson
	 * @version Summer 2019
	 *****************************************************************/
	private class Listener implements ActionListener {

		
		/**************************************************************
	     * This method defines interactions between the user and the
	     * CanadianSaladModel game logic.
	     * 
	     * @param event is an ActionEvent by the user.
	     *************************************************************/
		public void actionPerformed(final ActionEvent event) {
			if (event.getSource() == newGameButton) {
				LinkedList<Player> thePlayers = 
						new LinkedList<>();
				for (Player p : model.getPlayers()) {
					thePlayers.add(new Player(p.getName()));
				}
				model = new CanadianSaladModel(thePlayers);
				removePanels();
				// System.out.println("got here");
				setupPanels();
				addPanels();
				repaintAll();
			}

			for (int h = 0; h < model.getUsersHand().size(); h++) {
				if (handButtons[h] == event.getSource()) {
					if (model.canFollowSuit()) {
						checkCard(h);
					} else {
						selectedCard =
								model.
								getUsersHand()
								.get(h);
					}
				}
			}
			if (event.getSource() == quitButton) {
				int selection = JOptionPane.showConfirmDialog(
						null,  "Do you really "
								+ "want " 
				+ "to quit?", "Quit?",
						JOptionPane.YES_NO_OPTION);
				if (selection == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
			if ((trickButtons[numPlayers - 1] == event.getSource()) 
					&& (selectedCard != null) && (model.getCurrentPlayer().equals(model.getUserPlayer()))) {
				model.playCard(selectedCard);
				selectedCard = null;
				// System.out.println(model.getCurrentTrick());
				displayBoard();
			}

			if (model.isGameComplete()) {
				JOptionPane.showMessageDialog(null,
						"Game Over!");
			}
		}

		
		/**************************************************************
	     * This method checks whether the selected card follows suit.
	     * 
	     * @param h represents the index of the selected card in the 
	     * user's hand.
	     *************************************************************/
		private void checkCard(final int h) {
			if (model.getCurrentPlayer().equals(model.getUserPlayer())) {
			if ((model.getCurrentTrick().getLed().getSuit() 
					== model.getUsersHand().get(h).
					getSuit())
					|| (model.trickCards().size()
							== numPlayers)) {
				selectedCard = model.getUsersHand().get(h);
			}
		}
	}
	}

	
	/*****************************************************************
     * This helper method creates a map that pairs Card objects
     * with their equivalent ImageIcon object.
     *****************************************************************/
	public void setupMap() {
		cardToImage = new HashMap<>();
		cardToImage.put(new Card(Suit.Clubs, Rank.Two), c2);
		cardToImage.put(new Card(Suit.Clubs, Rank.Three), c3);
		cardToImage.put(new Card(Suit.Clubs, Rank.Four), c4);
		cardToImage.put(new Card(Suit.Clubs, Rank.Five), c5);
		cardToImage.put(new Card(Suit.Clubs, Rank.Six), c6);
		cardToImage.put(new Card(Suit.Clubs, Rank.Seven), c7);
		cardToImage.put(new Card(Suit.Clubs, Rank.Eight), c8);
		cardToImage.put(new Card(Suit.Clubs, Rank.Nine), c9);
		cardToImage.put(new Card(Suit.Clubs, Rank.Ten), c10);
		cardToImage.put(new Card(Suit.Clubs, Rank.Jack), cJ);
		cardToImage.put(new Card(Suit.Clubs, Rank.Queen), cQ);
		cardToImage.put(new Card(Suit.Clubs, Rank.King), cK);
		cardToImage.put(new Card(Suit.Clubs, Rank.Ace), cA);

		cardToImage.put(new Card(Suit.Diamonds, Rank.Two), d2);
		cardToImage.put(new Card(Suit.Diamonds, Rank.Three), d3);
		cardToImage.put(new Card(Suit.Diamonds, Rank.Four), d4);
		cardToImage.put(new Card(Suit.Diamonds, Rank.Five), d5);
		cardToImage.put(new Card(Suit.Diamonds, Rank.Six), d6);
		cardToImage.put(new Card(Suit.Diamonds, Rank.Seven), d7);
		cardToImage.put(new Card(Suit.Diamonds, Rank.Eight), d8);
		cardToImage.put(new Card(Suit.Diamonds, Rank.Nine), d9);
		cardToImage.put(new Card(Suit.Diamonds, Rank.Ten), d10);
		cardToImage.put(new Card(Suit.Diamonds, Rank.Jack), dJ);
		cardToImage.put(new Card(Suit.Diamonds, Rank.Queen), dQ);
		cardToImage.put(new Card(Suit.Diamonds, Rank.King), dK);
		cardToImage.put(new Card(Suit.Diamonds, Rank.Ace), dA);

		cardToImage.put(new Card(Suit.Hearts, Rank.Two), h2);
		cardToImage.put(new Card(Suit.Hearts, Rank.Three), h3);
		cardToImage.put(new Card(Suit.Hearts, Rank.Four), h4);
		cardToImage.put(new Card(Suit.Hearts, Rank.Five), h5);
		cardToImage.put(new Card(Suit.Hearts, Rank.Six), h6);
		cardToImage.put(new Card(Suit.Hearts, Rank.Seven), h7);
		cardToImage.put(new Card(Suit.Hearts, Rank.Eight), h8);
		cardToImage.put(new Card(Suit.Hearts, Rank.Nine), h9);
		cardToImage.put(new Card(Suit.Hearts, Rank.Ten), h10);
		cardToImage.put(new Card(Suit.Hearts, Rank.Jack), hJ);
		cardToImage.put(new Card(Suit.Hearts, Rank.Queen), hQ);
		cardToImage.put(new Card(Suit.Hearts, Rank.King), hK);
		cardToImage.put(new Card(Suit.Hearts, Rank.Ace), hA);

		cardToImage.put(new Card(Suit.Spades, Rank.Two), s2);
		cardToImage.put(new Card(Suit.Spades, Rank.Three), s3);
		cardToImage.put(new Card(Suit.Spades, Rank.Four), s4);
		cardToImage.put(new Card(Suit.Spades, Rank.Five), s5);
		cardToImage.put(new Card(Suit.Spades, Rank.Six), s6);
		cardToImage.put(new Card(Suit.Spades, Rank.Seven), s7);
		cardToImage.put(new Card(Suit.Spades, Rank.Eight), s8);
		cardToImage.put(new Card(Suit.Spades, Rank.Nine), s9);
		cardToImage.put(new Card(Suit.Spades, Rank.Ten), s10);
		cardToImage.put(new Card(Suit.Spades, Rank.Jack), sJ);
		cardToImage.put(new Card(Suit.Spades, Rank.Queen), sQ);
		cardToImage.put(new Card(Suit.Spades, Rank.King), sK);
		cardToImage.put(new Card(Suit.Spades, Rank.Ace), sA);
	}

	
	/*****************************************************************
     * This helper method creates ImageIcon objects for every card
     * in a standard deck.
     *****************************************************************/
	public void setupImages() {
		c2 = new ImageIcon("cardPics\\2C.png");
		c3 = new ImageIcon("cardPics\\3C.png");
		c4 = new ImageIcon("cardPics\\4C.png");
		c5 = new ImageIcon("cardPics\\5C.png");
		c6 = new ImageIcon("cardPics\\6C.png");
		c7 = new ImageIcon("cardPics\\7C.png");
		c8 = new ImageIcon("cardPics\\8C.png");
		c9 = new ImageIcon("cardPics\\9C.png");
		c10 = new ImageIcon("cardPics\\10C.png");
		cJ = new ImageIcon("cardPics\\JC.png");
		cQ = new ImageIcon("cardPics\\QC.png");
		cK = new ImageIcon("cardPics\\KC.png");
		cA = new ImageIcon("cardPics\\AC.png");
		d2 = new ImageIcon("cardPics\\2D.png");
		d3 = new ImageIcon("cardPics\\3D.png");
		d4 = new ImageIcon("cardPics\\4D.png");
		d5 = new ImageIcon("cardPics\\5D.png");
		d6 = new ImageIcon("cardPics\\6D.png");
		d7 = new ImageIcon("cardPics\\7D.png");
		d8 = new ImageIcon("cardPics\\8D.png");
		d9 = new ImageIcon("cardPics\\9D.png");
		d10 = new ImageIcon("cardPics\\10D.png");
		dJ = new ImageIcon("cardPics\\JD.png");
		dQ = new ImageIcon("cardPics\\QD.png");
		dK = new ImageIcon("cardPics\\KD.png");
		dA = new ImageIcon("cardPics\\AD.png");
		h2 = new ImageIcon("cardPics\\2H.png");
		h3 = new ImageIcon("cardPics\\3H.png");
		h4 = new ImageIcon("cardPics\\4H.png");
		h5 = new ImageIcon("cardPics\\5H.png");
		h6 = new ImageIcon("cardPics\\6H.png");
		h7 = new ImageIcon("cardPics\\7H.png");
		h8 = new ImageIcon("cardPics\\8H.png");
		h9 = new ImageIcon("cardPics\\9H.png");
		h10 = new ImageIcon("cardPics\\10H.png");
		hJ = new ImageIcon("cardPics\\JH.png");
		hQ = new ImageIcon("cardPics\\QH.png");
		hK = new ImageIcon("cardPics\\KH.png");
		hA = new ImageIcon("cardPics\\AH.png");
		s2 = new ImageIcon("cardPics\\2S.png");
		s3 = new ImageIcon("cardPics\\3S.png");
		s4 = new ImageIcon("cardPics\\4S.png");
		s5 = new ImageIcon("cardPics\\5S.png");
		s6 = new ImageIcon("cardPics\\6S.png");
		s7 = new ImageIcon("cardPics\\7S.png");
		s8 = new ImageIcon("cardPics\\8S.png");
		s9 = new ImageIcon("cardPics\\9S.png");
		s10 = new ImageIcon("cardPics\\10S.png");
		sJ = new ImageIcon("cardPics\\JS.png");
		sQ = new ImageIcon("cardPics\\QS.png");
		sK = new ImageIcon("cardPics\\KS.png");
		sA = new ImageIcon("cardPics\\AS.png");

		emptyCard = new ImageIcon("cardPics\\solidWhite.png");
	}
}

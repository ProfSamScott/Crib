import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JPanel;

public class CribPanel extends JPanel implements MouseListener, Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int DISCARDING = 0;
	public static final int NEWHAND = 1;
	public static final int PINNING = 2;
	public static final int SHOWING = 3;
	public static final int CUTTHECARDS = 4;
	public static final int GAMEOVER = 5;
	public static final int TWOFORHISHEELS = 6;
	public static final int PINNINGMARKIT = 7;
	public static final int PINNINGENDED = 8;
	public static final int PINNINGROUNDOVER = 9;

	public static final int DECIDING = 0;
	public static final int DONE = 1;

	public static final int PLAYER = 1;
	public static final int COMPUTER = 2;
	public static final int CRIB = 3;
	public static final int PINNINGOVER = 4;

	public static final boolean DECKCHECK = true;
	public static final boolean NOPINNING = false;
	public static final boolean COMPUTERFACEUP = false;

	boolean boardToggle = false;
	Deck deck, crib;
	PinningDeck playerPinHand, computerPinHand;
	Board board;
	Image scoreBoardImage, skunkImage;
	CribHand hand, computerHand;
	CribScore score = null;
	CribScore oldScore = null;;
	Color tableColor = new Color(0, 128, 128);
	AudioClip badSound, ackSound;
	AudioClip flipOver, flipTop, replaceCard, shuffle, dealCard;
	PinningScore pinningScore;
	CanvasButton okButton, switchButton;
	Color[] playerColors = { new Color(110, 220, 110), new Color(220, 220, 110) };

	// GAME STATE VARIABLES
	int state = NEWHAND;
	int whosCrib = PLAYER;
	int whosTurn = PLAYER;
	// int whosPinScore = PLAYER;
	int playerStatus = DECIDING;
	int nextShow = PLAYER;

	public CribPanel() {
		Image cardImage = null, boardImage = null, okButtonImage = null, switchButtonImage = null;
		//try {
			cardImage = //ImageIO.read(new URL(a.getCodeBase(),"images/standard2.png"));
					Toolkit.getDefaultToolkit ().getImage (getClass().getClassLoader().getResource(
							"images/standard2.png"));
			boardImage = //ImageIO.read(new URL(a.getCodeBase(),
					Toolkit.getDefaultToolkit ().getImage (getClass().getClassLoader().getResource(
							"images/board2.jpg"));
			scoreBoardImage = //ImageIO.read(new URL(a.getCodeBase(),
					Toolkit.getDefaultToolkit ().getImage (getClass().getClassLoader().getResource(
							"images/scoreBoard.png"));
			okButtonImage = //ImageIO.read(new URL(a.getCodeBase(),
					Toolkit.getDefaultToolkit ().getImage (getClass().getClassLoader().getResource(
							"images/okbutton.png"));
			switchButtonImage = //ImageIO.read(new URL(a.getCodeBase(),
					Toolkit.getDefaultToolkit ().getImage (getClass().getClassLoader().getResource(
							"images/switchbutton.png"));
			skunkImage = //ImageIO.read(new URL(a.getCodeBase(),
					Toolkit.getDefaultToolkit ().getImage (getClass().getClassLoader().getResource(
							"images/skunk.png"));
		//} catch (IOException e) {
		//	System.out.println(e.toString());
		//}

		okButton = new CanvasButton(okButtonImage, 370, 406);
		switchButton = new CanvasButton(switchButtonImage, 510, 460);
	    URL location = CribPanel.class.getResource("sounds/flipover.wav"); 
	    flipOver = Applet.newAudioClip(location);// a.getAudioClip(a.getCodeBase(), "sounds/flipover.wav");
	    location = CribPanel.class.getResource("sounds/cutcards2.wav"); 
		flipTop = Applet.newAudioClip(location);//a.getAudioClip(a.getCodeBase(), "sounds/cutcards2.wav");
	    location = CribPanel.class.getResource("sounds/undealcard.wav"); 
		replaceCard = Applet.newAudioClip(location);//a.getAudioClip(a.getCodeBase(), "sounds/undealcard.wav");
	    location = CribPanel.class.getResource("sounds/shuffle.wav"); 
		shuffle = Applet.newAudioClip(location);//a.getAudioClip(a.getCodeBase(), "sounds/shuffle.wav");
	    location = CribPanel.class.getResource("sounds/dealcard.wav"); 
		dealCard = Applet.newAudioClip(location);//a.getAudioClip(a.getCodeBase(), "sounds/dealcard.wav");

	    location = CribPanel.class.getResource("sounds/BEEPJAZZ.WAV"); 
		badSound = Applet.newAudioClip(location);//a.getAudioClip(a.getCodeBase(), "sounds/BEEPJAZZ.WAV");
	    location = CribPanel.class.getResource("sounds/BEEPSINGLE.wav"); 
		ackSound = Applet.newAudioClip(location);//a.getAudioClip(a.getCodeBase(), "sounds/BEEPSINGLE.WAV");

	    location = CribPanel.class.getResource("sounds/pinout.wav"); 
		AudioClip pinOut = Applet.newAudioClip(location);//a.getAudioClip(a.getCodeBase(), "sounds/pinout.WAV");
	    location = CribPanel.class.getResource("sounds/pinin.wav"); 
		AudioClip pinIn = Applet.newAudioClip(location);//a.getAudioClip(a.getCodeBase(), "sounds/pinin.WAV");

		StandardCardLoader cl = new StandardCardLoader(cardImage, 79, 123);
		cl.loadCards();
		deck = cl.getDeck();
		deck.removeCardRank(Card.JOKER);
		deck.setX(10 + (int) ((79 + 5) * .5));
		deck.setY(143 + 20);

		// for (int i = 1; i < 8; i++)
		// deck.removeCardRank(i);
		// for (int i = 12; i < 14; i++)
		// deck.removeCardRank(i);

		crib = new Deck(deck.getBack(), tableColor,
				10 + (int) ((79 + 5) * 1.5), 143 + 20);

		board = new Board(boardImage, 550, 10, playerColors, pinOut, pinIn);

		hand = new CribHand(deck.getBack(), tableColor, 15, 318, 600, 123);
		hand.faceUp();
		computerHand = new CribHand(deck.getBack(), tableColor, 15, 15, 600,
				123);

		playerPinHand = new PinningDeck(deck.getBack(), tableColor, 366, 253);
		playerPinHand.faceUp();
		computerPinHand = new PinningDeck(deck.getBack(), tableColor, 366, 70);
		computerPinHand.faceUp();

		if (Math.random() < 0.5)
			whosCrib = PLAYER;
		else
			whosCrib = COMPUTER;

		addMouseListener(this);
		(new Thread(this)).start();
	}

	public void checkDeck() {
		if (deck.numCards() != 52)
			throw new RuntimeException("Bad deck - " + deck.numCards()
					+ " cards.");
	}

	public void deal() {
		if (DECKCHECK)
			checkDeck();
		deck.shuffle();
		shuffle.play();
		wait(500);
		computerHand.faceDown();
		computerHand.noSort();
		hand.faceDown();
		if (whosCrib == COMPUTER)
			for (int i = 0; i < 6; i++) {
				deck.dealToDeck(hand, 1);
				dealCard.play();
				repaint();
				wait(200);
				dealCard.stop();
				wait(50);
				deck.dealToDeck(computerHand, 1);
				dealCard.play();
				repaint();
				wait(200);
				dealCard.stop();
				wait(50);
			}
		else
			for (int i = 0; i < 6; i++) {
				deck.dealToDeck(computerHand, 1);
				dealCard.play();
				repaint();
				wait(200);
				dealCard.stop();
				wait(50);
				deck.dealToDeck(hand, 1);
				dealCard.play();
				repaint();
				wait(200);
				dealCard.stop();
				wait(50);
			}
		wait(300);
		hand.faceUp();
		flipOver.play();
		wait(300);
		repaint();
		state = DISCARDING;
		playerStatus = DECIDING;
		// computerHand.flipOver();

		// score = new CribScore(hand, deck.peekTopCard(), false, 550, 10, 500,
		// 500, "Your Score", scoreBoardImage);
	}

	public void actionButtonPressed() {
		switch (state) {
		case DISCARDING:
			if (hand.numCardsUnselected() == 4) {
				if (hand.discardSelectedToDeck(crib) != 0) {
					dealCard.play();
					playerStatus = DONE;
					// score = new CribScore(computerHand, null, false, 550, 10,
					// 500, 500, "Computer Score");
					repaint();
					return;
				}
			}
			badSound.play();
			return;
		case SHOWING:
		case TWOFORHISHEELS:
		case PINNINGMARKIT:
		case PINNINGENDED:
		case GAMEOVER:
			if (playerStatus == DECIDING) {
				playerStatus = DONE;
				ackSound.play();
				if (state == PINNINGROUNDOVER)
					pinningScore.clear();
				return;
			}
			badSound.play();
			return;
		case PINNINGROUNDOVER:
			if (playerStatus == DECIDING) {
				playerStatus = DONE;
				ackSound.play();
			}
			return;
		case CUTTHECARDS:
			if (playerStatus == DECIDING) {
				playerStatus = DONE;
				return;
			}
			badSound.play();
			return;
		case NEWHAND:
			if (playerStatus == DECIDING) {
				playerStatus = DONE;
				return;
			}
			badSound.play();
			return;
		default:
			badSound.play();
			return;
		}
	}

	public void wait(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}

	public void waitForComputer(int time, Deck deck) {
		Card c = null;
		if (deck != null)
			c = deck.peekTopCard();
		int counter = 0;
		while (time != 0) {
			if (counter == 0 & c != null)
				if (c.isSelected())
					c.setSelected(false);
				else
					c.setSelected(true);
			counter = (counter + 1) % 5;
			repaint();
			wait(100);
			time -= 100;
		}
		if (c != null)
			c.setSelected(false);
		repaint();
	}

	public void waitForPlayer(Deck deck) {
		Card c = null;
		if (deck != null)
			c = deck.peekTopCard();
		int counter = 0;
		while (playerStatus == DECIDING) {
			if (c != null) {
				if (counter == 0)
					if (c.isSelected())
						c.setSelected(false);
					else
						c.setSelected(true);
			}
			counter = (counter + 1) % 5;
			repaint();
			wait(100);
		}
		if (c != null)
			c.setSelected(false);
		repaint();
	}

	public void pegIt(int player, CribHand currentHand, boolean crib,
			String message) {
		setBoard(new CribScore(currentHand, deck.peekTopCard(), crib, 550, 10,
				message, scoreBoardImage, playerColors[player - 1]));
		int addition = score.getScore();
		currentHand.faceUp();
		flipOver.play();
		wait(300);
		playerStatus = DECIDING;
		repaint();
		waitForPlayer(null);
		setBoard(null);
		repaint();
		board.add(player, addition, this);
		// currentHand.dealToDeckBottom(deck, 4);
		if (board.winner() == 0)
			moveCards(currentHand, deck, 4, true);
	}

	public void newGame() {
		board.reset();
		deck.faceDown();
		moveCards(hand, deck, 6, true);
		moveCards(computerHand, deck, 6, true);
		moveCards(crib, deck, 6, true);
		moveCards(computerPinHand, deck, 6, true);
		moveCards(playerPinHand, deck, 6, true);
		if (DECKCHECK)
			checkDeck();
		if (Math.random() < 0.5)
			whosCrib = PLAYER;
		else
			whosCrib = COMPUTER;
	}

	public void startPinning() {
		pinningScore = new PinningScore(hand, 550, 10, 500, 500,
				scoreBoardImage);
		playerPinHand.discardAll();
		computerPinHand.discardAll();
		boardToggle = false;
		setBoard(pinningScore);
		if (whosCrib == PLAYER)
			whosTurn = COMPUTER;
		else
			whosTurn = PLAYER;
		state = PINNING;
	}

	public void switchPinningTurn() {
		if (whosTurn == PLAYER)
			whosTurn = COMPUTER;
		else
			whosTurn = PLAYER;
	}

	public void selectComputerPinningMove() {
		Card c = computerHand.choosePinCard(pinningScore);
		wait(500);
		c.setSelected(true);
		repaint();
		wait(200);
		c.setSelected(false);
		computerPinHand.addToTop(c);
		pinningScore.addCard(c, playerColors[COMPUTER - 1]);
		repaint();
		dealCard.play();
	}

	public void selectPlayerPinningMove() {
		playerStatus = DECIDING;
		waitForPlayer(null);
	}

	public void setBoard(CribScore newBoard) {
		if (boardToggle) {
			boardToggle = false;
			CribScore temp = score;
			score = oldScore;
			oldScore = temp;
		}
		oldScore = score;
		score = newBoard;
	}

	// THIS IS THE OTHER HALF OF SELECT PLAYER PINNING MOVE
	public void mouseReleased(MouseEvent arg0) {
		if (boardToggle) {
			boardToggle = false;
			CribScore temp = score;
			score = oldScore;
			oldScore = temp;
		}
		if (state == PINNING & playerStatus == DECIDING & currentCard != null) {
			currentCard.setSelected(false);
			if (pinningScore.addCard(currentCard, playerColors[PLAYER - 1])) {
				playerPinHand.addToTop(currentCard);
				hand.removeCard(currentCard);
				currentCard = null;
				dealCard.play();
				playerStatus = DONE;
			} else
				badSound.play();
		}
		repaint();
	}

	public void moveCards(Deck fromDeck, Deck toDeck, int numCards,
			boolean bottom) {
		for (int i = 0; i < 4; i++) {
			if (!fromDeck.isEmpty()) {
				if (bottom) {
					fromDeck.dealToDeckBottom(toDeck, 1);
				} else {
					fromDeck.dealToDeck(toDeck, 1);
				}
				dealCard.play();
				repaint();
				wait(100);
			}
		}
		dealCard.stop();
	}

	public void run() {
		while (true) {
			// System.out.println("state: " + state + " whosTurn: " + whosTurn
			// + " playerStatus: " + playerStatus);
			if (COMPUTERFACEUP)
				computerHand.faceUp();
			switch (state) {
			case GAMEOVER:
				// wait(1000);
				winCounter = 0;
				playerStatus = DECIDING;
				waitForPlayer(null);
				newGame();
				state = NEWHAND;
				break;
			case NEWHAND:
				if (whosCrib == PLAYER)
					whosCrib = COMPUTER;
				else
					whosCrib = PLAYER;
				deck.faceDown();
				// flipOver.play();
				// wait(300);
				repaint();
				wait(1000);
				deal();
				break;
			case DISCARDING:
				playerStatus = DECIDING;
				waitForComputer(1000, crib);
				computerHand.selectBestDiscard(whosCrib == COMPUTER);
				repaint();
				waitForComputer(1000, crib);
				computerHand.discardSelectedToDeck(crib);
				repaint();
				computerHand.sort();
				waitForPlayer(crib);
				state = CUTTHECARDS;
				if (whosCrib == COMPUTER)
					playerStatus = DECIDING;
				break;
			case CUTTHECARDS:
				if (whosCrib == COMPUTER)
					waitForPlayer(deck);
				else
					waitForComputer(1000, deck);
				flipTop.play();
				wait(500);
				deck.exposeTopCard();
				if (deck.peekTopCard().getRank() == Card.JACK) {
					setBoard(new CribScore(
							new Hand(hand.getBack(), tableColor), deck
							.peekTopCard(), false, 550, 10,
							"Two For His Heels", scoreBoardImage,
							playerColors[whosCrib - 1]));
					state = TWOFORHISHEELS;
					playerStatus = DECIDING;
				} else {
					startPinning();
				}
				break;
			case TWOFORHISHEELS:
				waitForPlayer(null);
				setBoard(null);
				board.add(whosCrib, 2, this);
				if (board.winner() != 0)
					state = GAMEOVER;
				else
					startPinning();
			case PINNING:
				if (NOPINNING)
					state = SHOWING;
				else {
					if (!hand.canPin(pinningScore)
							& !computerHand.canPin(pinningScore)) {
						// END OF ROUND
						state = PINNINGROUNDOVER;
						switchPinningTurn(); // for marking the score
						pinningScore.roundOver(playerColors[whosTurn - 1]);
					} else if (whosTurn == PLAYER) {
						// PLAYER'S TURN
						if (hand.canPin(pinningScore)) {
							selectPlayerPinningMove();
							if (pinningScore.getScore() > 0)
								state = PINNINGMARKIT;
							else
								switchPinningTurn();
						} else
							switchPinningTurn();
					} else {
						if (computerHand.canPin(pinningScore)) {
							selectComputerPinningMove();
							if (pinningScore.getScore() > 0)
								state = PINNINGMARKIT;
							else
								switchPinningTurn();
						} else
							switchPinningTurn();
					}
				}
				break;
			case PINNINGMARKIT:
				wait(1000);
				setBoard(null);
				repaint();
				board.add(whosTurn, pinningScore.getScore(), this);
				setBoard(pinningScore);
				switchPinningTurn();
				if (board.winner() != 0) {
					setBoard(null);
					state = GAMEOVER;
				} else
					state = PINNING;
				break;
			case PINNINGROUNDOVER:
				wait(1000);
				setBoard(null);
				repaint();
				board.add(whosTurn, pinningScore.getScore(), this);
				setBoard(pinningScore);
				switchPinningTurn();
				computerPinHand.roundOver();
				playerPinHand.roundOver();
				flipOver.play();
				if (board.winner() == 0) {
					wait(300);
					if ((whosTurn == COMPUTER & computerHand.numCards() > 0)
							| (hand.numCards() == 0 & computerHand.numCards() == 0)) {
						playerStatus = DECIDING;
						waitForPlayer(null);
					}
				}
				if (board.winner() != 0) {
					setBoard(null);
					state = GAMEOVER;
				} else if (hand.numCards() == 0 & computerHand.numCards() == 0)
					state = PINNINGENDED;
				else
					state = PINNING;
				break;
			case PINNINGENDED:
				setBoard(null);
				moveCards(computerPinHand, computerHand, 4, false);
				hand.faceDown();
				moveCards(playerPinHand, hand, 4, false);
				state = SHOWING;
				if (whosCrib == PLAYER)
					nextShow = COMPUTER;
				else
					nextShow = PLAYER;
				break;
			case SHOWING:
				hand.faceDown();
				computerHand.faceDown();
				wait(1000);
				switch (nextShow) {
				case PLAYER:
					pegIt(PLAYER, hand, false, "Your Hand");
					break;
				case COMPUTER:
					pegIt(COMPUTER, computerHand, false, "My Hand");
					break;
				case CRIB:
					if (whosCrib == PLAYER) {
						hand.faceDown();
						moveCards(crib, hand, 4, false);
						pegIt(PLAYER, hand, true, "Your Crib");
					} else if (whosCrib == COMPUTER) {
						moveCards(crib, computerHand, 4, false);
						pegIt(COMPUTER, computerHand, true, "My Crib");
					}
					break;
				}
				if (board.winner() == 0) {
					switch (nextShow) {
					case PLAYER:
						if (whosCrib == COMPUTER)
							nextShow = COMPUTER;
						else
							nextShow = CRIB;
						playerStatus = DECIDING;
						break;
					case COMPUTER:
						if (whosCrib == COMPUTER)
							nextShow = CRIB;
						else
							nextShow = PLAYER;
						break;
					case CRIB:
						state = NEWHAND;
						flipOver.play();
						wait(300);
						hand.faceUp();
						computerHand.faceDown();
						break;
					}
				} else
					state = GAMEOVER;
			}
			repaint();
			wait(50);
		}
	}

	int winCounter = 0;

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(tableColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		// Player 1 Tray
		int numCards = Math.max(4, hand.numCards());
		/*
		 * if (state == NEWHAND || state == DISCARDING) numCards = 6; else
		 * numCards = 4;
		 */
		g.setColor(playerColors[0]);
		g.fillRoundRect(5, 308, 79 * numCards + hand.spacer * (numCards - 1)
				+ 20, 143, 20, 20);
		g.setColor(Color.black);
		g.drawRoundRect(5, 308, 79 * numCards + hand.spacer * (numCards - 1)
				+ 20, 143, 20, 20);

		hand.draw(g);
		deck.draw(g);
		crib.draw(g);

		// Player 2 Tray
		numCards = Math.max(4, computerHand.numCards());
		g.setColor(playerColors[1]);
		g.fillRoundRect(5, 5, 79 * numCards + computerHand.spacer
				* (numCards - 1) + 20, 143, 20, 20);
		g.setColor(Color.black);
		g.drawRoundRect(5, 5, 79 * numCards + computerHand.spacer
				* (numCards - 1) + 20, 143, 20, 20);
		computerHand.draw(g);
		board.draw(g);
		if (score != null)
			score.draw(g);
		if (state == PINNING
				| state == PINNINGMARKIT
				| state == PINNINGENDED
				| state == PINNINGROUNDOVER
				| (state == GAMEOVER & (!computerPinHand.isEmpty() | !playerPinHand
						.isEmpty()))) {
			playerPinHand.draw(g);
			computerPinHand.draw(g);
		}
		String message1 = "", message2 = "", message3 = "";
		boolean showOKButton = false;
		boolean showSkunk = false;

		switch (whosCrib) {
		case PLAYER:
			message1 = "your crib";
			break;
		case COMPUTER:
			message1 = "my crib";
			break;
		}
		switch (state) {
		case NEWHAND:
			message2 = "dealing";
			// message3 = "press ok to deal";
			message3 = "hang on a second";
			break;
		case DISCARDING:
			message2 = "discarding";
			if (playerStatus == DECIDING)
				message3 = "select two cards and click the crib";
			else
				message3 = "hang on a second";
			break;
		case CUTTHECARDS:
			if (whosCrib == COMPUTER) {
				message2 = "your cut";
				message3 = "click the deck";
			} else {
				message2 = "my cut";
				message3 = "hang on a second";
			}
			break;
		case PINNING:
			message2 = "pegging";
			if (whosTurn == PLAYER)
				message3 = "click a card";
			else
				message3 = "hang on a second";
			break;
		case PINNINGMARKIT:
		case PINNINGROUNDOVER:
		case PINNINGENDED:
			message2 = "pegging";
			if (playerStatus == DECIDING) {
				message3 = "click the checkmark to continue";
				showOKButton = true;
			} else
				message3 = "hang on a second";
			break;
		case SHOWING:
			message2 = "counting";
			if ((!boardToggle & score == null)
					|| (boardToggle & oldScore == null)) {
				message3 = "hang on a second";

				/*
				 * if (nextShow == PLAYER) message3 = "click the table to count
				 * your hand"; else if (nextShow == COMPUTER) message3 = "click
				 * the table to count my hand"; else if (whosCrib == PLAYER)
				 * message3 = "click the table to count your crib"; else
				 * message3 = "click the table to count my crib.";
				 */
			} else {
				message3 = "click the checkmark to mark it";
				showOKButton = true;
			}
			break;
		case TWOFORHISHEELS:
			message2 = "2 for his heels";
			message3 = "click the checkmark to mark it";
			if (playerStatus == DECIDING)
				showOKButton = true;
			break;
		case GAMEOVER:
			if (board.winner() == PLAYER)
				message2 = "you win!";
			else
				message2 = "I win!";
			if (board.skunk())
				showSkunk = true;
			message3 = "click the checkmark for a new game";
			showOKButton = true;
			break;
		}

		g.setFont(new Font("SansSerif", Font.BOLD, 16));
		g.setColor(Color.black);
		g.drawString(message1, 10, 480);
		g.drawString(message3, 240, 480);
		g.setColor(new Color(64, 0, 0));
		g.drawString(message2, 120, 480);

		if (showOKButton)
			okButton.draw(g);
		switchButton.draw(g);

		winCounter++;
		if (state == GAMEOVER & board.winner() > 0 & (winCounter / 5) % 2 == 0) {
			g.setXORMode(tableColor);
			// g.setColor(Color.white);
			// g.fillRoundRect(0,0,getWidth(),getHeight(),100,100);
			// g.setColor(Color.black);
			// g.drawRoundRect(0,0,getWidth(),getHeight(),100,100);
			g.setColor(playerColors[board.winner() - 1]);
			g.setFont(new Font("SansSerif", Font.BOLD, 130));
			int xCoord;
			if (board.winner() == 2)
				xCoord = 220;
			else
				xCoord = 30;
			g.drawString(message2, xCoord, 260);
			if (showSkunk)
				g.drawImage(skunkImage, 440, 390, 70, 70, null);
		}
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent arg0) {

	}

	Card currentCard = null;

	public void mouseExited(MouseEvent arg0) {
		if (currentCard != null) {
			currentCard.setSelected(false);
			currentCard = null;
		}
		if (boardToggle) {
			boardToggle = false;
			CribScore temp = score;
			score = oldScore;
			oldScore = temp;
		}
		repaint();
	}

	public void mousePressed(MouseEvent e) {
		if (switchButton.clickedOn(e.getX(), e.getY())) { // TOGGLE BOARD
			boardToggle = true;
			CribScore temp = score;
			score = oldScore;
			oldScore = temp;
			if (score == oldScore)
				badSound.play();
		} else if (state == SHOWING | state == TWOFORHISHEELS
				| (state == PINNINGROUNDOVER & playerStatus == DECIDING)) {
			if (okButton.clickedOn(e.getX(), e.getY()))
				actionButtonPressed();
			else
				badSound.play();
		} else if (state == PINNING) {
			if (playerStatus == DECIDING) {
				currentCard = hand.clickedOn(e.getX(), e.getY());
				if (currentCard == null)
					badSound.play();
			} else
				badSound.play();
		} else if (state == CUTTHECARDS & playerStatus == DECIDING) {
			Card c = deck.clickedOn(e.getX(), e.getY());
			if (c == null)
				badSound.play();
			else {
				playerStatus = DONE;
			}
		} else if (state == DISCARDING) {
			if (crib.hitTheDeck(e.getX(), e.getY()))
				actionButtonPressed();
			else
				hand.clickedOn(e.getX(), e.getY(), replaceCard, badSound);
		} else if (state == GAMEOVER) {
			if (okButton.clickedOn(e.getX(), e.getY()))
				actionButtonPressed();
			else
				badSound.play();
		} else
			badSound.play();
		repaint();
	}
}

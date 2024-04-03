import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JApplet;

public class HandTest extends JApplet implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Deck deck;
	Hand hand;
	CribScore score;

	public void init() {
		BufferedImage cardImage = null;
		try {
			cardImage = ImageIO.read(new URL(this.getCodeBase(),
					"images/standard2.png"));
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		StandardCardLoader cl = new StandardCardLoader(cardImage, 79, 123);
		cl.loadCards();
		deck = cl.getDeck();
		deck.removeCardRank(Card.JOKER);
		/*
		 * deck.removeCardSuit(0); deck.removeCardSuit(2);
		 * deck.removeCardSuit(3); deck.removeCardRank(1);
		 * deck.removeCardRank(2); deck.removeCardRank(3);
		 * deck.removeCardRank(4); deck.removeCardRank(5);
		 * deck.removeCardRank(6); deck.removeCardRank(7);
		 */
		deck.shuffle();
		deck.setX(10);
		deck.setY(10);

		deal();

		addMouseListener(this);
	}

	public void deal() {
		hand = new Hand(deck.getBack(), Color.blue, 10, 143, 450, 123);
		deck.dealToDeck(hand, 4);
		hand.flipOver();
		deck.exposeTopCard();

		score = new CribScore(hand, deck.peekTopCard(), false, 450, 10, 500,
				500);
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, getWidth(), getHeight());
		hand.draw(g);
		deck.draw(g);
		score.draw(g);
	}

	public void mouseClicked(MouseEvent arg0) {
		hand.dealToDeck(deck, 4);
		deck.shuffle();
		deal();
		repaint();

	}

	public void mouseEntered(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}

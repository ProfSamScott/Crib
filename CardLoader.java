import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
/**
 * Generic stuff for card loaders.
 * 
 * @author Sam Scott
 *
 */
public abstract class CardLoader extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The deck that was created from the image
	 */
	protected Deck deck;
	/**
	 * The image of the cards
	 */
	protected Image cardsImage;
	/**
	 * The width of a card
	 */
	protected int width;
	/**
	 * the height of a card
	 */
	protected int height;
	/**
	 * true if cards loaded
	 */
	protected boolean cardsLoaded = false;

	/**
	 * @return The deck that was loaded.
	 */
	public Deck getDeck() {
		return deck;
	}

	/**
	 * Must be called to load the cards
	 */
	public abstract void loadCards();
	
	/**
	 * Draws the cards for debugging purposes
	 * 
	 * @param g
	 *            graphics context
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//double width = this.width / 2;
		//double height = this.height / 2;
		if (!cardsLoaded) {
			g.drawString("CardsNotLoaded", 0, 20);
		} else {
			// REGULAR CARDS
			for (int suit = 0; suit <= 3; suit++)
				for (int rank = 1; rank <= 13; rank++) {
					Card card = deck.dealTopCard();
					g.drawImage(card.getImage(), (int) ((rank - 1)
							* (width + 2) + .5),
							(int) (suit * (height + 2) + .5),
							(int) (width + .5), (int) (height + .5), null);
					deck.addToBottom(card);
				}
			// JOKERS
			Card j1 = deck.dealTopCard();
			g.drawImage(j1.getImage(), 0, (int) (4 * (height + 2) + .5),
					(int) (width + .5), (int) (height + .5), null);
			deck.addToBottom(j1);
			Card j2 = deck.dealTopCard();
			g.drawImage(j1.getImage(), (int) (width + 2 + .5),
					(int) (4 * (height + 2) + .5), (int) (width + .5),
					(int) (height + .5), null);
			deck.addToBottom(j2);
			// BACK OF DECK
			g.drawImage(deck.getBack(), (int) (2 * (width + 2) + .5),
					(int) (4 * (height + 2) + .5), (int) (width + .5),
					(int) (height + .5), null);
		}
	}

}

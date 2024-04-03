import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Standard Card Loader creates a deck of 52 cards from a standard image. Cards
 * in 5 rows and 13 columns. Row 0: Clubs A-K... Row 1: Diamonds A-K... Row 2:
 * Hearts A-K... Row 3: Spades A-K... Row 4: Joker 1, Joker2, Back of Deck
 * 
 * It's a JPanel for easy debugging.
 * 
 * @author Sam Scott
 * 
 */
public class StandardCardLoader extends CardLoader {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Sets up the card loader. Must call loadCards() to do the work.
	 * 
	 * @param cardsImage
	 *            the image
	 * @param width
	 *            the width of a card
	 * @param height
	 *            the height of a card
	 */
	public StandardCardLoader(Image cardsImage, int width, int height) {
		this.cardsImage = cardsImage;
		this.width = width;
		this.height = height;
	}

	/**
	 * Must be called to actually load the cards.
	 */
	public void loadCards() {
		// GET BACK OF CARD IMAGE
		BufferedImage backOfCardImage = new BufferedImage((int) (width + .5),
				(int) (height + .5), BufferedImage.TYPE_4BYTE_ABGR);
		int tries;
		for (tries = 0; tries < 100; tries++) {
			if (backOfCardImage.getGraphics().drawImage(cardsImage, 0, 0,
					width, height, 2 * width, 4 * height, 3 * width,
					5 * height, null))
				break;
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		}
		// SOMETHING WENT WRONG!
		if (tries == 100) {
			throw new RuntimeException("Cards image not loaded in time.");
		}
		// CREATE DECK
		deck = new Deck(backOfCardImage, Color.blue);
		// GET REGULAR CARDS
		for (int row = 0; row <= 3; row++) {
			int suit = Card.CLUBS;
			switch (row) {
			case 0:
				suit = Card.CLUBS;
				break;
			case 1:
				suit = Card.DIAMONDS;
				break;
			case 2:
				suit = Card.HEARTS;
				break;
			case 3:
				suit = Card.SPADES;
				break;
			}
			for (int col = 0; col <= 12; col++) {
				int rank = col + 1;
				BufferedImage currentCardImage = new BufferedImage(width,
						height, BufferedImage.TYPE_4BYTE_ABGR);
				currentCardImage.getGraphics().drawImage(cardsImage, 0, 0,
						width, height, col * width, row * height,
						(col + 1) * width, (row + 1) * height, null);
				deck.addToBottom(new Card(currentCardImage, rank, suit));
			}
		}
		// GET JOKERS
		BufferedImage j1 = new BufferedImage(width, height,
				BufferedImage.TYPE_4BYTE_ABGR);
		j1.getGraphics().drawImage(cardsImage, 0, 0, width, height, 0,
				4 * height, width, 5 * height, null);
		deck.addToBottom(new Card(j1, Card.JOKER, Card.JOKER));
		BufferedImage j2 = new BufferedImage(width, height,
				BufferedImage.TYPE_4BYTE_ABGR);
		j2.getGraphics().drawImage(cardsImage, 0, 0, width, height, 0,
				4 * height, width, 5 * height, null);
		deck.addToBottom(new Card(j2, Card.JOKER, Card.JOKER));

		cardsLoaded = true;
		// System.out.println(deck.toString());
	}
}

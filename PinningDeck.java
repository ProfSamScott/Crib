import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class PinningDeck extends Deck {

	private int marker = 0;

	/**
	 * Creates a pinning deck with given back image
	 * 
	 * @param back
	 *            the back image
	 */
	PinningDeck(BufferedImage back, Color tableColor) {
		super(back, tableColor);
	}

	/**
	 * Creates a pinning deck with given back image and location
	 * 
	 * @param back
	 *            the back image
	 * @param x
	 *            x-coord of deck
	 * @param y
	 *            y-coord of dedk
	 */
	PinningDeck(BufferedImage back, Color tableColor, int x, int y) {
		super(back, tableColor, x, y);
	}

	public void discardAll()
	{
		super.discardAll();
		marker = 0;
	}
	
	public void roundOver() {
		marker = numCards();
	}

	public void draw(Graphics g) {
		faceUp();
		if (isEmpty()) {
			g.setColor(Color.LIGHT_GRAY);
			g.drawRoundRect(x + 1, y + 1, width - 2, height - 2,
					(int) (width * .1), (int) (height * .1));
		} else {
			int drawX = x;

			for (int i = 0; i < numCards(); i++) {
				Card c = this.dealBottomCard();
				addToTop(c);
				if (i >= marker)
					g.drawImage(c.getImage(), drawX, y, null);
				else
					g.drawImage(getBack(), drawX, y, null);
				drawX += (int) (cardWidth * 0.2);
			}
		}
	}
}

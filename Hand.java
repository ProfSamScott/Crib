import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Implements a generic hand of cards
 * 
 * @author Sam Scott
 * 
 */
public class Hand extends Deck {

	/**
	 * Space between cards in display
	 */
	protected final int spacer = 5;
	protected int drawCardWidth;
	protected int drawCardHeight;

	/**
	 * Auto sort if true
	 */
	protected boolean sort = true;

	/**
	 * Constructor
	 * 
	 * @param back
	 *            the back image from the deck
	 */
	Hand(BufferedImage back, Color tableColor) {
		this.back = back;
		this.tableColor = tableColor;
		cardWidth = back.getWidth();
		cardHeight = back.getHeight();
		this.width = cardWidth;
		this.height = cardHeight;
	}

	/**
	 * Constructor
	 * 
	 * @param back
	 *            the back image from the deck
	 * @param x
	 *            x-coord of hand
	 * @param y
	 *            y-coord of hand
	 */
	Hand(BufferedImage back, Color tableColor, int x, int y) {
		this(back, tableColor);
		this.x = x;
		this.y = y;
	}

	/**
	 * Constructor
	 * 
	 * @param back
	 *            the back image from the deck
	 * @param x
	 *            x-coord of hand
	 * @param y
	 *            y-coord of hand
	 * @param width
	 *            width of hand
	 * @param height
	 *            height of hand
	 */
	Hand(BufferedImage back, Color tableColor, int x, int y, int width,
			int height) {
		this(back, tableColor, x, y);
		this.width = width;
		this.height = height;
	}

	/**
	 * Constructor - creates duplicate of given hand
	 * 
	 * @param hand
	 *            hand to duplicate
	 */
	Hand(Hand h) {
		this(h.getBack(), h.getTableColor(), h.getX(), h.getY(),
				h.getWidth(), h.getHeight());
		for (int i = 0; i < h.numCards(); i++) {
			Card c = h.dealTopCard();
			addToBottom(c);
			h.addToBottom(c);
		}
	}

	public void sort() {
		sort = true;
		super.sort();
	}

	public void noSort() {
		sort = false;
	}

	public int numCardsUnselected() {
		int count = 0;
		for (int i = 0; i < numCards(); i++) {
			Card c = dealTopCard();
			addToBottom(c);
			if (!c.isSelected())
				count++;
		}
		return count;
	}

	public void unselectAll() {
		for (int i = 0; i < numCards(); i++) {
			Card c = dealTopCard();
			addToBottom(c);
			c.setSelected(false);
		}
	}

	public Card clickedOn(int clickX, int clickY) {
		int cardIndex = -1;
		for (int i = 0; i < numCards(); i++) {
			int cardX = x + (drawCardWidth + spacer) * i;
			int cardY = y;
			if (clickX > cardX & clickX < cardX + drawCardWidth
					& clickY > cardY & clickY < cardY + drawCardHeight) {
				cardIndex = i;
				break;
			}
		}
		if (cardIndex != -1) {
			Card cardClicked = getCard(cardIndex);
			cardClicked.toggleSelected();
			return cardClicked;
		}
		return null;
	}

	/**
	 * Draws the cards (always sorts first)
	 */
	public void draw(Graphics g) {
		if (!isEmpty()) {
			// SORT THE CARDS
			if (sort)
				sort();

			// FIGURE OUT HEIGHT AND WIDTH FOR DRAWING
			double scale1 = (double) height / cardHeight;
			double cardSpace = (width - (numCards() - 1) * (double) spacer)
					/ numCards();
			double scale2 = (double) cardSpace / cardWidth;
			double scale = Math.min(scale1, scale2);
			if (scale > 1)
				scale = 1;
			drawCardWidth = (int) (cardWidth * scale + .5);
			drawCardHeight = (int) (cardHeight * scale + .5);

			// DRAW THE CARDS WITH COMPUTED HEIGHT AND WIDTH
			for (int i = 0; i < numCards(); i++) {
				int cardX = x + (drawCardWidth + spacer) * i;
				int cardY = y;
				if (getCard(i).isSelected()) {
					cardY -= 5;
					g.setColor(Color.black);
					g.fillRoundRect(cardX + 2, cardY + 5, drawCardWidth,
							drawCardHeight, (int) (drawCardWidth * .2),
							(int) (drawCardHeight * .2));
					// g.drawImage(back, cardX+2, cardY+5, drawCardWidth,
					// drawCardHeight, null);
				}
				if (state == FACEUP)
					g.drawImage(getCard(i).getImage(), cardX, cardY,
							drawCardWidth, drawCardHeight, null);
				else
					g.drawImage(back, cardX, cardY, drawCardWidth,
							drawCardHeight, null);
				/*
				 * if (getCard(i).isSelected()) {
				 * System.out.println(getCard(i).toString());
				 * g.setXORMode(tableColor); g.setColor(Color.blue);
				 * g.fillRoundRect(cardX, cardY, drawCardWidth, drawCardHeight,
				 * (int)(drawCardWidth*.05), (int)(drawCardHeight*.05)); }
				 */
			}
		}
	}

	/**
	 * Draws the cards but just the top left corners (always sorts first)
	 */
	public void drawBrief(Graphics g) {
		if (!isEmpty()) {
			// SORT THE CARDS
			if (sort)
			sort();

			// FIGURE OUT HEIGHT AND WIDTH FOR DRAWING
			double scale1 = (double) height / cardHeight;
			double cardSpace = (width - (numCards() - 1) * (double) spacer)
					/ numCards();
			double scale2 = (double) cardSpace / cardWidth;
			double scale = Math.min(scale1, scale2);
			if (scale > 1)
				scale = 1;
			int drawCardWidth = (int) (cardWidth * scale + .5);
			int drawCardHeight = (int) (cardHeight * scale + .5);

			// DRAW THE CARDS WITH COMPUTED HEIGHT AND WIDTH
			for (int i = 0; i < numCards(); i++) {
				int cardX = x + (drawCardWidth + spacer) * i;
				int cardY = y;
				if (state == FACEUP)
					g.drawImage(getCard(i).getImage(), cardX, cardY, cardX
							+ drawCardWidth, cardY + drawCardHeight, 0, 0,
							drawCardWidth, drawCardHeight, null);
				else
					g.drawImage(back, cardX, cardY, cardX + drawCardWidth,
							cardY + drawCardHeight, 0, 0, drawCardWidth,
							drawCardHeight, null);
			}
		}
	}
}

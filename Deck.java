import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Arrays;

/**
 * Generic Deck. Can be face up or face down or face down with top card turned
 * up.
 * 
 * @author Sam Scott
 * 
 */
public class Deck {

	// CONSTANTS
	public static final int FACEDOWN = 0;
	public static final int FACEUP = 1;
	public static final int TOPCARDUP = 2;

	/**
	 * The cards
	 */
	protected LinkedList<Card> cards = new LinkedList<Card>();
	/**
	 * The back image
	 */
	protected BufferedImage back;
	/**
	 * The location of the deck on the screen
	 */
	protected int x = 0, y = 0;
	/**
	 * The size of the deck
	 */
	protected int width, height;
	/**
	 * The state of the deck
	 */
	protected int state = FACEDOWN;
	/**
	 * Width and height of individual card
	 */
	protected int cardWidth;
	/**
	 * Width and height of individual card
	 */
	protected int cardHeight;

	/**
	 * The table color
	 */
	protected Color tableColor;

	/**
	 * For subclassing
	 */
	Deck() {

	}

	/**
	 * Audio
	 */
	protected AudioClip flipTop = null, dealCard = null, flipOver = null,
			shuffle = null, replaceCard = null;

	/**
	 * Creates a deck with given back image
	 * 
	 * @param back
	 *            the back image
	 */
	Deck(BufferedImage back, Color tableColor) {
		this.back = back;
		this.tableColor = tableColor;
		width = back.getWidth();
		height = back.getHeight();
		cardWidth = width;
		cardHeight = height;
	}

	/**
	 * Creates a deck with given back image and location
	 * 
	 * @param back
	 *            the back image
	 * @param x
	 *            x-coord of deck
	 * @param y
	 *            y-coord of dedk
	 */
	Deck(BufferedImage back, Color tableColor, int x, int y) {
		this(back, tableColor);
		this.x = x;
		this.y = y;
	}

	/**
	 * Sets ace high for all cards
	 */
	public void aceHigh() {
		for (int i = 0; i < numCards(); i++) {
			Card c = dealTopCard();
			c.aceHigh();
			addToBottom(c);
		}
	}

	/**
	 * Sets ace low for all cards
	 */
	public void aceLow() {
		for (int i = 0; i < numCards(); i++) {
			Card c = dealTopCard();
			c.aceLow();
			addToBottom(c);
		}
	}

	/**
	 * @return number of cards in deck
	 */
	public int numCards() {
		return cards.size();
	}

	/**
	 * @return the back image
	 */
	public BufferedImage getBack() {
		return back;
	}

	/**
	 * Adds card to bottom of deck.
	 * 
	 * @param c
	 *            card to add
	 */
	public void addToBottom(Card c) {
		if (c != null)
			if (state != FACEUP)
				cards.addLast(c);
			else
				cards.addFirst(c);
		playSound(replaceCard);
	}

	/**
	 * Adds card to top of deck.
	 * 
	 * @param c
	 *            card to add
	 */
	public void addToTop(Card c) {
		if (c != null)
			if (state != FACEUP) {
				cards.addFirst(c);
			} else
				cards.addLast(c);
		playSound(replaceCard);
	}

	/**
	 * Removes and returns top card
	 * 
	 * @return the top card
	 */
	public Card dealTopCard() {
		if (!isEmpty()) {
			playSound(dealCard);
			if (state != FACEUP) {
				Card topCard = cards.getFirst();
				cards.removeFirst();
				state = FACEDOWN;
				return topCard;
			} else {
				Card topCard = cards.getLast();
				cards.removeLast();
				return topCard;
			}
		} else
			return null;
	}

	/**
	 * Removes and returns bottom card
	 * 
	 * @return the top card
	 */
	public Card dealBottomCard() {
		if (!isEmpty()) {
			playSound(dealCard);
			if (state != FACEUP) {
				Card bottomCard = cards.getLast();
				cards.removeLast();
				state = FACEDOWN;
				return bottomCard;
			} else {
				Card bottomCard = cards.getFirst();
				cards.removeFirst();
				return bottomCard;
			}
		} else
			return null;
	}

	/**
	 * Returns top card without removing it
	 * 
	 * @return the top card
	 */
	public Card peekTopCard() {
		if (!isEmpty())
			if (state != FACEUP)
				return cards.getFirst();
			else
				return cards.getLast();
		else
			return null;
	}

	/**
	 * Turn first card up (only if in FACEDOWN state)
	 */
	public void exposeTopCard() {
		if (state == FACEDOWN) {
			playSound(flipTop);
			state = TOPCARDUP;
		}
	}

	/**
	 * Flips over deck (becomes FACEUP or FACEDOWN)
	 */
	public void flipOver() {
		if (state == FACEUP || state == TOPCARDUP)
			state = FACEDOWN;
		else
			state = FACEUP;
		playSound(flipOver);
	}

	public void faceDown() {
		if (state != FACEDOWN)
			playSound(flipOver);
		state = FACEDOWN;
	}

	public void faceUp() {
		if (state != FACEUP)
			playSound(flipOver);
		state = FACEUP;
	}

	/**
	 * @return true if face up or top card showing
	 */
	public boolean isTopCardShowing() {
		return state == TOPCARDUP || state == FACEUP;
	}

	/**
	 * @return true if deck face down or face down with top card showing
	 */
	public boolean isFaceDown() {
		return state == FACEDOWN || state == TOPCARDUP;
	}

	/**
	 * @return true if face up
	 */
	public boolean isFaceUp() {
		return state == FACEUP;
	}

	/**
	 * Shuffles the cards in the deck
	 */
	public void shuffle() {
		playSound(shuffle);
		for (int i = 0; i < 300; i++) {
			Card c = dealTopCard();
			int newPlace = (int) (Math.random() * cards.size());
			if (newPlace == cards.size() - 1)
				cards.addLast(c);
			else
				cards.add(newPlace, c);
		}
	}

	/**
	 * Empties the deck
	 */
	public void discardAll() {
		cards = new LinkedList<Card>();
	}

	/**
	 * @return array containing the cards
	 */
	public Card[] toArray() {
		Card[] cardArray = new Card[numCards()];
		for (int i = 0; i < cardArray.length; i++) {
			cardArray[i] = dealTopCard();
			addToBottom(cardArray[i]);
		}
		return cardArray;
	}

	/**
	 * Sorts the deck in descending order by rank and suit
	 */
	public void sort() {
		// Dump cards to array
		Card[] cardArray = new Card[numCards()];
		for (int i = 0; i < cardArray.length; i++)
			cardArray[i] = dealTopCard();
		// Sort
		Arrays.sort(cardArray);
		// Get cards back from array
		for (int i = 0; i < cardArray.length; i++)
			cards.addFirst(cardArray[i]);

	}

	/**
	 * Deals a set of cards from the top of the current deck onto the top of a
	 * target deck
	 * 
	 * @param targetDeck
	 *            deck to deal to
	 * @param numCardsToDeal
	 *            num cards to deal
	 * @return true if desired number of cards were dealt
	 */
	public boolean dealToDeck(Deck targetDeck, int numCardsToDeal) {
		boolean success = true;
		if (numCardsToDeal > numCards())
			success = false;
		for (int i = 0; i < numCardsToDeal; i++)
			targetDeck.addToTop(dealTopCard());
		return success;
	}

	/**
	 * Deals a set of cards from the top of the current deck onto the bottom of
	 * a target deck
	 * 
	 * @param targetDeck
	 *            deck to deal to
	 * @param numCardsToDeal
	 *            num cards to deal
	 * @return true if desired number of cards were dealt
	 */
	public boolean dealToDeckBottom(Deck targetDeck, int numCardsToDeal) {
		boolean success = true;
		if (numCardsToDeal > numCards())
			success = false;
		for (int i = 0; i < numCardsToDeal; i++)
			targetDeck.addToBottom(dealTopCard());
		return success;
	}

	/**
	 * Discards selected cards into the target deck.
	 * 
	 * @param targetDeck
	 *            the deck to discard to
	 * @return the number discarded
	 */
	public int discardSelectedToDeck(Deck targetDeck) {
		int discard = 0;
		int n = numCards();
		for (int i = 0; i < n; i++) {
			Card c = this.dealTopCard();
			if (c.isSelected()) {
				c.toggleSelected();
				targetDeck.addToBottom(c);
				discard++;
			} else
				addToBottom(c);
		}
		return discard;
	}

	/**
	 * Removes all cards of the given rank.
	 * 
	 * @param rank
	 *            rank to remove
	 */
	public void removeCardRank(int rank) {
		LinkedList<Card> newCards = new LinkedList<Card>();

		for (Iterator<Card> i = cards.iterator(); i.hasNext();) {
			Card nextCard = i.next();
			if (nextCard.getRank() != rank)
				newCards.add(nextCard);
		}
		cards = newCards;
	}

	/**
	 * Removes all cards of the given suit.
	 * 
	 * @param rank
	 *            rank to remove
	 */
	public void removeCardSuit(int suit) {
		LinkedList<Card> newCards = new LinkedList<Card>();

		for (Iterator<Card> i = cards.iterator(); i.hasNext();) {
			Card nextCard = i.next();
			if (nextCard.getSuit() != suit)
				newCards.add(nextCard);
		}
		cards = newCards;
	}

	/**
	 * @return true if deck empty
	 */
	public boolean isEmpty() {
		return cards.size() == 0;
	}

	/**
	 * @return a string representation of the deck
	 */
	public String toString() {
		String output;
		if (state == FACEDOWN)
			output = "DECK: FACE DOWN\n";
		else if (state == FACEUP)
			output = "DECK: FACE UP\n";
		else
			output = "DECK: FACE DOWN - TOP CARD UP\n";

		for (int i = 0; i < cards.size(); i++) {
			output += cards.get(i).toString() + "\n";
		}
		return output;
	}

	/**
	 * Returns card clicked on or null if no card clicked on
	 * 
	 * @param clickX
	 *            x-coord of the click
	 * @param clickY
	 *            y-coord of the click
	 * @return top card if deck clicked
	 */
	public Card clickedOn(int clickX, int clickY) {
		if (clickX < x + width & clickX >= x & clickY < y + height
				& clickY >= y)
			return peekTopCard();
		else
			return null;
	}

	public boolean hitTheDeck(int clickX, int clickY) {
		if (clickX < x + width & clickX >= x & clickY < y + height
				& clickY >= y)
			return true;
		else
			return false;

	}

	/**
	 * @param index
	 *            index
	 * @return card at given index
	 */
	public Card getCard(int index) {
		if (index < numCards())
			return cards.get(index);
		return null;
	}

	/**
	 * removes the all cards that match the given card
	 * 
	 * @param c
	 *            the card to remove
	 * @return number of cards removed
	 */
	public int removeCard(Card c) {
		int numRemoved = 0;
		int numCards = numCards();
		for (int i = 0; i < numCards; i++) {
			Card d = dealTopCard();
			if (d.getRank() != c.getRank() | d.getSuit() != c.getSuit())
				addToBottom(d);
		}
		return numRemoved;
	}

	/**
	 * Draws the deck on a graphics context
	 * 
	 * @param g
	 *            the graphics context
	 */
	public void draw(Graphics g) {
		int cardY = y;

		if (isEmpty()) {
			g.setColor(Color.LIGHT_GRAY);
			g.drawRoundRect(x + 1, y + 1, width - 2, height - 2,
					(int) (width * .1), (int) (height * .1));
		} else {
			if (peekTopCard().isSelected()) {
				cardY -= 5;
				g.setColor(Color.black);
				g.fillRoundRect(x + 2, cardY + 5, width, height,
						(int) (width * .2), (int) (height * .2));
			}
			if (state != FACEDOWN)
				g.drawImage(peekTopCard().getImage(), x, cardY, width, height,
						null);
			else
				g.drawImage(back, x, cardY, null);
		}
	}

	/**
	 * @return x-coord of deck
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return y-coord of deck
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return width of deck
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return height of deck
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Set x-coord of deck
	 * 
	 * @param x
	 *            new x-coord
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * set y-coord of deck
	 * 
	 * @param y
	 *            new y-coord
	 */
	public void setY(int y) {
		this.y = y;
	}

	public int getCardWidth() {
		return cardWidth;
	}

	public int getCardHeight() {
		return cardHeight;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the tableColor
	 */
	public Color getTableColor() {
		return tableColor;
	}

	/**
	 * @param tableColor
	 *            the tableColor to set
	 */
	public void setTableColor(Color tableColor) {
		this.tableColor = tableColor;
	}

	/**
	 * @param flipTop
	 *            the flipTop to set
	 */
	public void setFlipTop(AudioClip flipTop) {
		this.flipTop = flipTop;
	}

	/**
	 * @param dealCard
	 *            the dealCard to set
	 */
	public void setDealCard(AudioClip dealCard) {
		this.dealCard = dealCard;
	}

	/**
	 * @param flipOver
	 *            the flipOver to set
	 */
	public void setFlipOver(AudioClip flipOver) {
		this.flipOver = flipOver;
	}

	/**
	 * @param shuffle
	 *            the shuffle to set
	 */
	public void setShuffle(AudioClip shuffle) {
		this.shuffle = shuffle;
	}

	/**
	 * @param replaceCard
	 *            the replaceCard to set
	 */
	public void setReplaceCard(AudioClip replaceCard) {
		this.replaceCard = replaceCard;
	}

	private void playSound(AudioClip a) {
		if (a != null)
			a.play();
	}
}

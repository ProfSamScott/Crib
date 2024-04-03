import java.awt.image.BufferedImage;

/**
 * Class for a playing card object
 * 
 * @author Sam Scott
 */
public class Card implements Comparable<Card> {

	// CONSTANTS
	public static final int CLUBS = 0;
	public static final int DIAMONDS = 1;
	public static final int HEARTS = 2;
	public static final int SPADES = 3;
	public static final int LOWACE = 1;
	public static final int HIGHACE = 14;
	public static final int JACK = 11;
	public static final int QUEEN = 12;
	public static final int KING = 13;
	public static final int JOKER = -1;

	/**
	 * rank of the card
	 */
	private int rank;
	/**
	 * suit of the card
	 */
	private int suit;
	/**
	 * name of the card
	 */
	private String name;
	/**
	 * card image
	 */
	private BufferedImage image;
	/**
	 * is card selected?
	 */
	private boolean selected = false;

	/**
	 * Create and initialize
	 * 
	 * @param image
	 *            card image
	 * @param rank
	 *            card rank
	 * @param suit
	 *            card suit
	 */
	public Card(BufferedImage image, int rank, int suit) {
		this.image = image;
		this.rank = rank;
		this.suit = suit;
		name = "";
		switch (rank) {
		case LOWACE:
		case HIGHACE:
			name = name + "Ace of ";
			break;
		case JACK:
			name = name + "Jack of ";
			break;
		case QUEEN:
			name = name + "Queen of ";
			break;
		case KING:
			name = name + "King of ";
			break;
		case JOKER:
			name = name + "Joker";
			break;
		default:
			name = name + rank + " of ";
			break;
		}
		switch (suit) {
		case HEARTS:
			name = name + "Hearts";
			break;
		case CLUBS:
			name = name + "Clubs";
			break;
		case DIAMONDS:
			name = name + "Diamonds";
			break;
		case SPADES:
			name = name + "Spades";
			break;
		}
	}

	/**
	 * Sets ace high
	 */
	public void aceHigh() {
		if (rank == LOWACE)
			rank = HIGHACE;
	}

	/**
	 * Sets ace low
	 */
	public void aceLow() {
		if (rank == HIGHACE)
			rank = LOWACE;
	}

	/**
	 * Returns the Cribbage value of the card (1-10)
	 * 
	 * @return the value
	 */
	public int getCribValue()
	{
		if (getRank() > 10)
			return 10;
		return rank;
	}
	
	/**
	 * @return rank of card
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * @return suit of card
	 */
	public int getSuit() {
		return suit;
	}

	/**
	 * @return name of card
	 */
	public String toString() {
		return name;
	}

	/**
	 * @return image of card
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * Compares current card to c, first by rank, then by suit
	 * 
	 * @param c
	 *            the card to compare to
	 * @return negative, zero, or positive if current card is less than, equal
	 *         to, or greater than c
	 */
	public int compareTo(Card c) {
		if (c == null)
			return 1;
		if (getRank() != c.getRank())
			return getRank() - c.getRank();
		if (getSuit() != c.getSuit())
			return getSuit() - c.getSuit();
		return 0;
	}

	/**
	 * @return whether selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * toggles selected status
	 * 
	 * @return new selected status
	 */
	public boolean toggleSelected() {
		if (selected)
			selected = false;
		else
			selected = true;
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}

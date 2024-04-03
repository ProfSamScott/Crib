import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * This class calculates a score when created, and then displays it on a
 * graphics context.
 * 
 * @author Sam
 * 
 */
public class CribScore extends Deck {

	/**
	 * The hand
	 */
	protected Hand hand;
	/**
	 * The turn-up
	 */
	protected Card topCard;
	/**
	 * The score
	 */
	protected int score = 0;
	/**
	 * The fifteens (stored as hands)
	 */
	protected LinkedList<Hand> fifteens = new LinkedList<Hand>();
	/**
	 * The pairs (stored as hands)
	 */
	protected LinkedList<Hand> pairs = new LinkedList<Hand>();
	/**
	 * The runs (stored as hands)
	 */
	protected LinkedList<Hand> runs = new LinkedList<Hand>();
	/**
	 * The flush (if there is one -- stored as a hand)
	 */
	protected Hand flush;
	/**
	 * The nob card (if there is one -- stored as a hand)
	 */
	protected Hand nob;
	/**
	 * The two for his heels card (if there is one -- stored as a hand)
	 */
	protected Hand heels;
	/**
	 * scale for drawing the cards in the scoring screen
	 */
	protected final double scale = .3;
	/**
	 * true if this is a crib hand
	 */
	protected boolean crib;
	/**
	 * header message
	 */
	protected String message = "Total Score";

	protected Color textColor = new Color(220, 220, 220);
	protected Color scoreColor = textColor;

	Image backDrop;

	/**
	 * Default constructor for subclasses
	 */
	public CribScore() {

	}

	/**
	 * Constructor
	 * 
	 * @param hand
	 *            the hand
	 * @param topCard
	 *            the turn-up
	 * @param crib
	 *            true if a crib hand
	 */
	public CribScore(Hand hand, Card topCard, boolean crib) {
		this.hand = new Hand(hand);
		this.topCard = topCard;
		this.crib = crib;
		cardHeight = (int) (hand.getCardHeight() * scale);
		cardWidth = (int) (hand.getCardWidth() * scale);
		this.width = cardWidth;
		this.height = cardHeight;
		flush = new Hand(hand.getBack(), tableColor, 0, 0, cardWidth * 6,
				cardHeight);
		nob = new Hand(hand.getBack(), tableColor, 0, 0, cardWidth, cardHeight);
		heels = new Hand(hand.getBack(), tableColor, 0, 0, cardWidth,
				cardHeight);
		generateScore();
	}

	/**
	 * Constructor
	 * 
	 * @param hand
	 *            the hand
	 * @param topCard
	 *            the turn-up
	 * @param crib
	 *            true if a crib hand
	 * @param x
	 *            x-coord of scoreboard
	 * @param y
	 *            y-coord of scoreboard
	 */
	public CribScore(Hand hand, Card topCard, boolean crib, int x, int y) {
		this(hand, topCard, crib);
		this.x = x;
		this.y = y;
	}

	/**
	 * Constructor
	 * 
	 * @param hand
	 *            the hand
	 * @param topCard
	 *            the turn-up
	 * @param crib
	 *            true if a crib hand
	 * @param x
	 *            x-coord of scoreboard
	 * @param y
	 *            y-coord of scoreboard
	 * @param width
	 *            width of scoreboard
	 * @param height
	 *            height of scoreboard
	 */
	public CribScore(Hand hand, Card topCard, boolean crib, int x, int y,
			int width, int height) {
		this(hand, topCard, crib, x, y);
		this.width = width;
		this.height = height;
	}

	/**
	 * Constructor *
	 * 
	 * @param hand
	 *            the hand
	 * @param topCard
	 *            the turn-up
	 * @param crib
	 *            true if a crib hand
	 * @param x
	 *            x-coord of scoreboard
	 * @param y
	 *            y-coord of scoreboard
	 * @param width
	 *            width of scoreboard
	 * @param height
	 *            height of scoreboard
	 * @param message
	 *            header message on scoreboard
	 */
	public CribScore(Hand hand, Card topCard, boolean crib, int x, int y,
			int width, int height, String message) {
		this(hand, topCard, crib, x, y, width, height);
		this.message = message;
	}

	public CribScore(Hand hand, Card topCard, boolean crib, int x, int y,
			String message, Image backDrop) {
		this(hand, topCard, crib, x, y, backDrop.getWidth(null), backDrop
				.getHeight(null), message);
		this.backDrop = backDrop;
	}

	public CribScore(Hand hand, Card topCard, boolean crib, int x, int y,
			String message, Image backDrop, Color scoreColor) {
		this(hand, topCard, crib, x, y, backDrop.getWidth(null), backDrop
				.getHeight(null), message);
		this.backDrop = backDrop;
		this.scoreColor = scoreColor;
	}

	/**
	 * Called once when created to generate a score
	 */
	private void generateScore() {
		score = 0;
		if (hand.numCards() == 0) // Empty hand means two for his heels
			score += twoForHisHeels();
		else if (hand.numCards() == 4) {
			score += oneForHisNob();
			score += flush();
			hand.addToBottom(topCard);
			hand.sort();
			score += fifteens();
			score += pairs();
			score += runs();
		}
	}

	/**
	 * Adds up the card values in a hand
	 * 
	 * @param hand
	 *            the hand
	 * @return the sum
	 */
	protected int sum(Hand hand) {
		int sum = 0;
		for (int i = 0; i < hand.numCards(); i++) {
			Card c = hand.dealTopCard();
			sum += c.getCribValue();
			hand.addToBottom(c);
		}
		return sum;
	}

	/**
	 * Calculates the heels score
	 * 
	 * @return the heels score
	 */
	private int twoForHisHeels() {
		if (topCard != null)
			if (topCard.getRank() == Card.JACK) {
				heels.addToTop(topCard);
				heels.flipOver();
				return 2;
			}
		return 0;
	}

	/**
	 * Calculates the nob score
	 * 
	 * @return the nob score
	 */
	private int oneForHisNob() {
		if (topCard != null)
			for (int i = 0; i < hand.numCards(); i++) {
				Card c = hand.dealTopCard();
				hand.addToBottom(c);
				if (c.getRank() == Card.JACK & c.getSuit() == topCard.getSuit()) {
					nob.addToTop(c);
					nob.flipOver();
					return 1;
				}
			}
		return 0;
	}

	/**
	 * calculates fifteens score
	 * 
	 * @return the fifteens score
	 */
	private int fifteens() {
		int localScore = 0;
		// 5-card hand (or 4 if no topcard)
		if (sum(hand) == 15) {
			localScore += 2;
			Hand h = new Hand(hand);
			h.flipOver();
			fifteens.addLast(h);
		}
		// 4-card hands (or 3 if no topcard)
		for (int i = 0; i < hand.numCards(); i++) {
			Card c = hand.dealTopCard();
			if (sum(hand) == 15) {
				localScore += 2;
				Hand h = new Hand(hand);
				h.flipOver();
				fifteens.addLast(h);
			}
			hand.addToBottom(c);
		}
		// 3-card hands
		Card[] cardArray = hand.toArray();
		if (topCard != null) {
			for (int i = 0; i < hand.numCards() - 2; i++)
				for (int j = i + 1; j < hand.numCards() - 1; j++)
					for (int k = j + 1; k < hand.numCards(); k++) {
						Hand h = new Hand(hand);
						h.discardAll();
						h.addToTop(cardArray[i]);
						h.addToTop(cardArray[j]);
						h.addToTop(cardArray[k]);
						h.flipOver();
						if (sum(h) == 15) {
							localScore += 2;
							fifteens.addLast(h);
						}
					}
		}
		// 2-card hands
		for (int i = 0; i < hand.numCards() - 1; i++)
			for (int j = i + 1; j < hand.numCards(); j++) {
				Hand h = new Hand(hand);
				h.discardAll();
				h.addToTop(cardArray[i]);
				h.addToTop(cardArray[j]);
				h.flipOver();
				if (sum(h) == 15) {
					localScore += 2;
					fifteens.addLast(h);
				}
			}
		return localScore;
	}

	/**
	 * calculates the pairs score
	 * 
	 * @return the pairs score
	 */
	private int pairs() {
		int localScore = 0;
		Card[] cardArray = hand.toArray();
		for (int i = 0; i < hand.numCards() - 1; i++)
			for (int j = i + 1; j < hand.numCards(); j++) {
				if (cardArray[i].getRank() == cardArray[j].getRank()) {
					Hand h = new Hand(hand);
					h.discardAll();
					h.addToTop(cardArray[i]);
					h.addToTop(cardArray[j]);
					h.flipOver();
					localScore += 2;
					pairs.addLast(h);
				}
			}
		return localScore;
	}

	/**
	 * is this hand a run?
	 * 
	 * @param h
	 *            a hand
	 * @return true if hand is a run
	 */
	protected boolean run(Hand h) {
		h.sort();
		// DESCENDING
		Card c = h.dealTopCard();
		h.addToBottom(c);
		int i;
		for (i = 1; i < h.numCards(); i++) {
			Card c2 = h.dealTopCard();
			h.addToBottom(c2);
			if (c2.getRank() != c.getRank() + 1)
				break;
			c = c2;
		}
		if (i == h.numCards())
			return true;

		return false;
	}

	/**
	 * Computes score from runs
	 * 
	 * @return score from runs
	 */
	private int runs() {
		int localScore = 0;
		// 5-card hand
		Hand h = new Hand(hand);
		h.flipOver();
		// 5-card hand (or 4-card if no top card)
		if (run(h)) {
			if (topCard != null)
				localScore += 5;
			else
				localScore += 4;
			runs.addLast(h);
		}
		if (localScore == 0) {
			// 4-card hands (or 3-card if no top card)
			for (int i = 0; i < hand.numCards(); i++) {
				Card c = hand.dealTopCard();
				h = new Hand(hand);
				h.flipOver();
				if (run(h)) {
					if (topCard != null)
						localScore += 4;
					else
						localScore += 3;
					runs.addLast(h);
				}
				hand.addToBottom(c);
			}
			if (localScore == 0 & topCard != null) {
				// 3-card hands
				Card[] cardArray = hand.toArray();
				for (int i = 0; i < hand.numCards() - 2; i++)
					for (int j = i + 1; j < hand.numCards() - 1; j++)
						for (int k = j + 1; k < hand.numCards(); k++) {
							h = new Hand(hand);
							h.discardAll();
							h.addToTop(cardArray[i]);
							h.addToTop(cardArray[j]);
							h.addToTop(cardArray[k]);
							h.flipOver();
							if (run(h)) {
								localScore += 3;
								runs.addLast(h);
							}
						}
			}
		}
		return localScore;
	}

	/**
	 * computes the flush score
	 * 
	 * @return the flush score
	 */
	private int flush() {
		Card c = hand.dealTopCard();
		int suit = c.getSuit();
		hand.addToBottom(c);
		int i;
		for (i = 0; i < hand.numCards() - 1; i++) {
			c = hand.dealTopCard();
			hand.addToBottom(c);
			if (c.getSuit() != suit)
				break;
		}
		if (i < hand.numCards() - 1)
			return 0;
		flush = new Hand(hand);
		flush.flipOver();
		if (topCard != null)
			if (topCard.getSuit() == suit) {
				flush.addToTop(topCard);
				return 5;
			}

		// Crib only scores 5-flush
		if (!crib)
			return 4;
		flush.discardAll();
		return 0;
	}

	/**
	 * draw the scoreboard
	 * 
	 * @param g
	 *            the graphics context
	 */
	public void draw(Graphics g) {
		double scale = 1;
		int x = this.x;
		int y = this.y;

		// WORK OUT THE SCALE
		int displayHeight = 0;
		// System.out.print(" " + displayHeight);
		displayHeight = fifteens.isEmpty() ? displayHeight : displayHeight + 12;
		displayHeight += fifteens.size() * (cardHeight + 10);
		// System.out.print(" " + displayHeight);
		displayHeight = pairs.isEmpty() ? displayHeight : displayHeight + 12;
		displayHeight += pairs.size() * (cardHeight + 10);
		// System.out.print(" " + displayHeight);
		displayHeight = runs.isEmpty() ? displayHeight : displayHeight + 12;
		displayHeight += runs.size() * (cardHeight + 10);
		// System.out.print(" " + displayHeight);
		displayHeight = flush.isEmpty() ? displayHeight : displayHeight
				+ cardHeight + 17;
		// System.out.print(" " + displayHeight);
		displayHeight = nob.isEmpty() ? displayHeight : displayHeight
				+ cardHeight + 17;
		// System.out.print(" " + displayHeight);
		displayHeight = heels.isEmpty() ? displayHeight : displayHeight
				+ cardHeight + 17;
		// System.out.print(" " + displayHeight + " ");
		// System.out.print("displayHeight: " + displayHeight
		// + " backDrop.getHeight() - 35: " + (backDrop.getHeight() - 35)
		// + " scale: ");
		if (displayHeight > backDrop.getHeight(null) - 35)
			scale = (double) (backDrop.getHeight(null) - 35) / displayHeight;
		// System.out.println(scale);

		// BACKDROP
		g.drawImage(backDrop, x, y, null);
		x += 6;
		y += 8;
		// SHOW SCORE
		g.setColor(textColor);
		g.setFont(new Font("SansSerif", Font.BOLD, 14));
		y += 16;
		if (heels.isEmpty())
			g.drawString(message + ": " + score, x, y);
		else
			g.drawString(message, x, y);
		y += (int) 5;
		g.drawLine(x, y, x + cardWidth * 5 + nob.spacer * 4 + 20, y);
		g.setColor(scoreColor);
		// SHOW FIFTEENS
		if (!fifteens.isEmpty())
			y += (int) (12 * scale);
		for (int i = 0; i < fifteens.size(); i++) {
			g.setFont(new Font("SansSerif", Font.BOLD, 12));
			g.drawString("+2", x, y + (int) (cardHeight * scale + 0.5) / 2 + 6);
			Hand h = fifteens.remove();
			h.setX(x + 20);
			h.setY(y);
			h.setWidth(cardWidth * 6);
			h.setHeight((int) (cardHeight * scale + 0.5));
			h.drawBrief(g);
			fifteens.addLast(h);
			y += (int) ((cardHeight + 10) * scale + 0.5);
		}
		// SHOW PAIRS
		if (!pairs.isEmpty())
			y += (int) (12 * scale + 0.5);
		for (int i = 0; i < pairs.size(); i++) {
			g.setFont(new Font("SansSerif", Font.BOLD, 12));
			g.drawString("+2", x, y + (int) (cardHeight * scale + 0.5) / 2 + 6);
			Hand h = pairs.remove();
			h.setX(x + 20);
			h.setY(y);
			h.setWidth(cardWidth * 6);
			h.setHeight((int) (cardHeight * scale + 0.5));
			h.drawBrief(g);
			pairs.addLast(h);
			y += (int) ((cardHeight + 10) * scale + 0.5);
		}
		// SHOW RUNS
		if (!runs.isEmpty())
			y += (int) (12 * scale + 0.5);
		for (int i = 0; i < runs.size(); i++) {
			g.setFont(new Font("SansSerif", Font.BOLD, 12));
			g.drawString("+" + runs.getFirst().numCards(), x, y
					+ (int) (cardHeight * scale + 0.5) / 2 + 6);
			Hand h = runs.remove();
			h.setX(x + 20);
			h.setY(y);
			h.setWidth(cardWidth * 6);
			h.setHeight((int) (cardHeight * scale + 0.5));
			h.drawBrief(g);
			runs.addLast(h);
			y += (int) ((cardHeight + 10) * scale + 0.5);
		}
		// SHOW FLUSH
		if (!flush.isEmpty()) {
			y += (int) (12 * scale + 0.5);
			g.setFont(new Font("SansSerif", Font.BOLD, 12));
			g.drawString("+" + flush.numCards(), x, y
					+ (int) (cardHeight * scale + 0.5) / 2 + 6);
			flush.setX(x + 20);
			flush.setY(y);
			flush.setWidth((cardWidth * 6));
			flush.setHeight((int) (cardHeight * scale + 0.5));
			flush.drawBrief(g);
			y += (int) ((cardHeight + 5) * scale + 0.5);
		}
		// SHOW ONE FOR HIS NOB
		if (!nob.isEmpty()) {
			y += (int) (12 * scale + 0.5);
			g.setFont(new Font("SansSerif", Font.BOLD, 12));
			g.drawString("+1", x, y + (int) (cardHeight * scale + 0.5) / 2 + 6);
			nob.setX(x + 20);
			nob.setY(y);
			flush.setHeight((int) (cardHeight * scale + 0.5));
			nob.drawBrief(g);
			y += (int) ((cardHeight + 5) * scale + 0.5);
		}
		// SHOW TWO FOR HIS HEELS
		if (!heels.isEmpty()) {
			y += (int) (12 * scale + 0.5);
			g.setFont(new Font("SansSerif", Font.BOLD, 12));
			g.drawString("+2", x, y + (int) (cardHeight * scale + 0.5) / 2 + 6);
			heels.setX(x + 20);
			heels.setY(y);
			flush.setHeight((int) (cardHeight * scale + 0.5));
			heels.drawBrief(g);
			y += (int) ((cardHeight + 5) * scale + 0.5);
		}
		g.setColor(textColor);
		g.drawLine(x, y, x + cardWidth * 5 + nob.spacer * 4 + 20, y);
		/*
		 * g.setColor(Color.DARK_GRAY); g.fillRect(this.x + width - 50 - 5,
		 * this.y + height - 30 - 5, 50 , 30); g.setColor(textColor); for (int i =
		 * 0; i < 5; i++) g.drawRect(this.x + width - 50 + i - 5, this.y +
		 * height - 30 + i - 5, 50 - i * 2, 30 - i * 2);
		 * 
		 * g.setFont(new Font("SansSerif", Font.BOLD, 16)); g.drawString("OK",
		 * this.x+width-50+5+5,this.y+height-30+5+12);
		 */
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}
}

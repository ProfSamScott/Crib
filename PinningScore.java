import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class PinningScore extends CribScore {

	public int pinningTotal = 0;

	private LinkedList<Color> colors = new LinkedList<Color>();
	private LinkedList<Integer> scores = new LinkedList<Integer>();
	private boolean startNewRound = true;

	public PinningScore(Hand hand, int x, int y, int width, int height,
			Image backDrop) {
		this.hand = new Hand(hand.getBack(), tableColor, 0, 0, cardWidth,
				cardHeight);
		this.hand.noSort();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.backDrop = backDrop;
		cardHeight = (int) (hand.getCardHeight() * scale);
		cardWidth = (int) (hand.getCardWidth() * scale);
		message = "Pegging";
	}

	public PinningScore(PinningScore p) {
		this.hand = new Hand(p.hand);
		this.startNewRound = p.isStartNewRound();
		System.out.println("new ps "+hand.numCards());
		this.hand.noSort();
		this.pinningTotal = p.getPinningTotal();
	}

	public boolean addCard(Card c, Color color) {
		if (startNewRound)
			clear();
		if (c.getCribValue() + pinningTotal > 31)
			return false;
		hand.addToBottom(c);
		pinningTotal += c.getCribValue();
		colors.addLast(color);
		startNewRound = false;

		// SHOULD SCORE THE CARD HERE
		score = 0;
		// RUN OF THREE TWICE - REMOVED
	/*	if (hand.numCards() >= 4) {
			// System.out.println("ROTT");
			Hand h = new Hand(hand);
			Hand h2 = new Hand(hand);
			h.discardAll();
			h2.flipOver();
			h2.dealToDeck(h, 4);
			// System.out.println(h.toString());
			h.sort();
			// System.out.println(h.toString());
			if (h.getCard(0).getRank() == h.getCard(1).getRank()) {
				h.removeCard(h.getCard(0));
				h.flipOver();
				// System.out.println(h.toString());
				if (run(h)) {
					score += 8;
				}
			} else if (h.getCard(1).getRank() == h.getCard(2).getRank()
					| h.getCard(2).getRank() == h.getCard(3).getRank()) {
				h.removeCard(h.getCard(2));
				h.flipOver();
				// System.out.println(h.toString());
				if (run(h)) {
					score += 8;
				}
			}
		}
		if (score == 0) // only check pairs and runs if no run of 3 twice
		{*/
			// PAIRS
			int comparison = hand.getCard(hand.numCards() - 1).getRank();
			if (hand.numCards() >= 2) {
				if (hand.getCard(hand.numCards() - 2).getRank() == comparison) {
					score += 2;
					if (hand.numCards() >= 3)
						if (hand.getCard(hand.numCards() - 3).getRank() == comparison) {
							score += 4;
							if (hand.numCards() >= 4)
								if (hand.getCard(hand.numCards() - 4).getRank() == comparison)
									score += 6;
						}
				}
			}
			// RUNS
			if (hand.numCards() >= 3) {
				Card[] cardArray = hand.toArray();
				for (int size = hand.numCards(); size >= 3; size--) {
					CribHand h = new CribHand(hand.getBack(), null);
					for (int i = hand.numCards() - 1; i >= hand.numCards()
							- size; i--)
						h.addToBottom(cardArray[i]);
					//System.out.println(h.toString());
					h.flipOver();
					if (run(h)) {
						score += size;
						break;
					}
				}
			}
	//	}
		// FIFTEENS
		if (sum(hand) == 15)
			score += 2;
		scores.addLast(score);
		return true;
	}

	public void clear() {
		hand.discardAll();
		colors = new LinkedList<Color>();
		scores = new LinkedList<Integer>();
	}

	public void roundOver(Color color) {
		startNewRound = true;
		pinningTotal = 0;
		if (sum(hand) == 31)
			score = 2;
		else
			score = 1;
		scores.addLast(score);
		colors.addLast(color);
	}

	public void draw(Graphics g) {
		int x = this.x;
		int y = this.y;

		// BACKDROP
		g.drawImage(backDrop, x, y, null);
		x += 6;
		y += 10;
		// SHOW SCORE
		g.setFont(new Font("SansSerif", Font.BOLD, 14));
		g.setColor(new Color(220, 220, 220));
		y += 16;
		g.drawString(message, x, y);
		y += 5;
		g.drawLine(x, y, x + cardWidth * 5 + hand.spacer * 4 + 20, y);
		// SHOW CARDS
		y += 12;
		g.setFont(new Font("SansSerif", Font.BOLD, 12));
		int runningTotal = 0;
		for (int i = 0; i < hand.numCards(); i++) {
			Card c = hand.dealTopCard();
			hand.addToBottom(c);
			Color color = colors.remove();
			colors.addLast(color);
			int score = scores.remove();
			scores.addLast(score);
			runningTotal += c.getCribValue();
			g.setColor(color);
			if (score != 0)
				g.drawString("+" + score, x, y + cardHeight / 2 + 6);
			Hand h = new Hand(hand);
			h.flipOver();
			h.discardAll();
			h.addToBottom(c);
			h.setX(x + 20);
			h.setY(y);
			h.setWidth(cardWidth * 6);
			h.setHeight(cardHeight);
			h.drawBrief(g);
			g.drawString("" + runningTotal, x + 20 + cardWidth + 10, y
					+ cardHeight / 2 + 6);
			y += cardHeight + 10;
		}
		if (startNewRound && !hand.isEmpty()) {
			g.setColor(new Color(220, 220, 220));
			g.drawLine(x, y, x + cardWidth * 5 + hand.spacer * 4 + 20, y);
		}
		if (scores.size() > hand.numCards()) {
			int score = scores.remove();
			scores.addLast(score);
			Color color = colors.remove();
			colors.addLast(color);
			g.setColor(color);
			// y-=cardHeight+10;
			y += 16;
			g.drawString("+" + score, x /* + 20 + cardWidth*3 + 10 */, y
			/* + cardHeight / 2 + 6 */);
		}
	}

	/**
	 * @return the pinningTotal
	 */
	public int getPinningTotal() {
		return pinningTotal;
	}

	/**
	 * @return the startNewRound
	 */
	public boolean isStartNewRound() {
		return startNewRound;
	}

	/**
	 * @param startNewRound the startNewRound to set
	 */
	public void setStartNewRound(boolean startNewRound) {
		this.startNewRound = startNewRound;
	}
}

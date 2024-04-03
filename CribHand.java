import java.applet.AudioClip;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class CribHand extends Hand {

	/**
	 * Constructor
	 * 
	 * @param back
	 *            the back image from the deck
	 */
	CribHand(BufferedImage back, Color tableColor) {
		super(back, tableColor);
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
	CribHand(BufferedImage back, Color tableColor, int x, int y) {
		super(back, tableColor, x, y);
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
	CribHand(BufferedImage back, Color tableColor, int x, int y, int width,
			int height) {
		super(back, tableColor, x, y, width, height);
	}

	/**
	 * Constructor - creates duplicate of given hand
	 * 
	 * @param hand
	 *            hand to duplicate
	 */
	CribHand(CribHand hand) {
		super(hand.getBack(), hand.getTableColor(), hand.getX(), hand.getY(),
				hand.getWidth(), hand.getHeight());
	}

	// public Card clickedOn(int clickX, int clickY) {
	// return clickedOn(clickX, clickY, null, null);
	// }

	/**
	 * Determines if there is a card that could be played in pinning.
	 * 
	 * @param pinningTotal
	 *            the total so far in pinning
	 * @return true if there is a card to be played
	 */
	public boolean canPin(PinningScore ps) {
		for (int i = 0; i < numCards(); i++)
			if (getCard(i).getCribValue() + ps.getPinningTotal() <= 31)
				return true;
		return false;
	}

	/**
	 * Determines best pin card. Assumes there is at least one card that it can
	 * play.
	 * 
	 * @param ps
	 *            the current pinning score
	 * @return the card
	 */
	public Card choosePinCard(PinningScore ps) {
		Card chosenCard = null;

		// LOOK FOR SCORING CARDS
		int bestScore = -100;
		for (int i = 0; i < numCards(); i++) {
			Card c = dealTopCard();
			addToBottom(c);
			if (c.getCribValue() + ps.getPinningTotal() <= 31) {
				int score = 0;
				// bias towards bigger cards, but save 5s
				if (c.getCribValue() != 5)
					score += c.getCribValue() - 1;
				// add 10 times pinning score
				PinningScore p = new PinningScore(ps);
				p.addCard(c, null);
				score += p.getScore() * 10;
				if (p.getPinningTotal() == 31)
					score += 20;
				// penalties for a total of 5, 10, or 21
				if (p.getPinningTotal() == 5)
					score -= 10;
				if (p.getPinningTotal() == 21)
					score -= 10;
				if (p.getPinningTotal() == 10)
					score -= 5;
				System.out.print(score + " " + p.getPinningTotal() + " ");
				System.out.println(score + ".");
				if (score > bestScore) {
					chosenCard = c;
					bestScore = score;
				}
			}
		}
		/*
		if (bestScore == 0) {
			// NO SCORING CARDS FOUND
			chosenCard = dealTopCard();
			addToBottom(chosenCard);
			for (int i = 1; i < numCards(); i++) {
				Card c = dealTopCard();
				addToBottom(c);
				if (c.getCribValue() + ps.getPinningTotal() <= 31
						& c.getRank() != 5
						& c.getCribValue() + ps.getPinningTotal() != 5
						& c.getCribValue() + ps.getPinningTotal() != 21)
					if (c.getRank() > chosenCard.getRank())
						chosenCard = c;
			}
		}
		if (chosenCard.getCribValue() + ps.getPinningTotal() > 31) {
			// LAST DITCH EFFORT
			chosenCard = dealTopCard();
			addToBottom(chosenCard);
			for (int i = 1; i < numCards(); i++) {
				Card c = dealTopCard();
				addToBottom(c);
				if (c.getCribValue() + ps.getPinningTotal() <= 31) {
					chosenCard = c;
					break;
				}
			}

		}
		if (ps.getPinningTotal() == 0 & chosenCard.getRank() == 5) {
			// NO FIVES!
			chosenCard = dealTopCard();
			addToBottom(chosenCard);
			for (int i = 1; i < numCards(); i++) {
				Card c = dealTopCard();
				addToBottom(c);
				if (c.getRank() == 5) {
					chosenCard = c;
					break;
				}
			}
		}
*/
		removeCard(chosenCard);
		return chosenCard;
	}

	public boolean selectBestDiscard(boolean myCrib) {
		int bestScore = 0;
		int bestDiscard1 = 0;
		int bestDiscard2 = 1;
		Card[] cardArray = toArray();
		unselectAll();

		if (numCards() != 6)
			return false;
		else {
			for (int i = 0; i < 5; i++)
				for (int j = i + 1; j < 6; j++) {
					CribHand h = new CribHand(getBack(), tableColor);
					for (int k = 0; k < 6; k++) {
						if (k != i & k != j)
							h.addToBottom(cardArray[k]);
					}
					int score = new CribScore(h, null, false).getScore();
					// System.out.print("i j " + i + " " + j + " score " +
					// score);
					if (!myCrib) { // CHECK THE POINTS YOU'RE GIVING AWAY
						if (cardArray[i].getRank() == cardArray[j].getRank())
							score -= 2;
						if (cardArray[i].getCribValue()
								+ cardArray[j].getCribValue() == 15)
							score -= 2;
						if (cardArray[i].getRank() == 5)
							score -= 4;
						if (cardArray[j].getRank() == 5)
							score -= 4;
						if (cardArray[i].getCribValue()
								+ cardArray[j].getCribValue() == 5)
							score -= 4;
					} else {
						if (cardArray[i].getRank() == cardArray[j].getRank())
							score += 2;
						if (cardArray[i].getCribValue()
								+ cardArray[j].getCribValue() == 15)
							score += 2;
					}
					if (score > bestScore) {
						bestScore = score;
						bestDiscard1 = i;
						bestDiscard2 = j;
					}
					// System.out.println(" best discards " + bestDiscard1 + " "
					// + bestDiscard2);
				}
		}
		cardArray[bestDiscard1].setSelected(true);
		cardArray[bestDiscard2].setSelected(true);
		// System.out.println(bestScore);
		return true;
	}

	public Card clickedOn(int clickX, int clickY, AudioClip goodSound,
			AudioClip badSound) {
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
			if (numCards() > 4) {
				if (cardClicked.isSelected() || numCardsUnselected() > 4) {
					cardClicked.toggleSelected();
					if (goodSound != null)
						goodSound.play();
				} else if (badSound != null)
					badSound.play();
			}
			return cardClicked;
		} else
			badSound.play();

		return null;
	}

}

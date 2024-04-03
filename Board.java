import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Board {

	private final int endScore = 121;
	private final int pinRadius = 7;
	private Color[] playerColors;
	private AudioClip pinOut, pinIn;
	private int highlightPlayer = -1;

	Image boardImage;
	int x, y;
	int[] score = { 0, 0 };

	// First index is player, second is spot on board, third is 0 for x, 1 for
	// y.
	int[][][] pinLocations = new int[2][123][2];

	// Two pins for each player.
	int[][] pins = { { 0, -1 }, { 0, -1 } };

	public Board(Image boardImage, int x, int y, Color[] playerColors,
			AudioClip pinOut, AudioClip pinIn) {
		this.boardImage = boardImage;
		this.x = x;
		this.y = y;
		this.playerColors = playerColors;
		this.pinOut = pinOut;
		this.pinIn = pinIn;
		setupLocations();
	}

	private void playSound(AudioClip a) {
		if (a != null)
			a.play();
	}

	/**
	 * Note: spot 0 on the board is at pin location 1. The spot behind spot 0 is
	 * at location -1. So spot 1 on the board is at pin location 2.
	 */
	public void setupLocations() {
		int leftP1X = 16;
		int leftP2X = 31;
		int rightP1X = 148;
		int rightP2X = 135;
		int middleP1X = 76;
		int middleP2X = 90;

		int leftStartY = 427;
		int rightStartY = 66;
		int middleStartY = 427;

		int straightSpacer = 9;
		int straightLongSpacer = 18;

		// THE STARTING POSITIONS
		pinLocations[0][0][0] = leftP1X;
		pinLocations[0][0][1] = 457;
		pinLocations[1][0][0] = leftP2X;
		pinLocations[1][0][1] = 457;
		pinLocations[0][1][0] = leftP1X;
		pinLocations[0][1][1] = 445;
		pinLocations[1][1][0] = leftP2X;
		pinLocations[1][1][1] = 445;

		// LEFT STRAIGHT SECTION
		int y = leftStartY;
		for (int i = 1; i < 35; i += 5) {
			for (int j = 0; j < 5; j++) {
				pinLocations[0][i + j + 1][0] = leftP1X;
				pinLocations[1][i + j + 1][0] = leftP2X;
				pinLocations[0][i + j + 1][1] = pinLocations[1][i + j + 1][1] = y;
				y -= straightSpacer;
			}
			y -= straightLongSpacer - straightSpacer;
		}
		// FIRST CURVE
		int[] p1X = { 18, 25, 40, 56, 76, 90, 108, 125, 141, 148 };
		int[] p1Y = { 47, 31, 19, 10, 6, 6, 10, 19, 33, 49 };
		int[] p2X = { 32, 40, 50, 62, 75, 90, 104, 117, 128, 134 };
		int[] p2Y = { 51, 38, 29, 22, 20, 20, 22, 29, 40, 52 };

		for (int i = 36; i < 46; i++) {
			pinLocations[0][i + 1][0] = p1X[i - 36];
			pinLocations[0][i + 1][1] = p1Y[i - 36];
			pinLocations[1][i + 1][0] = p2X[i - 36];
			pinLocations[1][i + 1][1] = p2Y[i - 36];
		}

		// RIGHT STRAIGHT SECTION
		y = rightStartY;
		for (int i = 46; i < 80; i += 5) {
			for (int j = 0; j < 5; j++) {
				pinLocations[0][i + j + 1][0] = rightP1X;
				pinLocations[1][i + j + 1][0] = rightP2X;
				pinLocations[0][i + j + 1][1] = pinLocations[1][i + j + 1][1] = y;
				y += straightSpacer;
			}
			y += straightLongSpacer - straightSpacer;
		}
		// SECOND CURVE
		int[] p1X2 = { 148, 138, 113, 86, 76 };
		int[] p1Y2 = { 443, 462, 473, 462, 443 };
		int[] p2X2 = { 134, 126, 113, 97, 90 };
		int[] p2Y2 = { 444, 455, 461, 455, 444 };

		for (int i = 81; i < 86; i++) {
			pinLocations[0][i + 1][0] = p1X2[i - 81];
			pinLocations[0][i + 1][1] = p1Y2[i - 81];
			pinLocations[1][i + 1][0] = p2X2[i - 81];
			pinLocations[1][i + 1][1] = p2Y2[i - 81];
		}
		// MIDDLE STRAIGHT SECTION
		y = middleStartY;
		for (int i = 86; i < 120; i += 5) {
			for (int j = 0; j < 5; j++) {
				pinLocations[0][i + j + 1][0] = middleP1X;
				pinLocations[1][i + j + 1][0] = middleP2X;
				pinLocations[0][i + j + 1][1] = pinLocations[1][i + j + 1][1] = y;
				y -= straightSpacer;
			}
			y -= straightLongSpacer - straightSpacer;
		}

		// FINAL LOCATION
		pinLocations[0][122][0] = 83;
		pinLocations[0][122][1] = 50;
		pinLocations[1][122][0] = 83;
		pinLocations[1][122][1] = 50;

	}

	public boolean clickedOn(int x, int y) {
		return (x > this.x & x < this.x + boardImage.getWidth(null) & y > this.y & y < this.y
				+ boardImage.getHeight(null));
	}

	public void add(int player, int pips, JPanel screen) {
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
		}
		if (pips > 0) {
			playSound(pinOut);
			highlightPlayer = player;
		}
		screen.repaint();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
		if (pips > 0) {
			score[player - 1] = Math.min(endScore, score[player - 1] + pips);
			pins[player - 1][1] = pins[player - 1][0];
			pins[player - 1][0] = score[player - 1];
			playSound(pinIn);
			highlightPlayer = -1;
		}
		screen.repaint();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
	}

	public int winner() {
		if (score[0] == endScore)
			return 1;
		if (score[1] == endScore)
			return 2;
		return 0;
	}
	
	public boolean skunk() {
		if (winner() !=0 & (score[0] <= 90 | score[1] <= 90))
			return true;
		return false;
	}

	public void reset() {
		score[0] = score[1] = 0;
		pins[0][0] = 0;
		pins[0][1] = -1;
		pins[1][0] = 0;
		pins[1][1] = -1;
	}

	private int highlightOffset = 2;

	public void draw(Graphics g) {
		g.drawImage(boardImage, x, y, null);
		int backPin = pins[0][0];
		int frontPin = pins[0][1];
		if (backPin > frontPin) {
			int temp = backPin;
			backPin = frontPin ;
			frontPin = temp;
		}
		g.setColor(playerColors[0]);
		if (highlightPlayer != 1)
			g.fillOval(x + pinLocations[0][backPin + 1][0], y
					+ pinLocations[0][backPin + 1][1], pinRadius, pinRadius);
		g.fillOval(x + pinLocations[0][frontPin + 1][0], y
				+ pinLocations[0][frontPin + 1][1], pinRadius, pinRadius);
		g.setFont(new Font("MonoSpaced", Font.BOLD, 16));
		String p1Message;
		if (score[0] < 10)
			p1Message = "  " + score[0];
		else if (score[0] < 100)
			p1Message = " " + score[0];
		else
			p1Message = "" + score[0];
		g.drawString("You", x + 42, y + 80);
		g.drawString(p1Message, x + 42, y + 98);
		backPin = pins[1][0];
		frontPin = pins[1][1];
		if (backPin > frontPin) {
			int temp = backPin;
			backPin = frontPin ;
			frontPin = temp;
		}
		g.setColor(playerColors[1]);
		if (highlightPlayer != 2)
			g.fillOval(x + pinLocations[1][backPin + 1][0], y
					+ pinLocations[1][backPin + 1][1], pinRadius, pinRadius);
		g.fillOval(x + pinLocations[1][frontPin + 1][0], y
				+ pinLocations[1][frontPin + 1][1], pinRadius, pinRadius);

		String p2Message;
		if (score[1] < 10)
			p2Message = "  " + score[1];
		else if (score[1] < 100)
			p2Message = " " + score[1];
		else
			p2Message = "" + score[1];
		g.drawString(" Me", x + 100, y + 80);
		g.drawString(p2Message, x + 100, y + 98);
	}
}

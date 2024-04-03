import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BoardTester extends Board {

	int xOffset, yOffset;

	public BoardTester(BufferedImage boardImage, int x, int y,
			Color[] playerColors) {
		super(boardImage, x, y, playerColors, null, null);
	}

	public void keyPressed(char k) {
		if (k == 'w')
			yOffset--;
		if (k == 's')
			yOffset++;
		if (k == 'a')
			xOffset--;
		if (k == 'd')
			xOffset++;
	}

	public void draw(Graphics g) {
		super.draw(g);
		g.setColor(Color.white);
		g.fillOval(x + xOffset, y + yOffset, 7, 7);
		g.setColor(Color.black);
		g.drawString("(" + xOffset + "," + yOffset + ")", x, y - 3);
	}
}

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JApplet;

public class BoardTesterApplet extends JApplet implements KeyListener {
	BoardTester board;

	public void init() {
		try {
			BufferedImage boardImage = ImageIO.read(new URL(getCodeBase(),
					"images/board2.jpg"));
			Color[] colors = { Color.green, Color.yellow };
			board = new BoardTester(boardImage, 20, 20, colors);
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		addKeyListener(this);
	}

	public void paint(Graphics g) {
		super.paint(g);
		board.draw(g);
	}

	public void keyPressed(KeyEvent arg0) {
		board.keyPressed(arg0.getKeyChar());
		repaint();
	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}

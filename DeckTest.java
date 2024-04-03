import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JApplet;


public class DeckTest extends JApplet implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Deck deck1, deck2;
	
	public void init()
	{
		BufferedImage cardImage = null;
		try {
			cardImage = ImageIO
					.read(new URL(this.getCodeBase(), "images/standard2.png"));
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		StandardCardLoader cl1 = new StandardCardLoader(cardImage, 79, 123);
		cl1.loadCards();
		deck1 = cl1.getDeck();
		deck2 = new Deck(deck1.getBack(), Color.blue);
		deck1.setX(10);
		deck1.setY(10);
		deck2.discardAll();
		deck2.flipOver();
		deck2.setX(100);
		deck2.setY(10);
		
		deck1.removeCardRank(Card.JOKER);
		deck1.shuffle();
		
		addMouseListener(this);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.BLUE);
		g.fillRect(0,0,getWidth(),getHeight());
		deck1.draw(g);
		deck2.draw(g);
	//	System.out.println(deck1.toString());
	//	System.out.println(deck2.toString());
	}

	public void mouseClicked(MouseEvent e) {
		if (deck1.clickedOn(e.getX(), e.getY())!=null)
		{
			if (deck1.isTopCardShowing())
			{
				deck2.addToTop(deck1.dealTopCard());
			}else
				deck1.exposeTopCard();
		}
		if (deck2.clickedOn(e.getX(), e.getY())!=null)
		{
			deck1.addToTop(deck2.dealTopCard());
		}
		repaint();
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}

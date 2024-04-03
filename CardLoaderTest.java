import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JPanel;

public class CardLoaderTest extends JApplet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	StandardCardLoader cl;

	public void init() {
		JPanel mainPanel = makeGUI();
		setContentPane(mainPanel);
		try {
			cl.loadCards();
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	} // init method

	public JPanel makeGUI() {
		BufferedImage cards = null;
		try {
			cards = ImageIO
					.read(new URL(this.getCodeBase(), "images/standard2.png"));
		} catch (IOException e) {
			System.err.println(e.toString());
		}
		cl = new StandardCardLoader(cards, 79, 123);
		return cl;
	}
}

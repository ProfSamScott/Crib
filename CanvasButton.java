import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class CanvasButton {

	Image image;
	int x, y;

	public CanvasButton(Image image, int x, int y) {
		this.image = image;
		this.x = x;
		this.y = y;
	}

	public boolean clickedOn(int x, int y) {
		if (x >= this.x & x < this.x + image.getWidth(null) & y >= this.y
				& y < this.y + image.getHeight(null))
			return true;
		return false;
	}

	public void draw(Graphics g) {
		g.drawImage(image, x, y, null);
	}
}

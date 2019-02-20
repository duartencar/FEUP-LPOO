package gui.Button;

import java.awt.Color;
import java.awt.Graphics;

public class DeleteSwimmerButton extends OvalButton {

	public DeleteSwimmerButton (int x, int y, int radius, boolean visibility) {
		super(x, y, radius);
	}

	public void paint(Graphics g) {
		g.setColor(Color.RED);

		g.fillOval(x-r/2, y-r/2, r, r);

		g.setColor(Color.WHITE);

		g.drawString("DELETE", x+18-r/2, y+35-r/2);

		g.drawString("Swimmer", x+15-r/2, y+55-r/2);
	}
}

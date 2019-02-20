package gui.Button;

import java.awt.Color;
import java.awt.Graphics;

public class AddSwimmerButton extends OvalButton {

	public AddSwimmerButton (int x, int y, int radius) {
		super(x, y, radius);
	}

	public void paint(Graphics g) {
		g.setColor(Color.BLUE);

		g.fillOval(x-r/2, y-r/2, r, r);

		g.setColor(Color.WHITE);

		g.drawString("ADD", x+30-r/2, y+35-r/2);

		g.drawString("Swimmer", x+15-r/2, y+55-r/2);
	}
}

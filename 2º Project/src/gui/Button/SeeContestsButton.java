package gui.Button;

import java.awt.Color;
import java.awt.Graphics;

public class SeeContestsButton extends OvalButton {

	public SeeContestsButton (int x, int y, int radius) {
		super(x, y, radius);
	}

	public void paint(Graphics g) {
		g.setColor(new Color(251, 197, 49));

		g.fillOval(x-r/2, y-r/2, r, r);

		g.setColor(Color.WHITE);

		g.drawString("SEE", x+30-r/2, y+35-r/2);

		g.drawString("Contests", x+15-r/2, y+55-r/2);
	}
}

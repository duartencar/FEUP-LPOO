package gui.Button;

import java.awt.Color;
import java.awt.Graphics;

public class AddContestButton extends OvalButton {

	public AddContestButton (int x, int y, int radius) {
		super(x, y, radius);
	}

	public void paint(Graphics g) {
		System.out.println("Painting");

		g.setColor(new Color(39, 174, 96));

		g.fillOval(x-r/2, y-r/2, r, r);

		g.setColor(Color.WHITE);

		g.drawString("ADD", x+30-r/2, y+35-r/2);

		g.drawString("Contest", x+20-r/2, y+55-r/2);
	}

}

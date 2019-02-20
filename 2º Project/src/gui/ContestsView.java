package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import engine.Contest.Contest;
import engine.Swimmer.Swimmer;

public class ContestsView extends JPanel {
	int length = 0;
	ArrayList<Swimmer> s;
	ContestPanel father = null;

	public ContestsView(ArrayList<Swimmer> swimmersContests) {
		s = swimmersContests;

		length = getNumberOfContest() * 50;

		setPreferredSize(new Dimension(100, length));
	}

	int getNumberOfContest() {
		int n = 0;

		for(Swimmer x : s) {
			n += x.getNumberOfContests();
		}

		return n;
	}

	public void setPanelFather(ContestPanel f) {
		father = f;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int y = 0;

		for(Swimmer sw : s) {

			if(sw.getNumberOfContests() > 0) {
				for(Contest c : sw.getAllContest()) {

					if(father.checkIfContestMustBeDisplayed(c, sw)) {
						c.paint(g, sw, y, 50);
						y += 50;
					}	
				}

				y += 50;
			}

			y -= 50;
		}
	}
}

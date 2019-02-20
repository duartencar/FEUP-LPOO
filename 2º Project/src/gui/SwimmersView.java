package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import engine.Swimmer.Swimmer;

public class SwimmersView extends JPanel implements MouseListener {

	int length = 0;
	ArrayList<Swimmer> s;
	SwimmersPanel father = null;
	int selectedSwimmer = -1;

	public SwimmersView(ArrayList<Swimmer> userSwimmers) {

		s = userSwimmers;
		length = userSwimmers.size() * 100;
		this.setPreferredSize(new Dimension(100, length));

		addMouseListener(this);
	}

	void setPanelFather(SwimmersPanel f) {
		father = f;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int y = 0;

		for(Swimmer s : s) {
			if(y/100 == selectedSwimmer) {
				s.setSelected(true);
			}
			else {
				s.setSelected(false);
			}
			s.paint(g, y, 100);

			y+=100;
		}
	}

	public int getSelectedSwimmer() {
		return selectedSwimmer;
	}

	public void setSelectedSwimmer(int selectedSwimmer) {
		this.selectedSwimmer = selectedSwimmer;
	}

	public boolean swimmerIsSelected() {
		return selectedSwimmer > -1;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		int y = e.getY();
		int x = e.getX();

		System.out.println(String.format("X=%d Y=%d", x, y));

		System.out.println("Selected option = " + Integer.valueOf(y / 100));

		if(y > length) {
			return;
		}
		else if(selectedSwimmer == Integer.valueOf(y / 100)) {
			selectedSwimmer = -1;
		}
		else {
			selectedSwimmer = Integer.valueOf(y / 100);
		}

		father.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}

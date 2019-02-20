package gui.Button;

import java.awt.Color;

public class TimmerButtonDesign extends OvalButton {
	protected String[] buttonStates = { "START", "PARTIAL", "END", "ADD" };

	protected Color[] buttonStateCollors = { Color.GRAY, new Color(39, 174, 96), new Color(218,182,0), Color.red };

	protected int[] stringAllignment = { 0, -10, 10, 10 };

	protected String text = buttonStates[0];

	protected int colorIndex = 0;

	protected int textIndex = 0;

	protected TimmerButtonDesign() {

	}

	public String getText() {
		return buttonStates[textIndex];
	}

	public Color getColor() {
		if(colorIndex >= buttonStateCollors.length) {
			colorIndex = 1;
		}

		return buttonStateCollors[colorIndex];
	}

	protected int getColorIndex() {
		return colorIndex;
	}

	protected void setColorIndex(int colorIndex) {
		this.colorIndex = colorIndex;
	}

	public int getStringAllignment() {
		return stringAllignment[textIndex];
	}
}

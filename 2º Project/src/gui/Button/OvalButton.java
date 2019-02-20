package gui.Button;

abstract class OvalButton {

	protected int x;
	protected int y;
	protected int r;

	public OvalButton () {
	}

	public OvalButton (int x, int y, int radius) {
		this.x = x;
		this.y = y;
		this.r = radius;
	}

	public boolean clickedOnMe(int y, int x) {

		int diffX = this.x - x;
		int diffY = y - this.y;

		if(Math.sqrt(diffX*diffX + diffY*diffY) <= r / 2) {
			return true;
		}

		return false;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}
}

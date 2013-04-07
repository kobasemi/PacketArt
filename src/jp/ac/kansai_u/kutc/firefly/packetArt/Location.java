package jp.ac.kansai_u.kutc.firefly.packetArt;

public class Location {
	int x = 0;
	int y = 0;

	public Location() {
	}

	public Location(int x, int y) {
		set(x, y);
	}

	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setX(int value) {
		x = value;
	}

	public void setY(int value) {
		y = value;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public boolean equals(Object comparison) {
		if (this == comparison)
			return true;
		if (comparison == null)
			return false;
		if (getClass() != comparison.getClass())
			return false;
		return getX() == ((Location) comparison).getX()
				&& getY() == ((Location) comparison).getY();
	}
	
	@Override
	public String toString(){
		return "[" + getX() + ", " + getY() + "]";
		
	}
}

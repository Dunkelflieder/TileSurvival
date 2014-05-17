package de.nerogar.game;

public class Position {

	private int x, y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Position addX(int x) {
		setX(getX() + x);
		return this;
	}

	public Position addY(int y) {
		setY(getY() + y);
		return this;
	}

	public Position subtract(Position p) {
		addX(-p.getX());
		addY(-p.getY());
		return this;
	}

	public Position subtracted(Position p) {
		return clone().subtract(p);
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		return x << 8 + y;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Position))
			return false;
		Position p = (Position) o;
		return (p.getX() == x && p.getY() == y);
	}

	@Override
	public String toString() {
		return "Position(" + getX() + ", " + getY() + ")";
	}

	@Override
	public Position clone() {
		return new Position(x, y);
	}

	public Vector toVector() {
		return new Vector(x, y);
	}

	public float getValue() {
		return (float) Math.sqrt(x * x + y * y);
	}
}

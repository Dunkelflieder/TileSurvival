package de.nerogar.game;

public class Vector {

	private float x;
	private float y;

	public Vector(float x, float y) {
		this.setX(x);
		this.setY(y);
	}

	public void addX(float x) {
		setX(getX() + x);
	}

	public void addY(float y) {
		setY(getY() + y);
	}

	public Vector add(Vector v) {
		addX(v.getX());
		addY(v.getY());
		return this;
	}

	public Vector subtract(Vector v) {
		addX(-v.getX());
		addY(-v.getY());
		return this;
	}

	public Vector multiply(float r) {
		setX(getX() * r);
		setY(getY() * r);
		return this;
	}

	public Vector added(Vector v) {
		return clone().add(v);
	}

	public Vector subtracted(Vector v) {
		return clone().subtract(v);
	}

	public Vector multiplied(float r) {
		return clone().multiply(r);
	}

	public Vector set(Vector v) {
		setX(v.getX());
		setY(v.getY());
		return this;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	@Override
	public Vector clone() {
		return new Vector(getX(), getY());
	}

}

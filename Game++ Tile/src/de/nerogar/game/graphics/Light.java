package de.nerogar.game.graphics;

import de.nerogar.game.Vector;

public class Light {
	public float size;
	public Vector pos;
	public float intensity;

	public Light(Vector pos, float size, float intensity) {
		this.size = size;
		this.pos = pos;
		this.intensity = intensity;
	}

	public boolean inArea(float offsX, float offsY, float width, float height) {
		if (pos.getX() + size < offsX) return false;
		if (pos.getX() - size > offsX + width) return false;
		if (pos.getY() + size < offsY) return false;
		if (pos.getY() - size > offsY + height) return false;
		return true;
	}
}

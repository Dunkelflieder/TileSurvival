package de.nerogar.game.graphics;

public class Light {
	public float size;
	public float posX;
	public float posY;

	public Light(float posX, float posY, float size) {
		this.size = size;
		this.posX = posX;
		this.posY = posY;
	}

	@Override
	public String toString() {
		return "light(vec2(" + posX + "," + posY + ")," + size + ")";
	}

	public boolean inArea(float offsX, float offsY, float width, float height) {
		if (posX + size < offsX) return false;
		if (posX - size > offsX + width) return false;
		if (posY + size < offsY) return false;
		if (posY - size > offsY + height) return false;
		return true;
	}
}

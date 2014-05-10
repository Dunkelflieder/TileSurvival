package de.nerogar.game.graphics;

public class Light {
	public float size;
	public float posX;
	public float posY;
	public float intensity;

	public Light(float posX, float posY, float size, float intensity) {
		this.size = size;
		this.posX = posX;
		this.posY = posY;
		this.intensity = intensity;
	}

	public boolean inArea(float offsX, float offsY, float width, float height) {
		if (posX + size < offsX) return false;
		if (posX - size > offsX + width) return false;
		if (posY + size < offsY) return false;
		if (posY - size > offsY + height) return false;
		return true;
	}
}

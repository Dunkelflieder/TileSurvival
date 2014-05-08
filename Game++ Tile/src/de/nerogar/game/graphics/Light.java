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
}

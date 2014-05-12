package de.nerogar.game.graphics.gui;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;

public abstract class GuiElement {

	private Vector pos;
	private Vector size;
	private int id = -1;

	public abstract void render();

	protected GuiElement(Vector pos, Vector size) {
		this.pos = pos;
		this.size = size;
	}

	public boolean isClicked(Vector at) {
		float ax = pos.getX() * Map.TILE_RENDER_SIZE;
		float ay = pos.getY() * Map.TILE_RENDER_SIZE;
		float bx = ax + size.getX() * Map.TILE_RENDER_SIZE;
		float by = ay + size.getY() * Map.TILE_RENDER_SIZE;
		return !(at.getX() < ax || at.getY() < ay || at.getX() > bx || at.getY() > by);
	}

	public Vector getPos() {
		return pos;
	}

	public void setPos(Vector pos) {
		this.pos = pos;
	}

	public Vector getSize() {
		return size;
	}

	public void setSize(Vector size) {
		this.size = size;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}

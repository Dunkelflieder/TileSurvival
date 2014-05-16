package de.nerogar.game.graphics.gui;

import de.nerogar.game.Vector;

public abstract class GuiElement {

	private Vector pos;
	private Vector size;
	private int id = -1;
	private boolean selectable;
	private boolean disabled = false;

	public abstract void render();

	protected GuiElement(Vector pos, Vector size, boolean selectable) {
		this.pos = pos;
		this.size = size;
		this.selectable = selectable;
	}

	public boolean hoveredBy(Vector at) {
		return !(at.getX() < pos.getX() || at.getY() < pos.getY() || at.getX() > pos.getX() + size.getX() || at.getY() > pos.getY() + size.getY());
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

	public boolean isSelectable() {
		return selectable;
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	public void click(int which) {
	}

	public void update() {
	}

	public boolean keyPressed(char c) {
		return false;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

}

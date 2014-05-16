package de.nerogar.game.graphics.gui;

import de.nerogar.game.Vector;

public class GEText extends GuiElement {

	private String text;
	private int alignment = FontRenderer.CENTERED;

	protected GEText(Vector pos, Vector size, String text) {
		super(pos, size, false);
		this.text = text;
	}

	@Override
	public void render() {
		FontRenderer.drawString(text, getPos().getX(), getPos().getY(), getSize().getX(), getSize().getY(), alignment);
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public int getAlignment() {
		return alignment;
	}

	public void setAlignment(int alignment) {
		this.alignment = alignment;
	}

}

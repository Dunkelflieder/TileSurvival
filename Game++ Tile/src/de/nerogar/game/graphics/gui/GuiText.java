package de.nerogar.game.graphics.gui;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;

public class GuiText extends GuiElement {

	private String text;

	protected GuiText(Vector pos, Vector size, String text) {
		super(pos, size);
		this.text = text;
	}

	@Override
	public void render() {
		float posX = getPos().getX() * Map.TILE_RENDER_SIZE;
		float posY = getPos().getY() * Map.TILE_RENDER_SIZE;
		float sizeX = getSize().getX() * Map.TILE_RENDER_SIZE;
		float sizeY = getSize().getY() * Map.TILE_RENDER_SIZE;
		FontRenderer.drawString(text, posX, posY, sizeX, sizeY, FontRenderer.CENTERED);
	}

}

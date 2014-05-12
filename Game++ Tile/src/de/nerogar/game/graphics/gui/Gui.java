package de.nerogar.game.graphics.gui;

import java.util.ArrayList;

import de.nerogar.game.Vector;

public abstract class Gui {

	private ArrayList<GuiElement> guiElements = new ArrayList<GuiElement>();

	protected void addGuiElements(GuiElement... elements) {
		for (GuiElement element : elements) {
			element.setId(guiElements.size());
			guiElements.add(element);
		}
	}

	public void render() {
		renderBackground();
		for (GuiElement guiElement : guiElements)
			guiElement.render();
	}

	public void clickAt(Vector at, int which) {
		for (int i = guiElements.size() - 1; i >= 0; i--) {
			if (guiElements.get(i).isClicked(at)) {
				click(i, which);
			}
		}
	}

	public abstract void click(int id, int which);

	public abstract void renderBackground();

	public abstract void update();

}
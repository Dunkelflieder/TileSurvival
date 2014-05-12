package de.nerogar.game.graphics.gui;

import de.nerogar.game.Vector;

public class GuiTitle extends Gui {

	private GuiText text1, text2, text3;

	public GuiTitle() {

		text1 = new GuiText(new Vector(0, 3), new Vector(20, 1), "Startbildschirm");
		text2 = new GuiText(new Vector(0, 5f), new Vector(20, 0.5f), "H für Host");
		text3 = new GuiText(new Vector(0, 5.7f), new Vector(20, 0.5f), "C für Client");
		addGuiElements(text1, text2, text3);

	}

	@Override
	public void renderBackground() {
		// no background
	}

	@Override
	public void click(int id, int which) {
		// nothing
	}

	@Override
	public void update() {
		// nothing
	}

}

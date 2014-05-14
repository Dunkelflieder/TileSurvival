package de.nerogar.game.graphics.gui;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import de.nerogar.game.InputHandler;
import de.nerogar.game.Vector;

public abstract class Gui {

	private ArrayList<GuiElement> guiElements = new ArrayList<GuiElement>();
	private boolean interceptsInput;
	private GuiElement selectedElement = null;

	public Gui(boolean interceptsInput) {
		this.setInterceptsInput(interceptsInput);
	}

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

	public void click(int id, int which) {
	};

	public void keyPressed(char key) {
	};

	public void renderBackground() {
	};

	public void update() {
		updateInput();
		for (int i = 0; i < guiElements.size(); i++) {
			guiElements.get(i).update();
		}
	};

	public void updateInput() {
		Vector mousePos = new Vector(Mouse.getX(), Display.getHeight() - Mouse.getY());
		for (int i = guiElements.size() - 1; i >= 0; i--) {
			GuiElement element = guiElements.get(i);
			if (element.hoveredBy(mousePos)) {
				if (InputHandler.isMouseButtonPressed(0)) {
					click(i, 0);
					element.click(0);
				}
				if (InputHandler.isMouseButtonPressed(1)) {
					click(i, 1);
					element.click(1);
				}
				if (InputHandler.isMouseButtonPressed(2)) {
					click(i, 2);
					element.click(2);
				}
				break;
			}
		}
		char key = InputHandler.getPressedKey();
		if (key > 0)
			keyPressed(key);
	}

	public boolean interceptsInput() {
		return interceptsInput;
	}

	public void setInterceptsInput(boolean interceptsInput) {
		this.interceptsInput = interceptsInput;
	}

	public GuiElement getSelectedElement() {
		return selectedElement;
	}

	public void selectElement(GuiElement selectedElement) {
		this.selectedElement = selectedElement;
	}

}
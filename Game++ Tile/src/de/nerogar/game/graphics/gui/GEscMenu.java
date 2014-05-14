package de.nerogar.game.graphics.gui;

import org.lwjgl.opengl.GL11;

import de.nerogar.game.*;

public class GEscMenu extends Gui {

	private GEButton buttonResume, buttonSettings, buttonClose;

	public GEscMenu() {
		super(true);

		float posX = (Game.game.WIDTH - Map.TILE_RENDER_SIZE * 4f) * 0.5f;
		float posY = (Game.game.HEIGHT - Map.TILE_RENDER_SIZE) * 0.5f;

		buttonResume = new GEButton(new Vector(posX, posY - 150), new Vector(Map.TILE_RENDER_SIZE * 4, Map.TILE_RENDER_SIZE), "Resume");
		buttonSettings = new GEButton(new Vector(posX, posY - 50), new Vector(Map.TILE_RENDER_SIZE * 4, Map.TILE_RENDER_SIZE), "Settings");
		buttonClose = new GEButton(new Vector(posX, posY + 50), new Vector(Map.TILE_RENDER_SIZE * 4, Map.TILE_RENDER_SIZE), "Close game");

		addGuiElements(buttonResume, buttonSettings, buttonClose);

	}

	@Override
	public void click(int id, int which) {
		if (id == buttonResume.getId()) {
			GuiBank.selectGui(GuiBank.GUI_INGAME);
		} else if (id == buttonSettings.getId()) {
			GuiBank.selectGui(GuiBank.GUI_SETTINGS);
		} else if (id == buttonClose.getId()) {
			// TODO end game
		}
	}

	@Override
	public void renderBackground() {
		RenderHelper.enableAlphaMask();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(0f, 0f, 0f, 0.6f);
		RenderHelper.renderQuad(0, 0, Game.game.WIDTH, Game.game.HEIGHT);
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	@Override
	public void keyPressed(char key) {
		if (key == 27) {
			GuiBank.selectGui(GuiBank.GUI_INGAME);
		}
	}

}

package de.nerogar.game.graphics.gui;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;
import de.nerogar.game.Game;
import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.graphics.TextureBank;

public class GTitle extends Gui {

	private GEButton buttonHost, buttonClient, buttonSettings, buttonCredits;

	public GTitle() {
		super(true);

		float posX = (Game.game.WIDTH - Map.TILE_RENDER_SIZE * 4) * 0.5f;
		float posY = Game.game.HEIGHT / 3f;

		buttonHost = new GEButton(new Vector(posX, posY), new Vector(Map.TILE_RENDER_SIZE * 4, Map.TILE_RENDER_SIZE), "Host a game");
		buttonClient = new GEButton(new Vector(posX, posY + 80), new Vector(Map.TILE_RENDER_SIZE * 4, Map.TILE_RENDER_SIZE), "Join a game");
		buttonSettings = new GEButton(new Vector(posX, posY + 160), new Vector(Map.TILE_RENDER_SIZE * 4, Map.TILE_RENDER_SIZE), "Settings");
		buttonCredits = new GEButton(new Vector(posX, posY + 240), new Vector(Map.TILE_RENDER_SIZE * 4, Map.TILE_RENDER_SIZE), "Credits");

		addGuiElements(buttonHost, buttonClient, buttonSettings, buttonCredits);

	}

	@Override
	public void renderBackground() {
		TextureBank.instance.bindTexture("title.png");

		glBegin(GL_QUADS);

		glTexCoord2f(0, 0);
		glVertex3f(0, 0, -1f);

		glTexCoord2f(1, 0);
		glVertex3f(Game.game.WIDTH, 0, -1f);

		glTexCoord2f(1, 1);
		glVertex3f(Game.game.WIDTH, Game.game.HEIGHT, -1f);

		glTexCoord2f(0, 1);
		glVertex3f(0, Game.game.HEIGHT, -1f);

		glEnd();
	}

	@Override
	public void click(int id, int which) {
		if (id == buttonHost.getId()) {
			GuiBank.selectGui(GuiBank.CLASS_SELECTION);
		} else if (id == buttonClient.getId()) {
			GuiBank.selectGui(GuiBank.LOBBY_CLIENT);
		} else if (id == buttonSettings.getId()) {
			GuiBank.selectGui(GuiBank.SETTINGS);
		} else if(id == buttonCredits.getId()) {
			GuiBank.selectGui(GuiBank.CREDITS);
		}
	}

	@Override
	public void keyPressed(char key) {
		if (key == 'h') {
			GuiBank.selectGui(GuiBank.CLASS_SELECTION);
		} else if (key == 'c') {
			GuiBank.selectGui(GuiBank.LOBBY_CLIENT);
		}
	}

}

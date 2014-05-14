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
import de.nerogar.game.network.Client;
import de.nerogar.game.network.Server;

public class GTitle extends Gui {

	private GEText textTitle;
	private GEButton buttonHost, buttonClient, buttonSettings;

	public GTitle() {
		super(true);

		float posX = (Game.game.WIDTH - Map.TILE_RENDER_SIZE * 4) * 0.5f;
		float posY = Game.game.HEIGHT / 3f;

		textTitle = new GEText(new Vector(0, 2), new Vector(20, 1), "Startbildschirm");

		buttonHost = new GEButton(new Vector(posX, posY), new Vector(Map.TILE_RENDER_SIZE * 4, Map.TILE_RENDER_SIZE), "Host a game");
		buttonClient = new GEButton(new Vector(posX, posY + 100), new Vector(Map.TILE_RENDER_SIZE * 4, Map.TILE_RENDER_SIZE), "Join a game");
		buttonSettings = new GEButton(new Vector(posX, posY + 200), new Vector(Map.TILE_RENDER_SIZE * 4, Map.TILE_RENDER_SIZE), "Settings");

		addGuiElements(textTitle, buttonHost, buttonClient, buttonSettings);

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
			Game.game.server = new Server(Game.port);
			GuiBank.selectGui(GuiBank.GUI_LOBBY_HOST);
		} else if (id == buttonClient.getId()) {
			//Game.game.client = new Client(Game.host, Game.port);
			GuiBank.selectGui(GuiBank.GUI_LOBBY_CLIENT);
		} else if (id == buttonSettings.getId()) {
			GuiBank.selectGui(GuiBank.GUI_SETTINGS);
		}
	}

	@Override
	public void keyPressed(char key) {
		if (key == 'h') {
			Game.game.server = new Server(Game.port);
			GuiBank.selectGui(GuiBank.GUI_LOBBY_HOST);
		} else if (key == 'c') {
			//Game.game.client = new Client(Game.host, Game.port);
			GuiBank.selectGui(GuiBank.GUI_LOBBY_CLIENT);
		}
	}

}

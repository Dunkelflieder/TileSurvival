package de.nerogar.game.graphics.gui;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;
import de.nerogar.game.*;
import de.nerogar.game.graphics.TextureBank;
import de.nerogar.game.network.Client;

public class GLobbyClient extends Gui {

	private GEInput inputIP;
	private GEText text;
	private GEButton buttonOK, buttonBack;
	private boolean tryConnection = false;
	private String host;
	private int port;

	public GLobbyClient() {
		super(true);

		float posX = (Game.game.WIDTH - Map.TILE_RENDER_SIZE * 4f) * 0.5f;
		float posY = Game.game.HEIGHT / 2f;

		text = new GEText(new Vector(0, posY - 150), new Vector(Game.game.WIDTH, 32f), "");
		inputIP = new GEInput(new Vector(posX, posY - 100), new Vector(Map.TILE_RENDER_SIZE * 4f, Map.TILE_RENDER_SIZE));
		buttonOK = new GEButton(new Vector(posX, posY), new Vector(Map.TILE_RENDER_SIZE * 4f, Map.TILE_RENDER_SIZE), "Connect");
		buttonBack = new GEButton(new Vector(posX, posY + 100), new Vector(Map.TILE_RENDER_SIZE * 4f, Map.TILE_RENDER_SIZE), "back");

		addGuiElements(text, inputIP, buttonOK, buttonBack);
	}

	@Override
	public void select() {
		text.setText("Enter the IP:port of the host");
	}

	@Override
	public void click(int id, int which) {
		if (id == buttonOK.getId()) {
			String input = inputIP.getText();
			String[] args = input.split(":");
			if (args.length != 2) {
				text.setText("Your input is invalid");
				return;
			}

			//Game.game.client = new Client(Game.host, Game.port);
			String host = args[0];
			int port = -1;
			try {
				port = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				text.setText("Your port is invalid");
				return;
			}
			text.setText("Connecting...");
			this.host = host;
			this.port = port;
			tryConnection = true;
		} else if (id == buttonBack.getId()) {
			GuiBank.selectGui(GuiBank.TITLE);
		}
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
	public void update() {
		if (tryConnection) {
			tryConnection = false;
			Client client = new Client(host, port);
			if (client.connected) {
				GuiBank.selectGui(GuiBank.CLASS_SELECTION);
				Game.game.client = client;
			} else {
				text.setText("No connection possible");
				client.stopClient();
			}
		}
		super.update();
	}
}

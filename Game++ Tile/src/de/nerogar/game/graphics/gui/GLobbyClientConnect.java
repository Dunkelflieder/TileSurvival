package de.nerogar.game.graphics.gui;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.util.ArrayList;

import de.nerogar.game.*;
import de.nerogar.game.graphics.TextureBank;
import de.nerogar.game.network.*;

public class GLobbyClientConnect extends Gui {

	private GEText text;
	private GEButton buttonCancel;
	private boolean failed = false;
	public String host;
	public int port;
	public boolean selectBuffer = false;

	public GLobbyClientConnect() {
		super(true);

		float posX = (Game.game.WIDTH - Map.TILE_RENDER_SIZE * 4f) * 0.5f;
		float posY = Game.game.HEIGHT / 2f;

		text = new GEText(new Vector(0, posY - 100), new Vector(Game.game.WIDTH, Map.TILE_RENDER_SIZE / 2f), "");
		buttonCancel = new GEButton(new Vector(posX, posY), new Vector(Map.TILE_RENDER_SIZE * 4f, Map.TILE_RENDER_SIZE), "cancel");

		addGuiElements(text, buttonCancel);
	}

	@Override
	public void select() {
		text.setText("connecting...");
		selectBuffer = true;
	}

	@Override
	public void update() {
		super.update();
		if (selectBuffer) {
			selectBuffer = false;
			Game.game.client = new Client(host, port);
			text.setText("Connected! Waiting for the host to start...");
		}
		if (!Game.game.client.connected) {
			Game.game.client.stopClient();
			text.setText("No connection to given host");
			failed = true;
		}
		if (failed)
			return;
		ArrayList<Packet> packets = null;
		try {
			packets = Game.game.client.getData(Packet.LOBBY_CHANNEL);
		} catch (Exception e) {
			System.out.println("Connection problem");
			e.printStackTrace();
			failed = true;
			text.setText("Could not connect.");
		}
		if (packets != null) {
			for (Packet packet : packets) {
				if (packet instanceof PacketStartGame) {
					PacketStartGame startgamepacket = (PacketStartGame) packet;
					Map map = MapLoader.loadMap(Map.CLIENT_WORLD, "map.png");
					//Game.game.client = client;

					map.initPlayer(startgamepacket.playerID);
					Game.game.map = map;

					GuiBank.selectGui(GuiBank.GUI_INGAME);
				}
			}
		}

	}

	@Override
	public void click(int id, int which) {
		if (id == buttonCancel.getId()) {
			Game.game.client.stopClient();
			GuiBank.selectGui(GuiBank.GUI_LOBBY_CLIENT);
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
}

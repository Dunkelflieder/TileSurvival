package de.nerogar.game.graphics.gui;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.util.ArrayList;
import java.util.HashMap;

import de.nerogar.game.*;
import de.nerogar.game.entity.playerClass.PlayerClass;
import de.nerogar.game.graphics.TextureBank;
import de.nerogar.game.network.*;

public class GLobbyClientConnect extends Gui {

	private GEText text;
	private GEButton buttonCancel;
	private boolean failed = false;
	
	private HashMap<Integer, Integer> selectedClasses;

	public GLobbyClientConnect() {
		super(true);

		float posX = (Game.game.WIDTH - Map.TILE_RENDER_SIZE * 4f) * 0.5f;
		float posY = Game.game.HEIGHT / 2f;

		text = new GEText(new Vector(0, posY - 100), new Vector(Game.game.WIDTH, Map.TILE_RENDER_SIZE / 2f), "");
		buttonCancel = new GEButton(new Vector(posX, posY), new Vector(Map.TILE_RENDER_SIZE * 4f, Map.TILE_RENDER_SIZE), "disconnect");

		addGuiElements(text, buttonCancel);
	}

	@Override
	public void select() {
		text.setText("Waiting for host to start game");
		selectedClasses = new HashMap<Integer, Integer>();
	}

	@Override
	public void update() {
		super.update();
		if (failed) return;
		if (!Game.game.client.connected) {
			System.out.println("Connection problem");
			failed = true;
			text.setText("Connection Lost.");
		}
		ArrayList<Packet> packets = null;
		packets = Game.game.client.getData(Packet.LOBBY_CHANNEL);
		if (packets != null) {
			if (packets.size() > 0) System.out.println(packets.size() + " packets");
			for (Packet packet : packets) {
				if (packet instanceof PacketStartGame) {
					PacketStartGame startgamepacket = (PacketStartGame) packet;
					Map map = MapLoader.loadMap(Map.CLIENT_WORLD, "map.png");
					//Game.game.client = client;

					//EntityPlayer player = (EntityPlayer) map.getEntities().get(startgamepacket.playerID);//startgamepacket.playerClass
					//player.playerClass = PlayerClass.getInstanceByID(startgamepacket.playerClass, player);
					map.initPlayer(startgamepacket.playerID);
					map.getPlayer().setPlayerClass(PlayerClass.getInstanceByID(PlayerClass.ENGINEER, map.getPlayer())); // default

					Game.game.map = map;

					GuiBank.selectGui(GuiBank.INGAME);
				} else if (packet instanceof PacketSelectPlayerClass) {
					PacketSelectPlayerClass packetSelect = (PacketSelectPlayerClass) packet;
					selectedClasses.put(packetSelect.playerID, packetSelect.playerClass);
				}
			}
		}

	}
	
	public int getPlayerClass(int id) {
		return selectedClasses.get(id);
	}

	@Override
	public void click(int id, int which) {
		if (id == buttonCancel.getId()) {
			Game.game.client.stopClient();
			GuiBank.selectGui(GuiBank.LOBBY_CLIENT);
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

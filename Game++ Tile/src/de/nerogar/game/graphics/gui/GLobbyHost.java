package de.nerogar.game.graphics.gui;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.util.ArrayList;

import de.nerogar.game.*;
import de.nerogar.game.entity.EntityPlayer;
import de.nerogar.game.graphics.TextureBank;
import de.nerogar.game.network.*;

public class GLobbyHost extends Gui {

	private GEButton buttonStart, buttonCancel;
	private GEText text, textPlayers, info1, info2, info3;

	public GLobbyHost() {
		super(true);

		float posX = (Game.game.WIDTH - Map.TILE_RENDER_SIZE * 4f) * 0.5f;
		float posY = (Game.game.HEIGHT - Map.TILE_RENDER_SIZE) * 0.5f;

		text = new GEText(new Vector(0, posY - 100), new Vector(Game.game.WIDTH, 32), "Clients can now connect");
		textPlayers = new GEText(new Vector(0, posY - 50), new Vector(Game.game.WIDTH, 32), "0 players connected");

		buttonStart = new GEButton(new Vector(posX + (Map.TILE_RENDER_SIZE * 2f + 16f), posY), new Vector(Map.TILE_RENDER_SIZE * 4f, Map.TILE_RENDER_SIZE), "start");
		buttonCancel = new GEButton(new Vector(posX - (Map.TILE_RENDER_SIZE * 2f + 16f), posY), new Vector(Map.TILE_RENDER_SIZE * 4f, Map.TILE_RENDER_SIZE), "cancel");

		info1 = new GEText(new Vector(0, posY + 100), new Vector(Game.game.WIDTH, 32), "");
		info2 = new GEText(new Vector(0, posY + 150), new Vector(Game.game.WIDTH, 32), "");
		info3 = new GEText(new Vector(0, posY + 200), new Vector(Game.game.WIDTH, 32), "(host must forward port in router)");

		addGuiElements(buttonStart, buttonCancel, text, textPlayers, info1, info2, info3);

	}

	@Override
	public void update() {
		super.update();
		textPlayers.setText(Game.game.server.getClients().size() + " players connected");
	}

	@Override
	public void select() {
		Server server = new Server(Game.port);
		Game.game.server = server;
		info1.setText("via LAN: " + server.getIP() + ":" + server.port);
		info2.setText("via Internet: yourIP:" + server.port);
	}

	@Override
	public void click(int id, int which) {
		if (id == buttonStart.getId()) {
			Game.game.server.stopAcceptingClients();

			Map map = MapLoader.loadMap(Map.SERVER_WORLD, "map.png");
			//Game.game.server = server;

			EntityPlayer playerEntity = new EntityPlayer(map, map.getSpawnLocation());
			map.spawnEntity(playerEntity);
			map.initPlayer(playerEntity.id);

			ArrayList<Client> clients = Game.game.server.getClients();
			for (Client client : clients) {
				EntityPlayer playerEntityClient = new EntityPlayer(map, map.getSpawnLocation());
				PacketStartGame gameStartPacket = new PacketStartGame();
				gameStartPacket.playerID = playerEntityClient.id;
				map.spawnEntity(playerEntityClient);
				client.sendPacket(gameStartPacket);
			}

			Game.game.map = map;

			GuiBank.selectGui(GuiBank.GUI_INGAME);
		} else if (id == buttonCancel.getId()) {
			Game.game.server.stopServer();
			Game.game.closeMap();
			GuiBank.selectGui(GuiBank.GUI_TITLE);
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

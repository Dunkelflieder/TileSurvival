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

	private GEButton buttonStart;
	private GEText text;

	public GLobbyHost() {
		super(true);

		float posX = (Game.game.WIDTH - Map.TILE_RENDER_SIZE * 4f) * 0.5f;
		float posY = (Game.game.HEIGHT - Map.TILE_RENDER_SIZE) * 0.5f;

		text = new GEText(new Vector(0, posY), new Vector(Game.game.WIDTH, 32), "Clients can now connect");
		buttonStart = new GEButton(new Vector(posX, posY + 100), new Vector(Map.TILE_RENDER_SIZE * 4f, Map.TILE_RENDER_SIZE), "start");

		addGuiElements(buttonStart, text);

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

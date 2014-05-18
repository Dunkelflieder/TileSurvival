package de.nerogar.game.graphics.gui;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.util.ArrayList;
import java.util.HashMap;

import de.nerogar.game.*;
import de.nerogar.game.entity.EntityPlayer;
import de.nerogar.game.entity.playerClass.PlayerClass;
import de.nerogar.game.graphics.TextureBank;
import de.nerogar.game.network.*;

public class GLobbyHost extends Gui {

	private GEButton buttonStart, buttonCancel;
	private GEText text, textPlayers, info1, info2, info3;
	private HashMap<Client, Integer> playerClassSelection = new HashMap<Client, Integer>();

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

		HashMap<Client, Integer> playerClassSelection2 = new HashMap<Client, Integer>();
		ArrayList<Client> clients = Game.game.server.getClients();
		for (Client client : clients) {
			playerClassSelection2.put(client, playerClassSelection.get(client));
			ArrayList<Packet> packets = null;
			packets = client.getData(Packet.LOBBY_CHANNEL);
			if (packets != null) {
				for (Packet packet : packets) {
					if (packet instanceof PacketSelectPlayerClass) {
						int pClass = ((PacketSelectPlayerClass) packet).playerClass;
						if (pClass >= 0) {
							playerClassSelection2.put(client, pClass);
						}
					}
				}
			}
		}
		playerClassSelection = playerClassSelection2;

		int chosen = 0;
		for (Integer i : playerClassSelection.values())
			if (i != null) chosen++;

		textPlayers.setText(chosen + "/" + clients.size() + " players chose their class");
		if (clients.size() == chosen) {
			buttonStart.setDisabled(false);
		} else {
			buttonStart.setDisabled(true);
		}
	}

	@Override
	public void select() {
		buttonStart.setDisabled(true);

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
			playerEntity.setPlayerClass(PlayerClass.getInstanceByID(GuiBank.CLASS_SELECTION.getPlayerClass(), playerEntity));
			playerEntity.pClass = GuiBank.CLASS_SELECTION.getPlayerClass();
			map.spawnEntity(playerEntity);
			map.initPlayer(playerEntity.id);

			// broadcast own player class
			PacketSelectPlayerClass selectPacket = new PacketSelectPlayerClass();
			selectPacket.playerID = playerEntity.id;
			selectPacket.playerClass = GuiBank.CLASS_SELECTION.getPlayerClass();
			Game.game.server.broadcastData(selectPacket);
			
			ArrayList<Client> clients = Game.game.server.getClients();
			for (int i = 0; i < clients.size(); i++) {
				Client client = clients.get(i);
				EntityPlayer playerEntityClient = new EntityPlayer(map, map.getSpawnLocation());
				int pClass = playerClassSelection.get(client);
				playerEntityClient.pClass = pClass;
				playerEntityClient.setPlayerClass(PlayerClass.getInstanceByID(pClass, playerEntityClient));

				System.out.println("Sending client PacketStartGame with Entity id " + playerEntityClient.id);
				
				PacketStartGame gameStartPacket = new PacketStartGame();
				gameStartPacket.playerID = playerEntityClient.id;
				client.sendPacket(gameStartPacket);
				
				selectPacket = new PacketSelectPlayerClass();
				selectPacket.playerID = playerEntityClient.id;
				selectPacket.playerClass = pClass;
				Game.game.server.broadcastData(selectPacket);

				map.spawnEntity(playerEntityClient);
				System.out.println("Spawned player id " + playerEntityClient.id);
			}


			Game.game.map = map;

			GuiBank.selectGui(GuiBank.INGAME);
		} else if (id == buttonCancel.getId()) {
			Game.game.server.stopServer();
			Game.game.closeMap();
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

}

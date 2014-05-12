package de.nerogar.game.graphics.gui;

import java.util.ArrayList;

import de.nerogar.game.*;
import de.nerogar.game.entity.EntityPlayer;
import de.nerogar.game.network.*;

public class GuiLobbyHost extends Gui {

	private GuiButton buttonStart;

	public GuiLobbyHost() {

		buttonStart = new GuiButton(new Vector(0.5f, 0.5f), new Vector(4, 1), "start");

		addGuiElements(buttonStart);

	}

	@Override
	public void renderBackground() {
		// no background
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
	public void update() {

	}

}

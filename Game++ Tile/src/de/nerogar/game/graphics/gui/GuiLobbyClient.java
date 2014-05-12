package de.nerogar.game.graphics.gui;

import java.util.ArrayList;

import de.nerogar.game.*;
import de.nerogar.game.network.*;

public class GuiLobbyClient extends Gui {

	private Client client;

	public GuiLobbyClient() {
		
	}

	@Override
	public void renderBackground() {
		// no background
	}

	@Override
	public void click(int id, int which) {

	}

	@Override
	public void update() {
		ArrayList<Packet> packets = client.getData(Packet.LOBBY_CHANNEL);
		if (packets != null) {
			for (Packet packet : packets) {
				if (packet instanceof PacketStartGame) {
					PacketStartGame startgamepacket = (PacketStartGame) packet;
					Map map = MapLoader.loadMap(Map.CLIENT_WORLD, "map.png");
					map.client = client;

					map.initPlayer(startgamepacket.playerID);
					Game.game.map = map;

					GuiBank.selectGui(GuiBank.GUI_INGAME);
				}
			}
		}

	}

}

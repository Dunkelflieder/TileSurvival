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

public class GLobbyClient extends Gui {


	public GLobbyClient() {
		super(true);
	}

	@Override
	public void update() {
		super.update();
		ArrayList<Packet> packets = Game.game.client.getData(Packet.LOBBY_CHANNEL);
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

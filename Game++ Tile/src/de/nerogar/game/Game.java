package de.nerogar.game;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;

import de.nerogar.game.graphics.gui.*;
import de.nerogar.game.network.Client;
import de.nerogar.game.network.Server;
import de.nerogar.game.sound.Sound;
import de.nerogar.game.sound.SoundCategory;
import de.nerogar.game.sound.SoundManager;

public class Game {
	public static String version = "1.0";
	public static String username = "nerogar";
	public static int port = 34543;
	public static String host = "localhost";
	public static Game game;

	private RenderEngine renderEngine;
	public Map map;

	public final int WIDTH = 1280;
	public final int HEIGHT = 720;
	public final int FRAMERATE = 60;

	//server
	public Server server;
	public Client client;

	private Sound bgMusic = new Sound(SoundCategory.MUSIC, "music1.ogg");

	public Game() {
		renderEngine = new RenderEngine();
		renderEngine.init(WIDTH, HEIGHT);
		bgMusic.setLooping(true);
		bgMusic.play();
		SoundCategory.MUSIC.setGain(0.3f);
		SoundCategory.EFFECT.setGain(0.5f);

	}

	public void run() {
		while (!Display.isCloseRequested()) {
			update();
			render();
			Display.sync(FRAMERATE);
			Display.update();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		}
		SoundManager.shutdown();
		cleanup();
	}

	private void update() {
		SoundManager.update();
		InputHandler.update(this);
		GuiBank.update();

		if (map != null && map.ready) {
			map.update(1f / FRAMERATE);
			float listenerX = (float) map.getOffsX() + 0.5f * WIDTH / Map.TILE_RENDER_SIZE;
			float listenerY = (float) map.getOffsY() + 0.5f * HEIGHT / Map.TILE_RENDER_SIZE;
			SoundManager.recalculateListener(new Vector(listenerX, listenerY));
		}

	}

	public void closeMap() {

		map = null;
	}

	private void render() {
		if (map != null && map.ready) {
			map.render();
		}
		GuiBank.render();
	}

	public void cleanup() {
		if (server != null) server.stopServer();
		if (client != null) client.stopClient();
	}

	public static void main(String[] args) {
		if (args.length > 0) {
			Game.host = args[0];
		}

		game = new Game();
		game.run();
	}

}

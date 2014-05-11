package de.nerogar.game;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;

import de.nerogar.game.graphics.GuiIngame;
import de.nerogar.game.sound.SoundManager;

public class Game {
	private RenderEngine renderEngine;
	private Map map;

	final int WIDTH = 1280;
	final int HEIGHT = 720;
	final int FRAMERATE = 60;

	private GuiIngame guiIngame;

	public Game() {
		renderEngine = new RenderEngine();
		renderEngine.init(WIDTH, HEIGHT);
		map = MapLoader.loadMap("map.png");
		guiIngame = new GuiIngame(map.getPlayer());
	}

	public void run() {
		while (!Display.isCloseRequested()) {
			//long time1 = System.nanoTime();
			update();
			render();
			Display.sync(FRAMERATE);
			Display.update();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			//long time2 = System.nanoTime();
			//System.out.println("time: " + ((time2 - time1) / 1000000d));
		}
		SoundManager.shutdown();
	}

	private void update() {
		map.update(1f / FRAMERATE);
		InputHandler.update(this);
	}

	private void render() {
		map.render();
		guiIngame.render();
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.run();
	}

}

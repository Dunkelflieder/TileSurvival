package de.nerogar.game;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;

import de.nerogar.game.sound.Sound;
import de.nerogar.game.sound.SoundManager;

public class Game {
	private RenderEngine renderEngine;
	private Map map;

	final int WIDTH = 1280;
	final int HEIGHT = 720;
	final int FRAMERATE = 60;
	
	private Sound bgMusic = SoundManager.create("licrea.wav", new Vector(0, 0));

	public Game() {
		renderEngine = new RenderEngine();
		renderEngine.init(WIDTH, HEIGHT);
		map = MapLoader.loadMap("map.png");
		bgMusic.setGain(0.1f);
		bgMusic.setLooping(true);
		bgMusic.play();
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
	}

	private void update() {
		map.update(1f / FRAMERATE);
	}

	private void render() {
		map.render();
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.run();
	}

}

package de.nerogar.game.graphics.gui;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.util.Random;

import de.nerogar.game.*;
import de.nerogar.game.graphics.TextureBank;
import de.nerogar.game.sound.SoundCategory;

public class GSettings extends Gui {

	private GEButton buttonOk;
	private GESlider sliderMusic, sliderEffects;
	private GEText textMusic1, textMusic2, textEffects1, textEffects2;

	public GSettings() {
		super(true);

		//float width = 4f;
		//System.out.println((Map.TILE_RENDER_SIZE / Map.TILE_PIXEL_COUNT));
		//float posX = ((Game.game.WIDTH - (width / 2f) * (Map.TILE_RENDER_SIZE / Map.TILE_PIXEL_COUNT) * Map.TILE_RENDER_SIZE) / 2) / (Map.TILE_RENDER_SIZE);

		float posX = (Game.game.WIDTH - Map.TILE_RENDER_SIZE * 4f) * 0.5f;

		sliderMusic = new GESlider(new Vector(posX, 200), new Vector(Map.TILE_RENDER_SIZE * 4, Map.TILE_RENDER_SIZE), "a", 0, 1, SoundCategory.MUSIC.getGain());
		sliderEffects = new GESlider(new Vector(posX, 300), new Vector(Map.TILE_RENDER_SIZE * 4, Map.TILE_RENDER_SIZE), "a", 0, 1, SoundCategory.EFFECT.getGain());

		textMusic1 = new GEText(new Vector(posX - Map.TILE_RENDER_SIZE * 4, 200), new Vector(Map.TILE_RENDER_SIZE * 2, Map.TILE_RENDER_SIZE), "Music");
		textEffects1 = new GEText(new Vector(posX - Map.TILE_RENDER_SIZE * 4, 300), new Vector(Map.TILE_RENDER_SIZE * 2, Map.TILE_RENDER_SIZE), "Effects");

		textMusic2 = new GEText(new Vector(posX + Map.TILE_RENDER_SIZE * 4, 200), new Vector(Map.TILE_RENDER_SIZE * 2, Map.TILE_RENDER_SIZE), sliderMusic.getText());
		textEffects2 = new GEText(new Vector(posX + Map.TILE_RENDER_SIZE * 4, 300), new Vector(Map.TILE_RENDER_SIZE * 2, Map.TILE_RENDER_SIZE), sliderEffects.getText());

		buttonOk = new GEButton(new Vector(posX, 400), new Vector(Map.TILE_RENDER_SIZE * 4, Map.TILE_RENDER_SIZE), "OK");

		addGuiElements(buttonOk, sliderMusic, sliderEffects, textMusic1, textMusic2, textEffects1, textEffects2);

	}

	@Override
	public void click(int id, int which) {
		if (id == buttonOk.getId()) {
			leaveMenu();
		}
	}

	@Override
	public void update() {
		super.update();
		textMusic2.setText(sliderMusic.getText());
		textEffects2.setText(sliderEffects.getText());
		SoundCategory.MUSIC.setGain(sliderMusic.getPosition());
		SoundCategory.EFFECT.setGain(sliderEffects.getPosition());
	}

	@Override
	public void renderBackground() {
		TextureBank.instance.bindTexture("title.png");

		glBegin(GL_QUADS);

		Random r = new Random();

		glTexCoord2f(r.nextFloat(), r.nextFloat());
		glVertex3f(0, 0, -1f);

		glTexCoord2f(r.nextFloat(), r.nextFloat());
		glVertex3f(Game.game.WIDTH, 0, -1f);

		glTexCoord2f(r.nextFloat(), r.nextFloat());
		glVertex3f(Game.game.WIDTH, Game.game.HEIGHT, -1f);

		glTexCoord2f(r.nextFloat(), r.nextFloat());
		glVertex3f(0, Game.game.HEIGHT, -1f);

		glEnd();
	}

	@Override
	public void keyPressed(char key) {
		if (key == 27) {
			leaveMenu();
		}
	}

	public void leaveMenu() {
		GuiBank.selectGui(referrer);
	}

}

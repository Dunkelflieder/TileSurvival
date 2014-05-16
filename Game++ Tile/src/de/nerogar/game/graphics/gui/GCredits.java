package de.nerogar.game.graphics.gui;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.util.Random;

import de.nerogar.game.*;
import de.nerogar.game.graphics.TextureBank;

public class GCredits extends Gui {

	private GEButton buttonBack;
	private GEText textNerogar1, textNerogar2, textFelk1, textFelk2, textGurke1, textGurke2;

	public GCredits() {
		super(true);

		//float width = 4f;
		//System.out.println((Map.TILE_RENDER_SIZE / Map.TILE_PIXEL_COUNT));
		//float posX = ((Game.game.WIDTH - (width / 2f) * (Map.TILE_RENDER_SIZE / Map.TILE_PIXEL_COUNT) * Map.TILE_RENDER_SIZE) / 2) / (Map.TILE_RENDER_SIZE);

		float posX = (Game.game.WIDTH - Map.TILE_RENDER_SIZE * 4f) * 0.5f;


		textNerogar1 = new GEText(new Vector(64, 240), new Vector(160, 24), "Nerogar:");
		textFelk1 = new GEText(new Vector(64, 300), new Vector(160, 24), "Felk:");
		textGurke1 = new GEText(new Vector(64, 360), new Vector(160, 24), "Gurke:");

		textNerogar2 = new GEText(new Vector(280, 240), new Vector(Game.game.WIDTH - 300, 24), "lead programming, artwork");
		textFelk2 = new GEText(new Vector(280, 300), new Vector(Game.game.WIDTH - 300, 24), "additional programming, music & sound");
		textGurke2 = new GEText(new Vector(280, 360), new Vector(Game.game.WIDTH - 300, 24), "lead game design, map design");
		
		textNerogar1.setAlignment(FontRenderer.LEFT);
		textFelk1.setAlignment(FontRenderer.LEFT);
		textGurke1.setAlignment(FontRenderer.LEFT);
		textNerogar2.setAlignment(FontRenderer.LEFT);
		textFelk2.setAlignment(FontRenderer.LEFT);
		textGurke2.setAlignment(FontRenderer.LEFT);

		buttonBack = new GEButton(new Vector(posX, 420), new Vector(Map.TILE_RENDER_SIZE * 4, Map.TILE_RENDER_SIZE), "back");

		addGuiElements(buttonBack, textNerogar1, textFelk1, textGurke1, textNerogar2, textFelk2, textGurke2);

	}

	@Override
	public void click(int id, int which) {
		if (id == buttonBack.getId()) {
			leaveMenu();
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

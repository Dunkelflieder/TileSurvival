package de.nerogar.game.graphics.gui;

import static org.lwjgl.opengl.GL11.glColor3f;

import de.nerogar.game.Game;
import de.nerogar.game.Vector;
import de.nerogar.game.entity.EntityPlayer;
import de.nerogar.game.weapon.Weapon;

public class GIngame extends Gui {

	private GEStatusBar barHealth;
	private GEStatusBar barEnergy;

	public GIngame() {
		super(false);

		float posY = Game.game.HEIGHT - (64 + Weapon.renderSize);

		barHealth = new GEStatusBar(new Vector(32f, posY - 20f), 0);
		barEnergy = new GEStatusBar(new Vector(32f, posY), 2);

		addGuiElements(barHealth, barEnergy);

	}

	@Override
	public void renderBackground() {
		// render weapons
		Weapon[] weapons = Game.game.map.getPlayer().getPlayerClass().weapons;
		float posX = 32f;
		float posY = Game.game.HEIGHT - (32f + Weapon.renderSize);
		for (int i = 0; i < weapons.length; i++) {
			Weapon w = weapons[i];
			w.render(posX + i * (8f + Weapon.renderSize), posY);
			if (w.level > 0) {
				glColor3f(0, 1, 0);
				FontRenderer.drawString("+" + w.level, posX + i * (8f + Weapon.renderSize) + 4, posY + Weapon.renderSize - 16f, 64f, 12f, FontRenderer.LEFT);
				glColor3f(1, 1, 1);
			}
		}
	}

	@Override
	public void update() {
		super.update();
		EntityPlayer player = Game.game.map.getPlayer();
		barHealth.setPosition((float) player.health / player.maxHealth);
		barEnergy.setPosition((float) player.energy / player.maxEnergy);
		if (Game.game.map.getPlayer().poisonTime > 0)
			barHealth.setTexturePos(1);
		else
			barHealth.setTexturePos(0);
	}

	@Override
	public void keyPressed(char key) {
		if (key == 27) {
			GuiBank.selectGui(GuiBank.ESCMENU);
		}
	}
}

package de.nerogar.game.graphics.gui;

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
		barEnergy = new GEStatusBar(new Vector(32f, posY), 1);

		addGuiElements(barHealth, barEnergy);

	}

	@Override
	public void renderBackground() {
		// render weapons
		Weapon[] weapons = Game.game.map.getPlayer().playerClass.weapons;
		float posX = 32f;
		float posY = Game.game.HEIGHT - (32f + Weapon.renderSize);
		for (int i = 0; i < weapons.length; i++) {
			Weapon w = weapons[i];
			w.render(posX + i * (8f + Weapon.renderSize), posY);
		}
	}

	@Override
	public void update() {
		super.update();
		EntityPlayer player = Game.game.map.getPlayer();
		barHealth.setPosition((float) player.health / player.maxHealth);
		barEnergy.setPosition((float) player.energy / player.maxEnergy);
	}

	@Override
	public void keyPressed(char key) {
		if (key == 27) {
			GuiBank.selectGui(GuiBank.GUI_ESCMENU);
		}
	}
}

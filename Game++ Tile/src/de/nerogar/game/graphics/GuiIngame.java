package de.nerogar.game.graphics;

import org.lwjgl.opengl.Display;

import de.nerogar.game.Map;
import de.nerogar.game.entity.EntityPlayer;
import de.nerogar.game.weapon.Weapon;

public class GuiIngame extends Gui {

	private StatusBar lifeBar;
	private StatusBar energyBar;
	private EntityPlayer player;

	public GuiIngame(EntityPlayer player) {
		this.player = player;
		lifeBar = new StatusBar(player.map);
		energyBar = new StatusBar(player.map);
		energyBar.texturePos = 1;
	}

	@Override
	public void render() {

		lifeBar.position = (float) player.health / player.maxHealth;
		energyBar.position = (float) player.energy / player.maxEnergy;

		lifeBar.posX = 0.5f;
		lifeBar.posY = (Display.getHeight() / Map.TILE_RENDER_SIZE) - 1.6f;

		energyBar.posX = 0.5f;
		energyBar.posY = (Display.getHeight() / Map.TILE_RENDER_SIZE) - 1.3f;

		lifeBar.render();
		energyBar.render();

		for (int i = 0; i < player.weapons.size(); i++) {
			Weapon weapon = player.weapons.get(i);

			weapon.posX = i + 0.5f;
			weapon.posY = (Display.getHeight() / Map.TILE_RENDER_SIZE) - 1;
			weapon.render();
		}
	}

}

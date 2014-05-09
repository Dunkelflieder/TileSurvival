package de.nerogar.game.graphics;

import org.lwjgl.opengl.Display;

import de.nerogar.game.Map;
import de.nerogar.game.entity.EntityPlayer;
import de.nerogar.game.weapon.Weapon;

public class GuiIngame extends Gui {

	private StatusBar lifeBar;
	private EntityPlayer player;

	public GuiIngame(EntityPlayer player) {
		this.player = player;
		lifeBar = new StatusBar(player.map);
	}

	@Override
	public void render() {

		lifeBar.position = player.health / player.maxHealth;

		lifeBar.posX = 0.5f;
		lifeBar.posY = (Display.getHeight() / Map.TILE_RENDER_SIZE) - 1.5f;

		lifeBar.render();

		for (int i = 0; i < player.weapons.size(); i++) {
			Weapon weapon = player.weapons.get(i);

			weapon.posX = i + 0.5f;
			weapon.posY = (Display.getHeight() / Map.TILE_RENDER_SIZE) - 1;
			weapon.render();
		}
	}

}

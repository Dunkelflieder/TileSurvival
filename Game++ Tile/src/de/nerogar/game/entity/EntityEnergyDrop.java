package de.nerogar.game.entity;

import de.nerogar.game.Map;

public class EntityEnergyDrop extends Entity {

	public EntityEnergyDrop(Map map, float posX, float posY) {
		super(map, posX, posY, Integer.MAX_VALUE);
		moveSpeed = 1.0f;
		textureID = 16 * 15 + 2;

		width = 0.3f;
		height = 0.3f;
	}

	@Override
	public void update(float time) {
		super.update(time);

		EntityPlayer player = map.getPlayer();
		if (player.energy < player.maxEnergy && player.intersects(getCenter())) {
			kill();
			player.energy++;
		}
	}

	@Override
	public void onDie() {

	}
}

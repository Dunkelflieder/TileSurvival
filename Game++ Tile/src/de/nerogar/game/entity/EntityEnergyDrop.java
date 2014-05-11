package de.nerogar.game.entity;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;

public class EntityEnergyDrop extends Entity {

	public EntityEnergyDrop(Map map, Vector pos) {
		super(map, pos, new Vector(0.3f), 0);
		resistDamage = true;
		moveSpeed = 1.0f;
		textureID = 16 * 15 + 2;
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

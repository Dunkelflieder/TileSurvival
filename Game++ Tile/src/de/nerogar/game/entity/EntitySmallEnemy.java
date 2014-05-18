package de.nerogar.game.entity;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;

public class EntitySmallEnemy extends EntityEnemy {

	public EntitySmallEnemy(Map map, Vector pos) {
		super(map, pos, new Vector(1.0f), 5, 1f);
		moveSpeed = 1.0f;
		textureID = 16;
	}

	@Override
	public void update(float time) {
		super.update(time);

		EntityPlayer player = map.getPlayer();
		Vector direction = player.getCenter().subtract(getCenter()).setValue(moveSpeed * time * speedmult);
		move(direction);

		if (intersects(map.getPlayer())) {
			damageEntity(map.getPlayer(), 1);
		}
	}

	@Override
	public void onDie() {
		if (Math.random() < 0.3) {
			map.spawnEntity(new EntityEnergyDrop(map, getCenter()));
		}
	}
}

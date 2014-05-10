package de.nerogar.game.entity;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;

public class EntitySmallEnemy extends EntityEnemy {

	public EntitySmallEnemy(Map map, float posX, float posY) {
		super(map, posX, posY, 5, 1f);
		moveSpeed = 1.0f;
		textureID = 16;
	}

	@Override
	public void update(float time) {
		super.update(time);

		EntityPlayer player = map.getPlayer();
		Vector direction = player.getCenter().subtract(getCenter()).setValue(moveSpeed * time * speedmult);
		moveX(direction.getX());
		moveY(direction.getY());

		if (intersects(map.getPlayer())) {
			damageEntity(map.getPlayer(), 1);
		}
	}

	@Override
	public void onDie() {
		Vector center = getCenter();
		map.spawnEntity(new EntityEnergyDrop(map, center.getX(), center.getY()));
	}
}

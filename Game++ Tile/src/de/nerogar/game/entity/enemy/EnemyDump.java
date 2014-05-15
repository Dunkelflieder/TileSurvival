package de.nerogar.game.entity.enemy;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.entity.EntityEnemy;
import de.nerogar.game.entity.EntityEnergyDrop;

public class EnemyDump extends EntityEnemy {

	public EnemyDump(Map map, Vector pos) {
		super(map, pos, new Vector(1.0f), 5, 1f);
		moveSpeed = 1.0f;
		textureID = 16;
	}

	@Override
	public void update(float time) {
		super.update(time);
	}

	@Override
	public void onDie() {
		if (Math.random() < 0.3) {
			map.spawnEntity(new EntityEnergyDrop(map, getCenter()));
		}
	}
}

package de.nerogar.game.entity.enemy;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.entity.EntityEnemy;
import de.nerogar.game.entity.EntityEnergyDrop;
import de.nerogar.game.weapon.Fireball;

public class EnemyGhost extends EntityEnemy {

	private static final float MAX_FIGHT_DISTANCE = 8.0f;
	private Vector walkPos;

	private Fireball fireballWeapon;

	public EnemyGhost(Map map, Vector pos) {
		super(map, pos, new Vector(1.0f), 10, 1f);
		moveSpeed = 3.0f;
		textureID = 16 * 5;
		ignoreWalls = true;

		fireballWeapon = new Fireball(this, 5, 3.0f);
	}

	@Override
	public void recalcPath() {
		if (target == null) return;
		if (walkPos == null || walkPos.subtracted(target.getCenter()).getValue() > MAX_FIGHT_DISTANCE) {
			walkPos = target.getCenter().add(new Vector((float) Math.random() - 0.5f, (float) Math.random() - 0.5f).setValue((float) Math.random() * MAX_FIGHT_DISTANCE));
		}
	}

	@Override
	public void update(float time) {
		super.update(time);

		if (nextRandomUpdate < 0f) {
			target = map.getNearestPlayer(pos);
			recalcPath();
			nextRandomUpdate = (float) (20f + Math.random() * 10f);
		}

		boolean idle = pos.subtracted(walkPos).getSquaredValue() < 0.5f;

		if (!idle) { //move
			Vector direction = walkPos.subtracted(getCenter()).setValue(moveSpeed * time * speedmult);
			move(direction);
		} else { //shoot
			if (fireballWeapon.cooldown <= 0f) {
				fireballWeapon.start(target.getCenter());
				fireballWeapon.cooldown = fireballWeapon.maxCooldown;
			}
		}

		fireballWeapon.update(time);
	}

	@Override
	public void onDie() {
		if (Math.random() < 0.3) {
			map.spawnEntity(new EntityEnergyDrop(map, getCenter()));
		}
	}
}

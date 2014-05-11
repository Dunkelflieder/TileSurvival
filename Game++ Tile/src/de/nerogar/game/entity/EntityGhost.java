package de.nerogar.game.entity;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.weapon.Fireball;

public class EntityGhost extends EntityEnemy {

	private static final float MAX_FIGHT_DISTANCE = 10.0f;
	private Vector walkPos;

	private Fireball fireballWeapon;

	public EntityGhost(Map map, Vector pos) {
		super(map, pos, new Vector(1.0f), 10, 1f);
		moveSpeed = 3.0f;
		textureID = 32;

		fireballWeapon = new Fireball(this, 0, 3.0f);
	}

	@Override
	public void update(float time) {
		super.update(time);

		EntityPlayer player = map.getPlayer();

		if (walkPos == null || walkPos.subtracted(player.getCenter()).getValue() > MAX_FIGHT_DISTANCE) {
			walkPos = player.getCenter().add(new Vector((float) Math.random(), (float) Math.random()).setValue((float) Math.random() * MAX_FIGHT_DISTANCE));
		}

		Vector direction = walkPos.subtracted(getCenter()).setValue(moveSpeed * time * speedmult);
		moveX(direction.getX());
		moveY(direction.getY());

		if (fireballWeapon.cooldown <= 0f) {
			fireballWeapon.start(player.getCenter());
			fireballWeapon.cooldown = fireballWeapon.maxCooldown;
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

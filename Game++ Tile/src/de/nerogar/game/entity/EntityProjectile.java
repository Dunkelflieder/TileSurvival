package de.nerogar.game.entity;

import java.util.ArrayList;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;

public abstract class EntityProjectile extends EntityWeapon {

	private Vector source;
	private Vector direction;

	private int hitCount;
	private int maxHitCount;
	private ArrayList<Entity> hitEntities;

	public EntityProjectile(Map map, Vector pos, int maxHitCount) {
		super(map, pos);
		this.maxHitCount = maxHitCount;
		dimension = new Vector(1f);
		init();
	}

	public EntityProjectile(Entity sender, Map map, Vector pos, Vector target, int damage, int maxHitCount, int speed) {
		super(sender, map, pos, new Vector(1f), damage);
		this.maxHitCount = maxHitCount;
		this.moveSpeed = speed;
		this.resistDamage = true;

		this.source = pos.clone();

		this.sender = sender;

		direction = target.subtracted(source);
		direction.setValue(moveSpeed);

		hitEntities = new ArrayList<Entity>();

		init();
	}

	protected abstract void init();

	@Override
	public void update(float time) {
		super.update(time);

		ArrayList<Entity> intersectingEntities = map.getIntersectingEntities(this);
		for (Entity entity : intersectingEntities) {
			if (canDamage(entity) /*&& intersects(entity)*/ && !hitEntities.contains(entity)) {
				hitCount++;
				hitEntities.add(entity);
				entity.damage(health);
			}

			if (hitCount == maxHitCount) {
				kill();
				break;
			}
		}

		if (map.isColliding(direction.multiplied(time).add(pos), dimension, false)) {
			kill();
		} else {
			move(direction.multiplied(time));
		}
	}

	@Override
	public void onDie() {
	}
}

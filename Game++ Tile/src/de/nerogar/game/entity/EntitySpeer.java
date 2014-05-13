package de.nerogar.game.entity;

import java.util.ArrayList;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.graphics.Light;

public class EntitySpeer extends EntityWeapon {

	private Vector source;
	private Vector direction;

	private int hitCount;
	private int maxHitcount = 2;
	private ArrayList<Entity> hitEntities;

	public EntitySpeer(Map map, Vector pos) {
		super(map, pos);
		dimension = new Vector(1f);
		init();
	}

	public EntitySpeer(Entity sender, Map map, Vector pos, Vector target, int damage) {
		super(sender, map, pos, new Vector(1f), damage);
		resistDamage = true;

		this.source = pos.clone();

		this.sender = sender;

		moveSpeed = 30.0f;

		direction = target.subtracted(source);
		direction.setValue(moveSpeed);

		hitEntities = new ArrayList<Entity>();

		init();
	}

	private void init() {
		textureID = 16 * 15 + 3;

		light = new Light(new Vector(), 2, 0.8f);
	}

	@Override
	public void update(float time) {
		super.update(time);

		ArrayList<Entity> intersectingEntities = map.getIntersectingEntities(this);
		for (Entity entity : intersectingEntities) {
			if (canDamage(entity) && intersects(entity) && !hitEntities.contains(entity)) {
				hitCount++;
				hitEntities.add(entity);
				entity.damage(health);
			}

			if (hitCount == maxHitcount) {
				kill();
				break;
			}
		}

		if (map.isColliding(direction.multiplied(time).add(pos), dimension)) {
			kill();
		} else {
			moveX(direction.getX() * time);
			moveY(direction.getY() * time);
		}
	}

	@Override
	public void onDie() {
	}
}

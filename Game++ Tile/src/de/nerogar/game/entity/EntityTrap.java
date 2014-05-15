package de.nerogar.game.entity;

import java.util.ArrayList;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.graphics.Light;

public class EntityTrap extends EntityWeapon {

	private int hitCount;
	private int maxHitcount = 2;
	private ArrayList<Entity> hitEntities;

	public EntityTrap(Map map, Vector pos) {
		super(map, pos);
		dimension = new Vector(2f);
		init();
	}

	public EntityTrap(Entity sender, Map map, Vector pos, int damage) {
		super(sender, map, pos, new Vector(2f), damage);
		resistDamage = true;

		hitEntities = new ArrayList<Entity>();

		init();
	}

	private void init() {
		textureID = 16 * 15 + 4;

		light = new Light(new Vector(), 2, 0.8f);
	}

	@Override
	public void update(float time) {
		super.update(time);

		ArrayList<Entity> intersectingEntities = map.getIntersectingEntities(this);
		for (Entity entity : intersectingEntities) {
			if (canDamage(entity) && !hitEntities.contains(entity)) {
				hitCount++;
				hitEntities.add(entity);
				entity.damage(health);
			}

			if (hitCount == maxHitcount) {
				kill();
				break;
			}
		}
	}

	@Override
	public void onDie() {
	}
}

package de.nerogar.game.entity;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.graphics.Light;

public class EntitySpear extends EntityProjectile {

	public EntitySpear(Map map, Vector pos) {
		super(map, pos, 3);
		dimension = new Vector(1f);
		init();
	}

	public EntitySpear(Entity sender, Map map, Vector pos, Vector target, int damage) {
		super(sender, map, pos, target, damage, 3, 30);
	}

	@Override
	protected void init() {
		textureID = 16 * 15 + 3;

		light = new Light(new Vector(), 2, 0.8f);
	}

}

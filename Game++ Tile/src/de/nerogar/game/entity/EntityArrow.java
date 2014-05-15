package de.nerogar.game.entity;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.graphics.Light;

public class EntityArrow extends EntityProjectile {

	public EntityArrow(Map map, Vector pos) {
		super(map, pos, 1);
		dimension = new Vector(1f);
		init();
	}

	public EntityArrow(Entity sender, Map map, Vector pos, Vector target, int damage) {
		super(sender, map, pos, new Vector(1f), damage, 1, 50);
	}

	@Override
	protected void init() {
		textureID = 16 * 15 + 3;

		light = new Light(new Vector(), 2, 0.8f);
	}

}

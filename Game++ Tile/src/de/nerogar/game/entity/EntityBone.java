package de.nerogar.game.entity;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.graphics.Light;

public class EntityBone extends Entity {

	public EntityBone(Map map, Vector pos) {
		super(map, pos, new Vector(0.3f), 0, false);
		resistDamage = true;
		textureID = 16 * 15 + 2;

		light = new Light(pos, 3f, 0.5f);
	}

	@Override
	public void update(float time) {
		super.update(time);
	}

	@Override
	public void onDie() {

	}
}

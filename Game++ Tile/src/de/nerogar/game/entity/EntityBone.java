package de.nerogar.game.entity;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.graphics.Light;

public class EntityBone extends Entity {

	public EntityBone(Map map, Vector pos) {
		super(map, pos, new Vector(1.0f), 1, false);
		resistDamage = true;
		textureID = 16 * 15 + 3;

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

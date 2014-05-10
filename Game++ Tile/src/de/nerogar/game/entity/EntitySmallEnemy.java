package de.nerogar.game.entity;

import de.nerogar.game.Map;

public class EntitySmallEnemy extends Entity {

	public EntitySmallEnemy(Map map, float posX, float posY) {
		super(map, posX, posY, 5);
		moveSpeed = 1.0f;
		textureID = 16;
	}

	@Override
	public void update(float time) {
		super.update(time);
	}
}

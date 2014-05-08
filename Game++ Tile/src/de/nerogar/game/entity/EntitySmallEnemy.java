package de.nerogar.game.entity;

import de.nerogar.game.Map;

public class EntitySmallEnemy extends Entity {

	public EntitySmallEnemy(Map map, float posX, float posY) {
		super(map, posX, posY);
		moveSpeed = 1.0f;
		health = 5;
		textureID = 16;
	}

	@Override
	public void update(float time) {

	}
}

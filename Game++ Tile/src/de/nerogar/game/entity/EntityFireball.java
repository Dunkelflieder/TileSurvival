package de.nerogar.game.entity;

import de.nerogar.game.Map;

public class EntityFireball extends Entity {

	private float sourceX;
	private float sourceY;

	private float targetX;
	private float targetY;

	private static final float MAX_LIFETIME = 0.15f;
	private float lifetime;

	public EntityFireball(Map map, float posX, float posY, float targetX, float targetY) {
		super(map, posX, posY);

		this.sourceX = posX;
		this.sourceY = posY;

		this.targetX = targetX;
		this.targetY = targetY;
		textureID = 16 * 15;
		width = 0.2f;
		height = 0.2f;

		lifetime = MAX_LIFETIME;

	}

	@Override
	public void update(float time) {
		float dist = time / MAX_LIFETIME;

		float distX = dist * (targetX - sourceX);
		float distY = dist * (targetY - sourceY);

		if (map.isColliding(posX + distX, posY + distY, width, height)) {
			explode();
		} else {
			moveX(distX);
			moveY(distY);
		}

		lifetime -= time;

		if (lifetime < 0) explode();
	}

	private void explode() {
		//TODO explode

		remove();
	}
}

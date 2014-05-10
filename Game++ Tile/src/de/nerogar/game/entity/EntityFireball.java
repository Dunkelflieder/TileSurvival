package de.nerogar.game.entity;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;

public class EntityFireball extends Entity {

	private float sourceX;
	private float sourceY;

	private float targetX;
	private float targetY;

	Vector direction;
	private float hitTime;

	public EntityFireball(Map map, float posX, float posY, float targetX, float targetY) {
		super(map, posX, posY, 0);

		this.sourceX = posX;
		this.sourceY = posY;

		this.targetX = targetX;
		this.targetY = targetY;

		moveSpeed = 50.0f;

		direction = new Vector((targetX - sourceX), (targetY - sourceY));
		direction.setValue(moveSpeed);

		hitTime = (targetX - sourceX) / direction.getX();

		textureID = 16 * 15;
		width = 0.2f;
		height = 0.2f;
	}

	@Override
	public void update(float time) {
		hitTime -= time;

		if (map.isColliding(posX + direction.getX() * time, posY + direction.getY() * time, width, height)) {
			explode();
		} else if (hitTime < 0) {
			posX = targetX;
			posY = targetY;
			explode();
		} else {
			moveX(direction.getX() * time);
			moveY(direction.getY() * time);
		}
	}

	private void explode() {
		//TODO explode

		remove();
	}
}

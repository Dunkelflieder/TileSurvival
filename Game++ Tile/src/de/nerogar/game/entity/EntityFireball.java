package de.nerogar.game.entity;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.graphics.Light;
import de.nerogar.game.sound.Sound;
import de.nerogar.game.sound.SoundManager;

public class EntityFireball extends Entity {

	private float sourceX;
	private float sourceY;

	private float targetX;
	private float targetY;

	Vector direction;
	private float hitTime;

	private Entity sender;

	private static Sound explodeSound = SoundManager.create("smallpuff1.ogg", new Vector(0,0));
	
	public EntityFireball(Entity sender, Map map, float posX, float posY, float targetX, float targetY, int damage) {
		super(map, posX, posY, damage);

		this.sourceX = posX;
		this.sourceY = posY;

		this.targetX = targetX;
		this.targetY = targetY;

		this.sender = sender;

		moveSpeed = 50.0f;

		direction = new Vector((targetX - sourceX), (targetY - sourceY));
		direction.setValue(moveSpeed);

		hitTime = (targetX - sourceX) / direction.getX();

		textureID = 16 * 15;
		width = 0.2f;
		height = 0.2f;

		light = new Light(0, 0, 2, 0.8f);
	}

	@Override
	public void update(float time) {
		hitTime -= time;

		if (map.isColliding(posX + direction.getX() * time, posY + direction.getY() * time, width, height)) {
			kill();
		} else if (hitTime < 0) {
			posX = targetX;
			posY = targetY;
			kill();
		} else {
			moveX(direction.getX() * time);
			moveY(direction.getY() * time);
		}
	}

	@Override
	public void onDie() {
		map.spawnEntity(new EntityExplosion(sender, map, posX + (width / 2), posY + (height / 2), 2f, health));
		explodeSound.setPosition(getCenter());
		explodeSound.play();
	}
}

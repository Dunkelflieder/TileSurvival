package de.nerogar.game.entity;

import static org.lwjgl.opengl.GL11.glColor3f;

import java.util.ArrayList;

import de.nerogar.game.*;
import de.nerogar.game.graphics.Light;
import de.nerogar.game.sound.Sound;
import de.nerogar.game.sound.SoundCategory;

public class EntityTrap extends EntityWeapon {

	private int hitCount;
	private int maxHitcount = 10;
	private ArrayList<Entity> hitEntities;
	private Sound activateSound = new Sound(SoundCategory.EFFECT, "place1.ogg");

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
		
		activateSound.setPosition(pos);
		activateSound.randomizePitch(0.4f);
		activateSound.play();
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

	@Override
	public void renderAfterShader() {

		float hitTime = (float) hitCount / (float) maxHitcount;
		glColor3f(0.0f, 0.1f, 0.1f);
		RenderHelper.renderLine((pos.getX() - map.getOffsX()) * Map.TILE_RENDER_SIZE, (pos.getY() - map.getOffsY()) * Map.TILE_RENDER_SIZE, (pos.getX() - map.getOffsX() + dimension.getX()) * Map.TILE_RENDER_SIZE, (pos.getY() - map.getOffsY()) * Map.TILE_RENDER_SIZE, 2);
		glColor3f(0.2f, 0.2f, 0.8f);
		RenderHelper.renderLine((pos.getX() - map.getOffsX()) * Map.TILE_RENDER_SIZE, (pos.getY() - map.getOffsY()) * Map.TILE_RENDER_SIZE, (pos.getX() - map.getOffsX() + dimension.getX() * hitTime) * Map.TILE_RENDER_SIZE, (pos.getY() - map.getOffsY()) * Map.TILE_RENDER_SIZE, 2);
		glColor3f(1, 1, 1);

	}

}

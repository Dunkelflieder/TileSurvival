package de.nerogar.game.entity;

import static org.lwjgl.opengl.GL11.glColor3f;
import de.nerogar.game.*;
import de.nerogar.game.graphics.Light;

public class EntityBomb extends EntityWeapon {

	private final float MAX_LIFETIME = 3f;
	private float lifetime;

	public EntityBomb(Map map, Vector pos) {
		super(map, pos);
		dimension = new Vector(1f);
		init();
	}

	public EntityBomb(Entity sender, Map map, Vector pos, int damage) {
		super(sender, map, pos, new Vector(1.0f), damage);
		resistDamage = true;
		lifetime = MAX_LIFETIME;
		init();
	}

	private void init() {
		textureID = 16 * 15 + 3;

		light = new Light(pos, 3f, 0.5f);
	}

	@Override
	public void update(float time) {
		super.update(time);
		lifetime -= time;
		if (lifetime <= 0f) kill();

	}

	@Override
	public void onDie() {
		map.spawnEntity(new EntityExplosion(sender, map, pos, 5, health));
	}

	@Override
	public void renderAfterShader() {

		float fuseTime = (float) lifetime / MAX_LIFETIME;
		glColor3f(0.0f, 0.1f, 0.1f);
		RenderHelper.renderLine((pos.getX() - map.getOffsX()) * Map.TILE_RENDER_SIZE, (pos.getY() - map.getOffsY()) * Map.TILE_RENDER_SIZE, (pos.getX() - map.getOffsX() + dimension.getX()) * Map.TILE_RENDER_SIZE, (pos.getY() - map.getOffsY()) * Map.TILE_RENDER_SIZE, 2);
		glColor3f(0.2f, 0.2f, 0.8f);
		RenderHelper.renderLine((pos.getX() - map.getOffsX()) * Map.TILE_RENDER_SIZE, (pos.getY() - map.getOffsY()) * Map.TILE_RENDER_SIZE, (pos.getX() - map.getOffsX() + dimension.getX() * fuseTime) * Map.TILE_RENDER_SIZE, (pos.getY() - map.getOffsY()) * Map.TILE_RENDER_SIZE, 2);
		glColor3f(1, 1, 1);

	}

}

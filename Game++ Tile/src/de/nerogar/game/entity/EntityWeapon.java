package de.nerogar.game.entity;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;

public abstract class EntityWeapon extends Entity {

	public Entity sender;

	public EntityWeapon(Map map, Vector pos) {
		super(map, pos, new Vector(), 0);
	}

	public EntityWeapon(Entity sender, Map map, Vector pos, Vector dimension, int damage) {
		super(map, pos, dimension, damage);
		faction = sender.faction;
		resistDamage = true;

		this.sender = sender;

		moveSpeed = 10.0f;
	}

	public boolean canDamage(Entity entity) {
		return entity.faction != faction;
	}
}

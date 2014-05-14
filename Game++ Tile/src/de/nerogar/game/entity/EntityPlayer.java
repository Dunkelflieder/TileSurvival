package de.nerogar.game.entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import de.nerogar.game.*;
import de.nerogar.game.entity.playerClass.Engineer;
import de.nerogar.game.entity.playerClass.PlayerClass;
import de.nerogar.game.network.*;
import de.nerogar.game.weapon.Weapon;

public class EntityPlayer extends Entity {

	public PlayerClass playerClass;

	private float nextEnergyRestore;

	public EntityPlayer(Map map, Vector pos) {
		super(map, pos, new Vector(1.0f), 100);
		playerClass = new Engineer(this);
		faction = FACTION_PLAYER;

		//light = new Light(0, 0, 5f,2.0f);
	}

	@Override
	public void update(float time) {
		super.update(time);
	}

	public Weapon getSelectedWeapon() {
		return playerClass.getSelectedWeapon();
	}

	public void updateInput(float time, Client client) {
		float[] sendPos = new float[2];
		boolean newPos = false;

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			moveX(moveSpeed * time * speedmult);
			newPos = true;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			moveX(-moveSpeed * time * speedmult);
			newPos = true;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			moveY(-moveSpeed * time * speedmult);
			newPos = true;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			moveY(moveSpeed * time * speedmult);
			newPos = true;
		}

		if (client != null && newPos) {
			PacketPlayerPosition playerPositionPacket = new PacketPlayerPosition();
			playerPositionPacket.playerID = id;
			sendPos[0] = pos.getX();
			sendPos[1] = pos.getY();
			playerPositionPacket.playerPosition = sendPos;
			Game.game.client.sendPacket(playerPositionPacket);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
			playerClass.selectWeapon(0);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
			playerClass.selectWeapon(1);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
			playerClass.selectWeapon(2);
		}
		if (InputHandler.isMouseButtonPressed(1)) {
			playerClass.selectNextWeapon();
		}

		if (Mouse.isButtonDown(0)) {

			Vector target = new Vector();

			target.setX(((float) Mouse.getX()) / Map.TILE_RENDER_SIZE + map.getOffsX());
			target.setY((float) (Display.getHeight() - Mouse.getY()) / Map.TILE_RENDER_SIZE + map.getOffsY());

			Weapon weapon = playerClass.getSelectedWeapon();

			if (weapon.cooldown <= 0f && energy >= weapon.energyCost && weapon.canActivate()) {
				if (client == null) playerClass.getSelectedWeapon().start(target);
				energy -= playerClass.getSelectedWeapon().energyCost;
				weapon.cooldown = weapon.maxCooldown;

				if (client != null) {
					PacketActivateWeapon activateWeaponPacket = new PacketActivateWeapon();
					activateWeaponPacket.targetPosition = new float[] { target.getX(), target.getY() };
					activateWeaponPacket.playerID = id;
					activateWeaponPacket.selectedWeapon = playerClass.selectedWeapon;
					Game.game.client.sendPacket(activateWeaponPacket);
				}
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
			playerClass.activateWeaponDebugTimes();
		}

	}

	public void updateStats(float time) {
		playerClass.updateWeapons(time);

		if (nextEnergyRestore <= 0 && energy < maxEnergy) {
			nextEnergyRestore = 0.5f;
			energy += 1;
		}
		nextEnergyRestore -= time;
	}

	@Override
	public void onDie() {
		//TODO game end
	}
}

package de.nerogar.game.entity;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import de.nerogar.game.*;
import de.nerogar.game.network.*;
import de.nerogar.game.weapon.*;

public class EntityPlayer extends Entity {

	public ArrayList<Weapon> weapons;
	public int selectedWeapon;

	private float nextEnergyRestore;

	public EntityPlayer(Map map, Vector pos) {
		super(map, pos, new Vector(1.0f), 100);
		weapons = new ArrayList<Weapon>();
		weapons.add(new Fireball(this, 3, 1.0f));
		//weapons.add(new SlowDownArea(this, 0, 2.0f));
		//weapons.add(new FireBlast(this, 10, 2.0f));
		//weapons.add(new Heal(this, 20, 2.0f));

		maxEnergy = 100;
		energy = maxEnergy;
		moveSpeed = 3.0f;

		//light = new Light(0, 0, 5f,2.0f);
	}

	@Override
	public void update(float time) {
		super.update(time);
	}

	public Weapon getSelectedWeapon() {
		return weapons.get(selectedWeapon);
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
			selectedWeapon = 0;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_2) && weapons.size() > 1) {
			selectedWeapon = 1;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_3) && weapons.size() > 2) {
			selectedWeapon = 2;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_4) && weapons.size() > 3) {
			selectedWeapon = 3;
		}
		if (InputHandler.isMouseButtonPressed(1)) {
			selectedWeapon = (selectedWeapon + 1) % weapons.size();
		}
		selectedWeapon = (selectedWeapon - Integer.signum(Mouse.getDWheel()) + weapons.size()) % weapons.size();

		for (Weapon weapon : weapons) {
			weapon.update(time);
		}

		if (Mouse.isButtonDown(0)) {

			Vector target = new Vector();

			target.setX(((float) Mouse.getX()) / Map.TILE_RENDER_SIZE + map.getOffsX());
			target.setY((float) (Display.getHeight() - Mouse.getY()) / Map.TILE_RENDER_SIZE + map.getOffsY());

			Weapon weapon = weapons.get(selectedWeapon);

			if (weapon.cooldown <= 0f && energy >= weapon.energyCost && weapon.canActivate()) {
				weapons.get(selectedWeapon).start(target);
				energy -= weapons.get(selectedWeapon).energyCost;
				weapon.cooldown = weapon.maxCooldown;

				if (client != null) {
					PacketActivateWeapon activateWeaponPacket = new PacketActivateWeapon();
					activateWeaponPacket.targetPosition = new float[] { target.getX(), target.getY() };
					activateWeaponPacket.playerID = id;
					activateWeaponPacket.selectedWeapon = selectedWeapon;
					Game.game.client.sendPacket(activateWeaponPacket);
				}
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
			for (Weapon weapon : weapons) {
				weapon.maxCooldown = 0;
				weapon.energyCost = 0;
			}
		}

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

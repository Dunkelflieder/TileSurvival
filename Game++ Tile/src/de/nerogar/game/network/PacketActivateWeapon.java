package de.nerogar.game.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;

public class PacketActivateWeapon extends Packet {
	public int playerID;
	public float[] targetPosition;
	public int selectedWeapon;

	public PacketActivateWeapon() {
		channel = WORLD_CHANNEL;
	}

	@Override
	public void pack() {
		data = new DNFile();

		data.addInt("pID", playerID);
		data.addFloat("pos", targetPosition);
		data.addInt("wID", selectedWeapon);

		packedData = data.toByteArray();
	}

	@Override
	public void unpack() {
		data = new DNFile();
		try {
			data.fromByteArray(packedData);
		} catch (IOException e) {
			e.printStackTrace();
		}

		playerID = data.getInt("ep");
		targetPosition = data.getFloatArray("pos");
		selectedWeapon = data.getInt("wID");
	}

	@Override
	public String getName() {
		return "activateWeapon";
	}
}

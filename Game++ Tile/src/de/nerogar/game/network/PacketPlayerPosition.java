package de.nerogar.game.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;

public class PacketPlayerPosition extends Packet {
	public int playerID;
	public float[] playerPosition;
	public int playerDirection;

	public PacketPlayerPosition() {
		channel = WORLD_CHANNEL;
	}

	@Override
	public void pack() {
		data = new DNFile();

		data.addInt("pID", playerID);
		data.addFloat("pos", playerPosition);
		data.addInt("dir", playerDirection);

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

		playerID = data.getInt("pID");
		playerPosition = data.getFloatArray("pos");
		playerDirection = data.getInt("dir");
	}

	@Override
	public String getName() {
		return "playerPosition";
	}
}

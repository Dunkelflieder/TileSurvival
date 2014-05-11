package de.nerogar.game.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;

public class PacketSpawnEntity extends Packet {
	public float[] pos;
	public int entityID;
	public int spawnID;

	public PacketSpawnEntity() {
		channel = WORLD_CHANNEL;
	}

	@Override
	public void pack() {
		data = new DNFile();

		data.addFloat("pos", pos);
		data.addInt("id", entityID);
		data.addInt("sID", spawnID);

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

		pos = data.getFloatArray("pos");
		entityID = data.getInt("id");
		spawnID = data.getInt("sID");
	}

	@Override
	public String getName() {
		return "spawnEntity";
	}
}

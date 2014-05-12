package de.nerogar.game.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;

public class PacketDespawnEntity extends Packet {
	public int entityID;

	public PacketDespawnEntity() {
		channel = WORLD_CHANNEL;
	}

	@Override
	public void pack() {
		data = new DNFile();

		data.addInt("id", entityID);

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

		entityID = data.getInt("id");
	}

	@Override
	public String getName() {
		return "despawnEntity";
	}
}

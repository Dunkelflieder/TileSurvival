package de.nerogar.game.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;

public class PacketEntityPositions extends Packet {
	public float[] entityPositions;
	public int[] entityIDs;

	public PacketEntityPositions() {
		channel = WORLD_CHANNEL;
	}

	@Override
	public void pack() {
		data = new DNFile();

		data.addFloat("ep", entityPositions);
		data.addInt("eID", entityIDs);

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

		entityPositions = data.getFloatArray("ep");
		entityIDs = data.getIntArray("eID");
	}

	@Override
	public String getName() {
		return "entityPositions";
	}
}

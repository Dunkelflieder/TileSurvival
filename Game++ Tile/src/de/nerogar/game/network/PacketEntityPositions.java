package de.nerogar.game.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;

public class PacketEntityPositions extends Packet {
	public int[] entityIDs;
	public float[] entityPositions;
	public float[] entityMoveSpeeds;
	public float[] entitySpeedMults;

	public PacketEntityPositions() {
		channel = WORLD_CHANNEL;
	}

	@Override
	public void pack() {
		data = new DNFile();

		data.addFloat("ep", entityPositions);
		data.addInt("eID", entityIDs);
		data.addFloat("s", entityMoveSpeeds);
		data.addFloat("sm", entitySpeedMults);

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
		entityMoveSpeeds = data.getFloatArray("s");
		entitySpeedMults = data.getFloatArray("sm");
	}

	@Override
	public String getName() {
		return "entityPositions";
	}
}

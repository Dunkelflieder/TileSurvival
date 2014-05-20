package de.nerogar.game.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;

public class PacketEntityPositions extends Packet {
	public int[] entityIDs;
	public float[] entityPositions;
	public float[] entityMoveSpeeds;
	public float[] entitySpeedMults;
	public int[] entityDirections;
	public float[] entityHealths;
	public float[] entityEnergys;

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
		data.addInt("d", entityDirections);
		data.addFloat("h", entityHealths);data.addFloat("e", entityEnergys);

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
		entityDirections = data.getIntArray("d");
		entityHealths = data.getFloatArray("h");
		entityEnergys = data.getFloatArray("e");
		
	}

	@Override
	public String getName() {
		return "entityPositions";
	}
}

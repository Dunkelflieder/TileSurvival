package de.nerogar.game.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;

public class PacketSelectPlayerClass extends Packet {
	public int playerID;
	public int playerClass;

	public PacketSelectPlayerClass() {
		channel = LOBBY_CHANNEL;
	}

	@Override
	public void pack() {
		data = new DNFile();

		data.addInt("id", playerID);
		data.addInt("pC", playerClass);

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

		playerID = data.getInt("id");
		playerClass = data.getInt("pC");
	}

	@Override
	public String getName() {
		return "selectPlayerClass";
	}
}

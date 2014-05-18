package de.nerogar.game.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;

public class PacketStartGame extends Packet {
	public int playerID;

	public PacketStartGame() {
		channel = LOBBY_CHANNEL;
	}

	@Override
	public void pack() {
		data = new DNFile();

		data.addInt("pID", playerID);

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
	}

	@Override
	public String getName() {
		return "entityPositions";
	}

	@Override
	public String toString() {
		return "PacketStartGame(playerID: " + playerID + ")";
	}
}

package de.nerogar.game.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;
import de.nerogar.game.Game;

public class PacketConnectionInfo extends Packet {
	public String version;
	public String username;

	@Override
	public void pack() {
		data = new DNFile();
		version = Game.version;
		username = Game.username;

		data.addString("version", version);
		data.addString("username", username);

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

		version = data.getString("version");
		username = data.getString("username");
	}

	@Override
	public String getName() {
		return "ConnectionInfo";
	}
}

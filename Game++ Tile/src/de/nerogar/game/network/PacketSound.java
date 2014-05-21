package de.nerogar.game.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;
import de.nerogar.game.Vector;

public class PacketSound extends Packet {
	public Vector pos;
	public String sound[];

	public PacketSound() {
		channel = WORLD_CHANNEL;
	}

	@Override
	public void pack() {
		data = new DNFile();

		data.addFloat("x", pos.getX());
		data.addFloat("y", pos.getY());
		data.addString("s", sound);

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

		pos = new Vector(data.getFloat("x"), data.getFloat("y"));
		sound = data.getStringArray("s");
	}

	@Override
	public String getName() {
		return "sound";
	}
}

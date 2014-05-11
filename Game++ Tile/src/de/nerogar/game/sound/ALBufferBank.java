package de.nerogar.game.sound;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.OpenALException;

import org.lwjgl.util.WaveData;

public class ALBufferBank {

	public static HashMap<String, ALBuffer> buffers;

	static {
		buffers = new HashMap<String, ALBuffer>();
	}

	public static void addSound(String filename) throws IOException, LWJGLException, OpenALException {
		File file = new File("res/sound/" + filename);

		int id = ALHelper.genBuffers();

		// Datei in den Buffer laden
		String format = getExtension(file);
		switch (format) {
		case "wav":
			setWaveFile(id, file);
			break;
		default:
			System.out.println("did not recognize extension for: " + file.getName());
			throw new IOException();
		}

		int size = ALHelper.getBufferSize(id);
		buffers.put(file.getName(), new ALBuffer(id, size));
	}

	public static ALBuffer getSound(String filename) throws OpenALException, IOException, LWJGLException {
		if (!buffers.containsKey(filename))
			addSound(filename);
		return buffers.get(filename);
	}

	public static ALBuffer[] getSounds(String[] filenames) throws OpenALException, IOException, LWJGLException {
		ALBuffer[] buffers = new ALBuffer[filenames.length];
		for (int i = 0; i < buffers.length; i++) {
			buffers[i] = getSound(filenames[i]);
		}
		return buffers;
	}

	private static void setWaveFile(int bufferID, File file) throws FileNotFoundException {
		WaveData waveFile = WaveData.create(new BufferedInputStream(new FileInputStream(file)));
		ALHelper.setBuffer(bufferID, waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
	}

	public static String getExtension(File file) {
		String[] parts = file.getName().split("\\.");
		if (parts.length <= 1)
			return null;
		return parts[parts.length - 1].toLowerCase();
	}

	public static void clear() {
		for (int i = 0; i < buffers.size(); i++) {
			if (buffers.get(i) != null)
				buffers.get(i).destroy();
		}
	}

}

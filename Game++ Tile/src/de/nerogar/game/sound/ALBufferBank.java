package de.nerogar.game.sound;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.OpenALException;
import org.lwjgl.util.WaveData;

import com.jcraft.oggdecoder.OggData;
import com.jcraft.oggdecoder.OggDecoder;

public class ALBufferBank {

	public static HashMap<String, ALBuffer> buffers;
	private static OggDecoder oggDecoder = new OggDecoder();

	static {
		buffers = new HashMap<String, ALBuffer>();
	}

	public static void addSound(String filename) throws IOException, LWJGLException, OpenALException {
		if (!SoundManager.alCreated) SoundManager.createAL();
		File file = new File("res/sound/" + filename);

		int id = ALHelper.genBuffers();

		// Datei in den Buffer laden
		String format = getExtension(file);
		switch (format) {
		case "wav":
			setWaveFile(id, file);
			break;
		case "ogg":
			setVorbisFile(id, file);
			break;
		default:
			System.out.println("did not recognize extension for: " + file.getName());
			throw new IOException();
		}

		int size = ALHelper.getBufferSize(id);
		int channels = ALHelper.getBufferChannels(id);
		buffers.put(file.getName(), new ALBuffer(id, size, channels));
	}

	public static ALBuffer getSound(String filename) throws OpenALException, IOException, LWJGLException {
		if (!buffers.containsKey(filename))
			addSound(filename);
		return buffers.get(filename);
	}

	public static ALBuffer[] getBuffers(String[] filenames) throws OpenALException, IOException, LWJGLException {
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

	private static void setVorbisFile(int bufferID, File file) throws IOException {
		// Decode OGG into PCM
		InputStream inputStream = new FileInputStream(file);
		OggData oggData = oggDecoder.getData(inputStream);

		// Load PCM data into buffer
		ALHelper.setBuffer(bufferID, oggData.channels > 1 ? AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_MONO16, oggData.data, oggData.rate);

		inputStream.close();
	}

	/*
	 * private static void setVorbisFile3(int bufferID, File file) throws IOException {
	 * 
	 * FileInputStream vorbisStream = new FileInputStream(file); byte[] data = new byte[(int) file.length()]; vorbisStream.read(data); vorbisStream.close(); ByteBuffer buffer = BufferUtils.createByteBuffer(data.length); buffer.put(data); buffer.flip();
	 * 
	 * ALHelper.setBuffer(bufferID, AL10.AL_FORMAT_VORBIS_EXT, buffer, buffer.capacity()); }
	 * 
	 * private static void setVorbisFile2(int bufferID, File file) throws IOException { FileInputStream vorbisStream = new FileInputStream(file); FileChannel channel = vorbisStream.getChannel(); ByteBuffer buffer = ByteBuffer.allocate((int) channel.size()); channel.read(buffer); channel.close(); vorbisStream.close(); buffer.flip(); ALHelper.setBuffer(bufferID, AL10.AL_FORMAT_VORBIS_EXT, buffer, buffer.capacity()); }
	 */

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

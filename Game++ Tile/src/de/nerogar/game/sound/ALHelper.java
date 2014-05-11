package de.nerogar.game.sound;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.*;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCdevice;
import org.lwjgl.openal.EFX10;
import org.lwjgl.openal.OpenALException;

import de.nerogar.game.Vector;

public class ALHelper {

	public static int ALC_MONO_SOURCES = -1;
	public static int ALC_STEREO_SOURCES = -1;
	public static int ALC_FREQUENCY = -1;

	public static void play(int sourceID) throws OpenALException {
		alSourcePlay(sourceID);
		checkAndThrowALError();
	}

	public static void stop(int sourceID) throws OpenALException {
		alSourceStop(sourceID);
		checkAndThrowALError();
	}

	public static void pause(int sourceID) throws OpenALException {
		alSourcePause(sourceID);
		checkAndThrowALError();
	}

	public static int genSources() throws OpenALException {
		IntBuffer ib = BufferUtils.createIntBuffer(1);
		alGenSources(ib);
		checkAndThrowALError();
		return ib.get(0);
	}

	public static int genBuffers() throws OpenALException {
		IntBuffer ib = BufferUtils.createIntBuffer(1);
		alGenBuffers(ib);
		checkAndThrowALError();
		return ib.get(0);
	}

	public static void setBuffer(int bufferID, int format, ByteBuffer data, int samplerate) throws OpenALException {
		alBufferData(bufferID, format, data, samplerate);
		checkAndThrowALError();
	}

	public static void bindBufferToSource(int bufferID, int sourceID) throws OpenALException {
		alSourcei(sourceID, AL_BUFFER, bufferID);
		checkAndThrowALError();
	}

	public static int getBufferSize(int id) {
		int result = AL10.alGetBufferi(id, AL_SIZE);
		checkAndThrowALError();
		return result;
	}
	
	public static int getBufferChannels(int id) {
		int result = AL10.alGetBufferi(id, AL_CHANNELS);
		checkAndThrowALError();
		return result;
	}

	public static void setPosition(int sourceID, Vector position) throws OpenALException {
		alSource3f(sourceID, AL_POSITION, position.getX(), position.getY(), 0);
		checkAndThrowALError();
	}

	public static void setVelocity(int sourceID, Vector velocity) throws OpenALException {
		alSource3f(sourceID, AL_VELOCITY, velocity.getX(), velocity.getY(), 0);
		checkAndThrowALError();
	}

	public static void setLooping(int sourceID, boolean looping) throws OpenALException {
		if (looping)
			alSourcei(sourceID, AL_LOOPING, AL_TRUE);
		else
			alSourcei(sourceID, AL_LOOPING, AL_FALSE);
		checkAndThrowALError();
	}

	public static void setPitch(int sourceID, float pitch) throws OpenALException {
		alSourcef(sourceID, AL_PITCH, pitch);
		checkAndThrowALError();
	}

	public static void setGain(int sourceID, float gain) throws OpenALException {
		alSourcef(sourceID, AL_GAIN, gain);
		checkAndThrowALError();
	}

	public static void setOffset(int sourceID, int bufferSize, float offset) throws OpenALException {
		alSourcei(sourceID, AL_BYTE_OFFSET, (int) (offset * bufferSize));
		checkAndThrowALError();
	}

	public static int getByteOffset(int sourceID) throws OpenALException {
		int result = alGetSourcei(sourceID, AL_BYTE_OFFSET);
		checkAndThrowALError();
		return result;
	}

	public static int getSourceState(int sourceID) throws OpenALException {
		int result = alGetSourcei(sourceID, AL_SOURCE_STATE);
		checkAndThrowALError();
		return result;
	}
	
	public static void setRolloffFactor(int sourceID, float factor) {
		alSourcef(sourceID, AL_ROLLOFF_FACTOR, factor);
	}

	public static boolean initVorbisExtension() {
		if (alIsExtensionPresent("AL_EXT_vorbis")) {
			checkAndThrowALError();
			return true;
		} else {
			checkAndThrowALError();
			return false;
		}
	}

	public static void setListener(Vector position, Vector velocity, float[] orientationAt, float[] orientationUp) {
		alListener3f(AL_POSITION, position.getX(), position.getY(), SoundManager.CAMERA_HEIGHT);
		checkAndThrowALError();
		alListener3f(AL_VELOCITY, velocity.getX(), velocity.getY(), 0);
		checkAndThrowALError();
		FloatBuffer ori = BufferUtils.createFloatBuffer(6);
		ori.put(0, orientationAt[0]);
		ori.put(1, orientationAt[1]);
		ori.put(2, orientationAt[2]);
		ori.put(3, orientationUp[0]);
		ori.put(4, orientationUp[1]);
		ori.put(5, orientationUp[2]);
		alListener(AL_ORIENTATION, ori);
		//alListenerf(AL_REFERENCE_DISTANCE, 0f);
		checkAndThrowALError();
	}

	public static void destroySource(int sourceID) throws OpenALException {
		IntBuffer intBuffer = BufferUtils.createIntBuffer(1);
		intBuffer.put(0, sourceID);
		alDeleteSources(intBuffer);
		checkAndThrowALError();
	}

	public static void destroyBuffer(int bufferID) throws OpenALException {
		IntBuffer intBuffer = BufferUtils.createIntBuffer(1);
		intBuffer.put(0, bufferID);
		alDeleteBuffers(intBuffer);
		checkAndThrowALError();
	}

	public static void readDeviceAttributes() {

		ALCdevice device = AL.getDevice();

		IntBuffer buffer = BufferUtils.createIntBuffer(1);

		ALC10.alcGetInteger(device, ALC10.ALC_ATTRIBUTES_SIZE, buffer);
		checkAndThrowALError();

		int length = buffer.get(0);
		buffer = BufferUtils.createIntBuffer(length);

		ALC10.alcGetInteger(device, ALC10.ALC_ALL_ATTRIBUTES, buffer);
		checkAndThrowALError();

		for (int i = 0; i + 1 < buffer.limit(); i += 2) {
			if (buffer.get(i) == ALC11.ALC_MONO_SOURCES) {
				ALHelper.ALC_MONO_SOURCES = buffer.get(i + 1);
				System.out.println("ALC_MONO_SOURCES: " + buffer.get(i + 1));
			} else if (buffer.get(i) == ALC11.ALC_STEREO_SOURCES) {
				ALHelper.ALC_STEREO_SOURCES = buffer.get(i + 1);
				System.out.println("ALC_STEREO_SOURCES: " + buffer.get(i + 1));
			} else if (buffer.get(i) == ALC10.ALC_FREQUENCY) {
				ALHelper.ALC_FREQUENCY = buffer.get(i + 1);
				System.out.println("ALC_FREQUENCY: " + buffer.get(i + 1));
			} else if (buffer.get(i) == AL10.AL_BUFFER) {
				System.out.println("AL_BUFFER: " + buffer.get(i + 1));
			} else if (buffer.get(i) == ALC10.ALC_REFRESH) {
				System.out.println("ALC_REFRESH: " + buffer.get(i + 1));
			} else if (buffer.get(i) == EFX10.ALC_MAX_AUXILIARY_SENDS) {
				System.out.println("ALC_MAX_AUXILIARY_SENDS: " + buffer.get(i + 1));
			} else {
				System.out.println("unspecified: " + buffer.get(i) + " > " + buffer.get(i + 1));
			}

		}
		//System.out.println("Buffer content: "+buffer.get(0));

	}

	public static String getALErrorString(int err) {
		switch (err) {
		case AL_NO_ERROR:
			return "AL_NO_ERROR";
		case AL_INVALID_NAME:
			return "AL_INVALID_NAME";
		case AL_INVALID_ENUM:
			return "AL_INVALID_ENUM";
		case AL_INVALID_VALUE:
			return "AL_INVALID_VALUE";
		case AL_INVALID_OPERATION:
			return "AL_INVALID_OPERATION";
		case AL_OUT_OF_MEMORY:
			return "AL_OUT_OF_MEMORY";
		default:
			return "No such error code";
		}
	}

	/*
	 * public static String getALCErrorString(int err) { switch (err) { case ALC_NO_ERROR: return "AL_NO_ERROR"; case ALC_INVALID_DEVICE: return "ALC_INVALID_DEVICE"; case ALC_INVALID_CONTEXT: return "ALC_INVALID_CONTEXT"; case ALC_INVALID_ENUM: return "ALC_INVALID_ENUM"; case ALC_INVALID_VALUE: return "ALC_INVALID_VALUE"; case ALC_OUT_OF_MEMORY: return "ALC_OUT_OF_MEMORY"; default: return "no such error code"; } }
	 */

	public static void checkAndThrowALError() throws OpenALException {
		int error = alGetError();
		if (error != AL_NO_ERROR) {
			throw new OpenALException(getALErrorString(error).toString());
		}
	}
}

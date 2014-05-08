package de.nerogar.game;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class RenderEngine {

	DisplayMode displayMode;

	public void init(int width, int height) {
		try {
			DisplayMode[] displayModes = Display.getAvailableDisplayModes();

			for (DisplayMode mode : displayModes) {
				//boolean flag1 = mode.getFrequency() == Display.getDisplayMode().getFrequency();
				boolean flag2 = mode.getBitsPerPixel() == Display.getDisplayMode().getBitsPerPixel();
				boolean flag3 = mode.getHeight() == height;
				boolean flag4 = mode.getWidth() == width;

				if (flag2 && flag3 && flag4) {
					Display.setDisplayMode(mode);
					displayMode = mode;
				}

			}

			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity(); // Reset The Projection Matrix
		glOrtho(0.0D, displayMode.getWidth(), displayMode.getHeight(), 0.0D, 1f, -1f);
		glMatrixMode(GL_MODELVIEW);

	}
}

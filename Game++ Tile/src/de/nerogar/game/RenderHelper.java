package de.nerogar.game;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.glBlendFuncSeparate;

public class RenderHelper {

	public static void renderQuad(int x, int y, int width, int height) {
		glBegin(GL_QUADS);

		glTexCoord2f(0, 0);
		glVertex3f(x, y, -1);
		glTexCoord2f(0, 1);
		glVertex3f(x, y + height, -1);
		glTexCoord2f(1, 1);
		glVertex3f(x + width, y + height, -1);
		glTexCoord2f(1, 0);
		glVertex3f(x + width, y, -1);

		glEnd();

	}

	public static void renderLine(float x1, float y1, float x2, float y2, float width) {
		enableAlphaMask();
		glDisable(GL_TEXTURE_2D);
		glLineWidth(width);
		glBegin(GL_LINES);
		glVertex2f(x1, y1);
		glVertex2f(x2, y2);

		glEnd();
		glEnable(GL_TEXTURE_2D);
	}

	public static void enableAlphaAdd() {
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);
	}

	public static void enableAlphaMask() {
		glEnable(GL_BLEND);
		glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE);
	}

	public static void disableAlpha() {
		glDisable(GL_BLEND);
	}
}

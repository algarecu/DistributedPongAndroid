package mc.pong.utils;

import android.graphics.Rect;

public class Pad {

	final int SCREEN_WIDTH = 300;
	final int SCREEN_HEIGHT = 420;

	// This means the real attribute of bats.
	public final int batLength = 75;
	public final int batHeight = 10;

	public int topBatY = 20;
	public int bottomBatX = (SCREEN_WIDTH / 2) - (batLength / 2);
	public int bottomBatY = 400;

	// To ease calculation, we always consider bat as a rectangle, so use
	// horiLength/vertLength
	// to denote the horizontal and vertical length of rectangles.
	public int horiLength;
	public int vertLength;
	final int batSpeed = 3;

	int direction = -1;

	public Pad() {
		setDirection(3);
	}

	public void moveLeft() {
		if (bottomBatX > 0)
			bottomBatX -= batSpeed;
	}

	public void moveRight() {
		if (bottomBatX < SCREEN_WIDTH - horiLength)
			bottomBatX += batSpeed;
	}

	public boolean isHorizontal() {
		return direction == 1 || direction == 3;
	}

	public void moveUp() {
		if (bottomBatY >= 0)
			bottomBatY -= batSpeed;
	}

	public void moveDown() {
		if (bottomBatY <= SCREEN_HEIGHT - vertLength)
			bottomBatY += batSpeed;
	}

	public Rect getRect() {
		return new Rect(bottomBatX, bottomBatY, bottomBatX + horiLength,
				bottomBatY + vertLength);
	}

	public void setDirection(int dir) {
		if (direction != dir) {
			direction = dir;
			if (direction % 2 == 0) {
				horiLength = batHeight;
				vertLength = batLength;
				bottomBatY = (SCREEN_HEIGHT - batLength) / 2;
				if (direction == 0) {
					bottomBatX = 0;
				} else {
					bottomBatX = SCREEN_WIDTH - horiLength;
				}
			} else {
				bottomBatX = (SCREEN_WIDTH / 2) - (batLength / 2);
				horiLength = batLength;
				vertLength = batHeight;
				if (direction == 1) {
					bottomBatY = 20;
				} else {
					bottomBatY = 400;
				}
			}
		}
	}

	public int getBottomX() {
		return bottomBatX;
	}

	public int getDirection() {
		return direction;
	}
}

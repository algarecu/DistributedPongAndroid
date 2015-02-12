package mc.pong.game;

import mc.pong.utils.Pad;

public class AccelerometerMapping {
	Pad pad;
	final int SCREEN_WIDTH = 300;
	final int SCREEN_HEIGHT = 420;
	int oldValueX = (SCREEN_WIDTH / 2) - (75 / 2),
			oldValueY = (SCREEN_HEIGHT / 2) - (75 / 2);

	public AccelerometerMapping(Pad pad) {
		this.pad = pad;
	}

	public void generateMap(float accValue) {
		int bottomBatX = pad.bottomBatX, bottomBatY = pad.bottomBatY, batLength = pad.batLength;
		oldValueX = bottomBatX;
		oldValueY = bottomBatY;
		float newCoordinate;

		if (pad.isHorizontal()) {
			if ((oldValueX > SCREEN_WIDTH / 2)) {
				if (((int) (newCoordinate = (oldValueX - oldValueX
						* (accValue / 7))) <= SCREEN_WIDTH - batLength)
						&& ((newCoordinate) >= 0))
					bottomBatX = (int) newCoordinate;
			} else if ((oldValueX <= SCREEN_WIDTH / 2)
					&& (oldValueX > SCREEN_WIDTH / 4)) {
				if (((int) (newCoordinate = (oldValueX - oldValueX
						* (accValue / 5))) <= SCREEN_WIDTH - batLength)
						&& ((newCoordinate) >= 0))
					bottomBatX = (int) newCoordinate;
			} else if ((oldValueX <= SCREEN_WIDTH / 4) && (oldValueX > 20)) {
				if (((int) (newCoordinate = (oldValueX - oldValueX
						* (accValue / 3))) <= SCREEN_WIDTH - batLength)
						&& ((newCoordinate) >= 0))
					bottomBatX = (int) newCoordinate;
			} else if ((oldValueX <= 20) && (oldValueX >= 0)) {
				if (oldValueX <= 10) {
					float difference = 10 - oldValueX;
					if (((int) (newCoordinate = (oldValueX + difference - (oldValueX + difference)
							* (accValue / 2))) <= SCREEN_WIDTH - batLength)
							&& ((newCoordinate) >= 0))
						bottomBatX = (int) newCoordinate;
				} else {
					if (((int) (newCoordinate = (oldValueX - oldValueX
							* (accValue / 2))) <= SCREEN_WIDTH - batLength)
							&& ((newCoordinate) >= 0))
						bottomBatX = (int) newCoordinate;
				}
			}
			pad.bottomBatX = bottomBatX;
		} else {
			if ((oldValueY > SCREEN_HEIGHT / 2)) {
				if (((int) (newCoordinate = (oldValueY + oldValueY
						* (accValue / 7))) <= SCREEN_HEIGHT - batLength)
						&& ((newCoordinate) >= 0))
					bottomBatY = (int) newCoordinate;
			} else if ((oldValueY <= SCREEN_HEIGHT / 2)
					&& (oldValueY > SCREEN_HEIGHT / 4)) {
				if (((int) (newCoordinate = (oldValueY + oldValueY
						* (accValue / 5))) <= SCREEN_HEIGHT - batLength)
						&& ((newCoordinate) >= 0))
					bottomBatY = (int) newCoordinate;
			} else if ((oldValueY <= SCREEN_HEIGHT / 4) && (oldValueY > 20)) {
				if (((int) (newCoordinate = (oldValueY + oldValueY
						* (accValue / 3))) <= SCREEN_HEIGHT - batLength)
						&& ((newCoordinate) >= 0))
					bottomBatY = (int) newCoordinate;
			} else if ((oldValueY <= 20) && (oldValueY >= 0)) {
				if (oldValueY <= 10) {
					float difference = 10 - oldValueY;
					if (((int) (newCoordinate = (oldValueY + difference + (oldValueY + difference)
							* (accValue / 2))) <= SCREEN_HEIGHT - batLength)
							&& ((newCoordinate) >= 0))
						bottomBatY = (int) newCoordinate;
				} else {
					if (((int) (newCoordinate = (oldValueY + oldValueY
							* (accValue / 2))) <= SCREEN_HEIGHT - batLength)
							&& ((newCoordinate) >= 0))
						bottomBatY = (int) newCoordinate;
				}
			}
			pad.bottomBatY = bottomBatY;
		}

	}

}

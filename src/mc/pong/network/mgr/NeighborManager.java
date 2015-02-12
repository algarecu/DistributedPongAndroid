package mc.pong.network.mgr;

import java.util.Calendar;
import java.util.Random;

public class NeighborManager {
	// Everyone has three neighbors at most: left, up, right
	String[] neightbours = new String[4];
	Boolean[] neighbourStates = new Boolean[4];

	public NeighborManager() {
		for (int i = 0; i < 4; ++i) {
			neightbours[i] = "";
			neighbourStates[i] = false;
		}
	}

	public void set(int dir, boolean isExisting) {
			neighbourStates[dir] = isExisting;
	}

	public Boolean exists(int dir) {
		return neighbourStates[dir];
	}

	public String getAddr(int dir) {
		return neightbours[dir];
	}

	public int setAll(String[] neighbourList) {
		int neighbNumber = 0, neighbourSum = 0, padPosition = 0, emptyPos = 0;
		for (int i = 0; i < 4; ++i) {
			neightbours[i] = neighbourList[i];
			if (neighbourList[i] == "") {
				neighbourStates[i] = false;
				emptyPos = i;
			} else {
				neighbourStates[i] = true;
				neighbourSum += i;
				++neighbNumber;
			}
		}

		if (neighbNumber == 1) {
			padPosition = (neighbourSum + 2) % 4;
		} else if (neighbNumber == 2) {
			Random ran = new Random(Calendar.getInstance().getTimeInMillis());
			if (ran.nextInt() % 2 == 0)
				padPosition = emptyPos;
			else
				padPosition = 6 - emptyPos - neighbourSum;
		} else {
			padPosition = 6 - neighbourSum;
		}

		return padPosition;
	}
}

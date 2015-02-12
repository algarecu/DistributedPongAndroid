package mc.pong.utils;

import java.io.Serializable;
import java.util.Date;

public class PingPongInfo implements Serializable {
	public int id;
	public Date genDate;
	int posX;
	int posY;
	int velocX;
	int velocY;

	public PingPongInfo(int id, int posX, int posY, int velocX, int velocY,
			Date genDate) {
		this.id = id;
		this.genDate = genDate;
		this.posX = posX;
		this.posY = posY;
		this.velocX = velocX;
		this.velocY = velocY;
	}
}

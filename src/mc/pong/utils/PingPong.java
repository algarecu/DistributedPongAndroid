package mc.pong.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

// Wall
//  0  means left (to your ping pong pad)
//  1  means up
//  2  means right

public class PingPong {
	public int id;
	final int ballSize = 10;
	final int SCREEN_WIDTH = 300;
	final int SCREEN_HEIGHT = 420;
	final int THRESHOLD = 50;
	public int posX;
	public int posY;
	public int velocX;
	public int velocY;
	public Date genDate;
	int willHit;
	boolean isLocal = true;
	int hasHitPad = 10;
	int color;
	int direction = 3;
	DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	public PingPong(int id, int posX, int posY, int velocX, int velocY,
			Date genDate) {
		this.id = id;
		this.posX = posX;
		this.posY = posY;
		this.velocX = velocX;
		this.velocY = velocY;
		this.genDate = genDate;

		isLocal = true;
		getRandomColor();
		calculateDest();
	}

	public PingPong(int id) {

		// Date genDate = new Date();
		this(id, 100, 100, 3, 3, new Date());

	}

	public PingPong(PingPongInfo info) {
		this(info.id, info.posX, info.posY, info.velocX, info.velocY,
				new Date());
	}

	public void getRandomColor() {
		switch (id) {
		case 0:
			color = 0x00FF00;
			break;
		case 1:
			color = 0xFF0000;
			break;
		case 2:
			color = 0x0000FF;
			break;
		case 3:
			color = 0xFFFF00;
			break;
		case 4:
			color = 0x00FFFF;
			break;
		case 5:
			color = 0xFF00FF;
			break;
		case 6:
			color = 0xFFFFFF;
			break;
		default:
			color = 0x0000ea % 0x1000000;
		}
	}

	public Rect getRect() {
		return new Rect(posX, posY, posX + ballSize, posY + ballSize);
	}

	public void setBallInfo(PingPongInfo info) {
		posX = info.posX;
		posY = info.posY;
		velocX = info.velocX;
		velocY = info.velocY;
		calculateDest();
	}

	public PingPongInfo getBallInfo(int neighbourDir) {
		if (neighbourDir == 0) {
			return new PingPongInfo(id, posX + SCREEN_WIDTH, posY, velocX,
					velocY, genDate);
		} else if (neighbourDir == 1) {
			return new PingPongInfo(id, SCREEN_WIDTH - posX, -posY, -velocX,
					-velocY, genDate);
		} else {
			return new PingPongInfo(id, posX - SCREEN_WIDTH, posY, velocX,
					velocY, genDate);
		}
	}

	public void updatePosition() {
		posX += velocX;
		posY += velocY;
	}

	public boolean collideLRWall() {
		if (posX > SCREEN_WIDTH || posX < 0)
			return true;
		else
			return false;
	}

	public boolean collideUDWall() {
		if (posY < 0 || posY > SCREEN_HEIGHT)
			return true;
		else
			return false;
	}

	public boolean collideFloor() {
		switch (direction) {
		case 0:
			return posX < 0;
		case 1:
			return posY < 0;
		case 2:
			return posX > SCREEN_WIDTH;
		default:
			return posY > SCREEN_HEIGHT;
		}
	}

	public boolean collidePad(Rect padRect) {

		if (hasHitPad == 0 && posX > padRect.left-10 && posX < padRect.right
				&& posY > padRect.top -10 && posY < padRect.bottom) {
			hasHitPad = 5;
			return true;
		} else {
			hasHitPad = Math.max(hasHitPad - 1, 0);
			return false;
		}
	}

	public boolean isLocal() {
		return isLocal;
	}

	public void show() {
		isLocal = true;
	}

	public void hide() {
		isLocal = false;
	}

	public void reboundVert() {
		velocY *= -1;
		calculateDest();
	}

	public void reboundHori() {
		velocX *= -1;
		calculateDest();
	}

	public void reset() {
		posX = 100;
		posY = 100;
		velocX = 3;
		velocY = 2;
		calculateDest();
	}

	// In fact here I just use the current position and speed of the ball to
	// calculate the
	// predicated hitting wall of the ball. Every time the position or dir of
	// speed of the ball is changed,
	// this will be calculated again.
	public void calculateDest() {
		if (velocX >= 0) {
			if (velocY >= 0) {
				if ((velocY * SCREEN_WIDTH + velocX * posY - velocY * posX)
						/ (float) velocX < SCREEN_HEIGHT)
					willHit = 2;
				else
					willHit = 3;
			} else {
				if ((velocY * SCREEN_WIDTH + velocX * posY - velocY * posX)
						/ (float) velocX > 0)
					willHit = 2;
				else
					willHit = 1;
			}
		} else {
			if (velocY >= 0) {
				if ((velocY + velocX * posY - velocY * posX) / (float) velocX < SCREEN_HEIGHT)
					willHit = 0;
				else
					willHit = 3;
			} else {
				if ((velocY + velocX * posY - velocY * posX) / (float) velocX > 0)
					willHit = 0;
				else
					willHit = 1;
			}
		}
	}

	public int iscloseToDestWall() {
		switch (willHit) {
		case 0:
			if (posX < 0 + THRESHOLD)
				return 0;
			break;
		case 1:
			if (posY < 0 + THRESHOLD)
				return 1;
			break;
		case 2:
			if (posX > SCREEN_WIDTH - THRESHOLD)
				return 2;
			break;
		}
		return -1;
	}

	public boolean isHitDestWall() {
		switch (willHit) {
		case 0:
			if (posX < 0)
				return true;
			break;
		case 1:
			if (posY < 0)
				return true;
			break;
		case 2:
			if (posX > SCREEN_WIDTH)
				return true;
			break;
		}
		return false;
	}

	public int getColor() {

		return color;
	}

	public void draw(Canvas canvas, Paint paint) {
		if (isLocal()) {
			paint.setColor(color);
			paint.setAlpha(200);
			canvas.drawRect(getRect(), paint);
		}
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public void reboundByPad() {
		if (direction == 0 || direction == 2)
			reboundHori();
		else
			reboundVert();
	}

}

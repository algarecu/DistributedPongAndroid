package mc.pong.game;

import java.util.ArrayList;
import java.util.List;

import mc.pong.message.state.StateScoreMessage;
import mc.pong.network.NetworkGameState;
import mc.pong.utils.Pad;
import mc.pong.utils.PingPong;
import mc.pong.utils.PingPongInfo;
import mc.pong.utils.PingPongNotify;
import mc.pong.utils.ScoreBoardInfo;
import mc.pong.utils.ScoreBoardManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.KeyEvent;

public class GameState {

	List<PingPong> ballList = new ArrayList<PingPong>();

	List<PingPongNotify> notifyList = new ArrayList<PingPongNotify>();
	// screen width and height

	final int SCREEN_WIDTH = 300;
	final int SCREEN_HEIGHT = 420;

	// int oldValueX = (SCREEN_WIDTH / 2) - (75 / 2), oldValueY = 400;
	int oldValueX, oldValueY;
	AccelerometerMapping map;

	Pad pad;
	NetworkGameState netGameState;
	ScoreBoardManager boards;

	public GameState() {
		boards = new ScoreBoardManager();
		boards.initLocalBoard();
		pad = new Pad();
		map = new AccelerometerMapping(pad);
	}

	public void setStateManager(NetworkGameState netGameState) {
		this.netGameState = netGameState;
		netGameState.setGameState(this);
	}

	// The update method
	public void update() {

//		checkPendingBalls();
//
//		checkPendingNotify();
//
//		checkPendingScore();
//
//		checkDirection();


		for (PingPong currentBall : ballList) {

			if (currentBall.isLocal()) {

				currentBall.updatePosition();

				// If the ball is close to wall should notify, 0 means left, 1
				// means up, 2 means right, -1 means not is close to any wall
				int destWall = currentBall.iscloseToDestWall();
				if (destWall != -1) {
					boolean remainsLocal = netGameState.notifyBall(currentBall,
							destWall);
					if (remainsLocal == false) {
						currentBall.hide();
						return;
					}
				}

				// The ball falls!
				if (currentBall.collideFloor()) {
					currentBall.reset();
					boards.minusMyScore();
					netGameState.notifyScore(boards.getMyInfo());
				} else {
					if (currentBall.collidePad(pad.getRect()))
						currentBall.reboundByPad();
					
					// Collide with left or right
					else if (currentBall.collideLRWall())
						currentBall.reboundHori();
					// Collide with bats
					else if (currentBall.collideUDWall())
						currentBall.reboundVert();
				}
			}
		}
	}

	synchronized public void checkDirection(int direction) {
		//int direction = netGameState.getPadDirection();
		
		if (direction != pad.getDirection()) {
			pad.setDirection(direction);
		}
		for (PingPong ball : ballList) {
			ball.setDirection(direction);
		}
	}

	synchronized public void checkPendingScore( ScoreBoardInfo  info) {
		//List<ScoreBoardInfo> notify = netGameState.getPendingScore();
		//for (ScoreBoardInfo info : notify) {
		boards.updateScoreBoard(info);
		//}
		//netGameState.clearPendingScore();
	}

	synchronized public void checkPendingNotify(int dir) {
		//List<Integer> notify = netGameState.getPendingNotify();
		notifyList.add(new PingPongNotify(dir));
	}

	synchronized public void checkPendingBalls(PingPongInfo ballInfo) {
			for (PingPong ball : ballList) {
				if (ball.isLocal() == false) {
					if (ball.id == ballInfo.id) {
						ball.setBallInfo(ballInfo);
						ball.show();
						return ;
					}
				}
			// Those ball infos that are still left must be new ball. Creating
			// new balls
		}
			ballList.add(new PingPong(ballInfo));
	}

	public boolean keyPressed(int keyCode, KeyEvent msg) {
		if (pad.isHorizontal()) {
			if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) // left
				pad.moveLeft();

			if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
				pad.moveRight();
		} else {
			if (keyCode == KeyEvent.KEYCODE_DPAD_UP) // left
				pad.moveUp();

			if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
				pad.moveDown();
		}

		if (keyCode == KeyEvent.KEYCODE_ENTER)
			addNewBall();

		return true;
	}

	// Touch controls
	public boolean touchEvent(float newCoordinateX, float newCoordinateY) {
		int bottomBatX = pad.bottomBatX, bottomBatY = pad.bottomBatY, batLength = pad.batLength, topBatY = pad.topBatY, batHeight = pad.batHeight;
		oldValueX = bottomBatX;
		oldValueY = bottomBatY;
		if (pad.isHorizontal()) {
			if (((oldValueX + batLength) >= (int) newCoordinateX)
					&& ((int) newCoordinateX <= SCREEN_WIDTH - batLength / 2)
					&& ((newCoordinateX - batLength / 2) >= 0)
					&& (newCoordinateY >= (SCREEN_HEIGHT - topBatY) && newCoordinateY <= (SCREEN_HEIGHT
							- topBatY + batHeight * 4)))
				pad.bottomBatX = ((int) newCoordinateX - batLength / 2);

		} else {

			if (((oldValueY - batLength / 2) <= (int) newCoordinateY)
					&& ((int) newCoordinateY <= SCREEN_WIDTH + batLength)
					&& ((newCoordinateY + batLength) >= 0)
					&& (newCoordinateX <= 3 * pad.batHeight))
				pad.bottomBatY = ((int) newCoordinateY);
		}
		return true;
	}

	// Accelerometer controls
	public boolean accEvent(float accValueX, float accValueY) {
		if (pad.isHorizontal()) {
			map.generateMap(accValueX);
		} else {
			map.generateMap(accValueY);
		}
		return true;
	}

	private void addNewBall() {
		int maxBallID = 0;
		for (PingPong ball : ballList) {
			if (ball.id > maxBallID)
				maxBallID = ball.id;
		}
		ballList.add(new PingPong(++maxBallID));
	}

	// the draw method
	public void draw(Canvas canvas, Paint paint) {

		// Clear the screen
		canvas.drawRGB(20, 20, 20);

		// draw the ball
		for (PingPong currentBall : ballList) {
			currentBall.draw(canvas, paint);
		}

		// Update ball coming notify..
		int size = notifyList.size();
		for (int i = 0; i < size; ++i) {
			PingPongNotify notify = notifyList.get(i);
			if (notify.hasExpire()) {
				notifyList.remove(notify);
				--i;
				--size;
			} else
				notify.draw(canvas, paint);
		}

		// Update score boards
		boards.draw(canvas, paint);

		paint.setARGB(200, 0, 200, 0);
		// draw the bats
		canvas.drawRect(pad.getRect(), paint); // bottom
												// bat

	}

}
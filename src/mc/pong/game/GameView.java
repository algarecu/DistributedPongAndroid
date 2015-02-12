package mc.pong.game;

import mc.pong.network.NetworkGameState;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	private GameThread thread;

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);

		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		setFocusable(true);

		thread = new GameThread(holder, context, new Handler());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent msg) {
		return thread.getGameState().keyPressed(keyCode, msg);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float xPosition1 = 0;
		float yPosition1 = 0;

		xPosition1 = event.getX();
		yPosition1 = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			thread.getGameState().touchEvent(xPosition1, yPosition1);
			break;
		}
		return true;
	}

	// Implemented as part of the SurfaceHolder.Callback interface
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// Mandatory, just swallowing it for this example

	}

	// Implemented as part of the SurfaceHolder.Callback interface
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread.start();
	}

	public void accEvents(float x, float y) {
		// if (!onTouchEvent(event))
		thread.getGameState().accEvent(x, y);
	}

	// Implemented as part of the SurfaceHolder.Callback interface
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		thread.stop();
	}

	public void setStateManager(NetworkGameState stateManager) {
		thread.setStateManager(stateManager);
	}

}

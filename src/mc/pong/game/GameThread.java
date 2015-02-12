package mc.pong.game;

import mc.pong.network.NetworkGameState;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Process;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

	/** Handle to the surface manager object we interact with */
	private SurfaceHolder surfaceHolder;
	private Paint paint;
	private GameState state;

	public GameThread(SurfaceHolder surfaceHolder, Context context,
			Handler handler) {
		this.surfaceHolder = surfaceHolder;
		paint = new Paint();
		state = new GameState();
	}

	@Override
	public void run() {
		while (true) {
			Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_DISPLAY);
			Canvas canvas = surfaceHolder.lockCanvas();
			state.update();
			state.draw(canvas, paint);
			surfaceHolder.unlockCanvasAndPost(canvas);
		}
	}

	public GameState getGameState() {
		return state;
	}

	public void setStateManager(NetworkGameState stateManager) {
		// TODO Auto-generated method stub
		state.setStateManager(stateManager);
	}
}

package mc.pong;

import mc.pong.game.GameAccelerometer;
import mc.pong.game.GameView;
import mc.pong.network.NetworkMember;
import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class DistributedPongActivity extends Activity {
	/** Called when the activity is first created. */
	NetworkMember networkMgr;
	static GameView view;
	GameAccelerometer myAccelerometer;
	private WakeLock mWakeLock;
	private PowerManager mPowerManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup);

		findViewById(R.id.button2).setOnClickListener(connectListener);
		findViewById(R.id.button1).setOnClickListener(startListener);
		networkMgr = NetworkMember.getInstance();
		myAccelerometer = new GameAccelerometer(this);
		mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
		mWakeLock = mPowerManager.newWakeLock(
				PowerManager.SCREEN_BRIGHT_WAKE_LOCK, getClass().getName());
	}

	private OnClickListener connectListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			networkMgr
					.startListening(((TextView) findViewById(R.id.listeningPort))
							.getText().toString());
			boolean result = networkMgr
					.connectTo(((TextView) findViewById(R.id.peerIP)).getText()
							.toString());
			if (result == true) {
				setContentView(R.layout.game);
				GameView view = (GameView) findViewById(R.id.gameview);
				view.setStateManager(networkMgr.getStateManager());
				myAccelerometer.registerListener();
			} else {
				Toast.makeText(v.getContext(), "Could not connect to peer!",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	private OnClickListener startListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			networkMgr
					.startListening(((TextView) findViewById(R.id.listeningPort))
							.getText().toString());
			setContentView(R.layout.game);
			view = (GameView) findViewById(R.id.gameview);
			view.setStateManager(networkMgr.getStateManager());
			myAccelerometer.registerListener();
			networkMgr.startGame();
		}
	};

	public void updateAccelerometer(float tx, float ty) {
		// float w = 420;
		// float x = ((w / 2) * tx) + (w / 2);
		view.accEvents(tx, ty);

	}
}

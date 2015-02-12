package mc.pong.game;

import mc.pong.DistributedPongActivity;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class GameAccelerometer extends Activity implements SensorEventListener {

	private SensorManager sensorManager;
	private Sensor sensorAccelerometer;
	private DistributedPongActivity parent;
	private float maximumRange;
	GameState myState;
	accelerometerValue myacc;

	public GameAccelerometer(Context c) {
		parent = (DistributedPongActivity) c;
	}

	public void registerListener() {
		sensorManager = (SensorManager) parent
				.getSystemService(Context.SENSOR_SERVICE);

	//	sensorAccelerometer = sensorManager
	//			.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

	//	maximumRange = sensorAccelerometer.getMaximumRange();

	//	sensorManager.registerListener(this, sensorAccelerometer,
	//			SensorManager.SENSOR_DELAY_NORMAL);
	}

	public void unregisterListener() {
	//	sensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
			return;

		// checking X and Y axis
		float valueX = event.values[0];
		float valueY = event.values[1];
		parent.updateAccelerometer(valueX / maximumRange, valueY / maximumRange);

	}

	public class accelerometerValue {
		private Float value;

		public accelerometerValue(Float value) {
			this.value = value;
		}

		public Float getValue() {

			return value;
		}

		public void setValue(Float value) {

			this.value = value;
		}

	}

}

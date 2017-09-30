package be.snipper.thrillcorder.AccSampler;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import be.snipper.thrillcorder.SensorSampler;

abstract class AccSampler implements SensorSampler, SensorEventListener {

    double mCurrentValue;

    AccSampler(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public double sample() {
        return mCurrentValue;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

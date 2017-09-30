package be.snipper.thrillcorder.AccSampler;

import android.content.Context;
import android.hardware.SensorEvent;

public class ZaccSampler extends AccSampler {

    public ZaccSampler(Context context) {
        super(context);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        mCurrentValue = sensorEvent.values[2];
    }
}

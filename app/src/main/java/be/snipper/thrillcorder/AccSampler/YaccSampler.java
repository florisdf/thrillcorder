package be.snipper.thrillcorder.AccSampler;

import android.content.Context;
import android.hardware.SensorEvent;

public class YaccSampler extends AccSampler {

    public YaccSampler(Context context) {
        super(context);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        mCurrentValue = sensorEvent.values[1];
    }
}

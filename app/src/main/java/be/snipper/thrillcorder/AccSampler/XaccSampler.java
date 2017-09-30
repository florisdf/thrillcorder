package be.snipper.thrillcorder.AccSampler;

import android.content.Context;
import android.hardware.SensorEvent;

public class XaccSampler extends AccSampler {

    public XaccSampler(Context context) {
        super(context);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        mCurrentValue = sensorEvent.values[0];
    }
}

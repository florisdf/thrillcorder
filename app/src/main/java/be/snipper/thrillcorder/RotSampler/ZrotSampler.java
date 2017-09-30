package be.snipper.thrillcorder.RotSampler;

import android.content.Context;

public class ZrotSampler extends RotSampler {

    public ZrotSampler(Context context) {
        super(context);
    }

    @Override
    public double sample() {
        return mOrientationAngles[2];
    }
}

package be.snipper.thrillcorder.RotSampler;

import android.content.Context;

public class YrotSampler extends RotSampler {

    public YrotSampler(Context context) {
        super(context);
    }

    @Override
    public double sample() {
        return mOrientationAngles[1];
    }
}

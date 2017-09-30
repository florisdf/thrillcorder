package be.snipper.thrillcorder.RotSampler;

import android.content.Context;

public class XrotSampler extends RotSampler {

    public XrotSampler(Context context) {
        super(context);
    }

    @Override
    public double sample() {
        return mOrientationAngles[0];
    }
}

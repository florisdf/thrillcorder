package be.snipper.thrillcorder;

/**
 * Interface to describe some measuring device that can be sampled.
 */
public interface SensorSampler {
    /**
     * @return the value of the sampled measurement.
     */
    double sample();
}
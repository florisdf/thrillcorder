package be.snipper.thrillcorder;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Connects a {@code SensorSampler} to a {@code File}. The {@code SensorSampler} will be used to sample a measurement device. The samples will then be written to a {@code File}.
 * @see be.snipper.thrillcorder.SensorSampler
 * @see java.io.File
 */
public class SamplerSaver {
    private final File file;
    private SensorSampler sampler;
    FileOutputStream outputStream;


    /**
     * @param sampler the {@code SensorSampler} which will be used to obtain samples
     * @param file the {@code File} to which the samples will be written in a CSV style
     */
    public SamplerSaver(SensorSampler sampler, File file) {
        this.sampler = sampler;
        this.file= file;
    }

    /**
     * @param x the sample value to save to the file
     */
    public void save(double x) {
        double y = sampler.sample();
        try {
            outputStream = new FileOutputStream(file, true);
            String xStr = "" + x;
            outputStream.write(xStr.getBytes());
            outputStream.write(';');
            String yStr = "" + y;
            outputStream.write(yStr.getBytes());
            outputStream.write('\n');
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

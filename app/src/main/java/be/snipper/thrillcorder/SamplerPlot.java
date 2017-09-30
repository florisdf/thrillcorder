package be.snipper.thrillcorder;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class SamplerPlot {
    private SensorSampler sampler;
    private LineChart lineChart;
    private LineDataSet lineDataSet;
    private List<Entry> entries;
    private String name;


    public SamplerPlot(SensorSampler sampler, LineChart lineChart, String name) {
        this.sampler = sampler;
        this.lineChart = lineChart;
        lineChart.setDrawBorders(false);
        lineChart.getAxisLeft().setDrawLabels(true);
        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.getXAxis().setDrawLabels(false);
        lineChart.getAxisLeft().setAxisMinimum(-20);
        lineChart.getAxisLeft().setAxisMaximum(20);
        lineChart.getLegend().setEnabled(false);
        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);
        this.name = name;
        entries = new ArrayList<>();
    }

    public void plot(double x) {
        double y = sampler.sample();
        if (entries.size() > 100) {
            entries.remove(0);
        }
        entries.add(new Entry((float) x, (float) y));
        lineDataSet = new LineDataSet(entries, name);
        lineDataSet.setDrawValues(false);
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.invalidate(); // refresh
    }
}

package model;

import java.awt.Color;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Plot extends JFrame {
    public Plot(String title, List<Double> x, List<Double> y) {
        super(title);

        // Create dataset  
        XYDataset dataset = createDataset(x, y);

        // Create chart  
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Równanie transportu ciepła",
                "X-Axis", "Y-Axis", dataset);


        //Changes background color  
        XYPlot plot = (XYPlot)chart.getPlot();
        plot.setBackgroundPaint(new Color(255,228,196));


        // Create Panel  
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    private XYDataset createDataset(List<Double> x, List<Double> y) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries series = new XYSeries("");

        for(int i = 0; i < x.size(); i+= 1){

            series.add(x.get(i), y.get(i));

        }

        dataset.addSeries(series);

        return dataset;
    }

}
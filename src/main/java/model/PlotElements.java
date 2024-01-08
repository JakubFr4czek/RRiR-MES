package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.Random;


public class PlotElements extends JFrame {
    public PlotElements(String title, List<List<Double>> x, List<List<Double>> y) {
        super(title);

        // Create dataset
        XYDataset dataset = createDataset(x, y);

        // Create chart
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Elementy",
                "X-Axis", "Y-Axis", dataset);

        // Paint datasets
        List<StandardXYItemRenderer> standardXYItemRenderers = new ArrayList<>();
        Random rng = new Random();
        for(int i = 0; i < x.size(); i += 1) {

            float r = rng.nextFloat();
            float g = rng.nextFloat();
            float b = rng.nextFloat();

            StandardXYItemRenderer standardXYItemRenderer = new StandardXYItemRenderer();
            standardXYItemRenderer.setSeriesPaint(i, new Color(r, g, b));
            standardXYItemRenderers.add(standardXYItemRenderer);

        }

        //Changes background color
        XYPlot plot = (XYPlot)chart.getPlot();
        plot.setBackgroundPaint(new Color(255,228,196));

        for(int i = 0; i < standardXYItemRenderers.size(); i += 1){

            plot.setRenderer(i, standardXYItemRenderers.get(i));

        }


        // Create Panel
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    private XYDataset createDataset(List<List<Double>> x, List<List<Double>> y) {

        XYSeriesCollection dataset = new XYSeriesCollection();

        for(int i = 0; i < x.size(); i += 1) {

            XYSeries series = new XYSeries("" + i);

            for (int j = 0; j < x.size(); j += 1) {

                series.add(x.get(i).get(j), y.get(i).get(j));

            }

            dataset.addSeries(series);

        }

        return dataset;
    }

}
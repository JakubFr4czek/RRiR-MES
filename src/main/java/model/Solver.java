package model;

import org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.linear.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static java.lang.Math.*;

public class Solver {

    public final int integrationPointCount = 30; // > 10
    private final UnivariateIntegrator integrator;
    private double h;
    private int elements;

    Solver(){

        this.integrator = new IterativeLegendreGaussIntegrator(integrationPointCount, 1e-12, 1e-12);

    }

    public void solve(int elements){

        this.elements = elements;
        this.h = 2.0f / elements;

        RealMatrix equationMatrix = createEquationMatrix();

        RealVector resultVector = createResultVector();

        RealVector coefficients = new LUDecomposition(equationMatrix).getSolver().solve(resultVector);

        List<Double> y = DoubleStream.of(coefficients.toArray()).boxed().collect(Collectors.toList());

        y.add(0.0);

        List<Double> x = createXList();

        System.out.println("----------Macierz A----------");
        printRealMatrix(equationMatrix);
        System.out.println("");
        System.out.println("----------Macierz B----------");
        printRealVector(resultVector);
        System.out.println("");
        System.out.println("----------x----------");
        System.out.println(x);
        System.out.println("");
        System.out.println("----------y----------");
        System.out.println(y);
        System.out.println("");

        SwingUtilities.invokeLater(() -> {
            Plot example = new Plot("Równanie transportu ciepła", x, y);
            example.setSize(800, 400);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });

        plotElements();

    }

    public List<Double> createXList(){

        List<Double> x = new ArrayList<>();

        for(int i = 0; i < elements + 1; i += 1){

            x.add(h * i);

        }

        return x;

    }

    public RealVector createResultVector(){

        RealVector resultVector = new ArrayRealVector(elements);

        for(int i = 0; i < elements - 1; i += 1){

            resultVector.setEntry(i, L(i));


        }

        resultVector.setEntry(elements - 1, 3.0d); //Warunek brzegowy Dirichleta

        return resultVector;
    }

    public RealMatrix createEquationMatrix(){

        //Tworzę tablicę zer
        RealMatrix equationMatrix = new Array2DRowRealMatrix(elements, elements);

        //Uzupełniam wartościami
        for(int i = 0; i < elements; i += 1){

            for(int j = 0; j < elements; j += 1){

                double a = 0.0f;
                double b = 0.0f;

                if(abs(i - j) == 1){ //po bokach przekątnej

                    a = 2.0d * max(0.0d, ( 1.0d * min(i, j)) / elements);
                    b = 2.0d * min(1.0d, ( 1.0d * max(i, j )) / elements);

                }else if(i == j){ //przekątna

                    a = 2.0d * max(0.0d, (i - 1.0d) / elements);
                    b = 2.0d * min(1.0d, (i + 1.0d) / elements);

                }else{

                 equationMatrix.setEntry(i, j, 0.0d);

                 continue;

                }

                equationMatrix.setEntry(i, j, B(i, j, a, b));

            }

        }

        return equationMatrix;

    }

    private double B(int i, int j, double a, double b){

        return this.integrator.integrate(
                Integer.MAX_VALUE,
                x -> e_prim(i, x) * e_prim(j, x),
                a,
                b
        ) - (e(i, 0) * e(j, 0));

    }

    private double L(int i){

        return -20.0d * e(i, 0);

    }

    private double e(int i, double x) {

        if (x < h * (i - 1) || x > h * (i + 1))
            return 0;

        if (x < h * i)
            return x / h - i + 1;

        return -x / h + i + 1;

    }

    private double e_prim(int i, double x) {

        if (x < h * (i - 1) || x > h * (i + 1))
            return 0;

        if (x < h * i)
            return 1 / h;

        return -1 / h;

    }

    private void printRealMatrix(RealMatrix matrix){

        double[][] array = matrix.getData();

        for (double[] innerArray : array) {

            for (double value : innerArray) {

                double x = round(value * 100.0) / 100.0;
                System.out.print(x + " ");

            }
            System.out.println("");

        }

    }

    private void printRealVector(RealVector vector){

        double[] vectorArray = vector.toArray();

        for(double value : vectorArray){

            double x = round(value * 100.0) / 100.0;
            System.out.print(x + " ");

        }
        System.out.println("");

    }

    /*private void plotElements(){

        List<List<Double>> xs = new ArrayList<>();
        List<List<Double>> ys = new ArrayList<>();

        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();

        int n = 100;

        for(double i = 0; i < h; i += h/n){

            x.add(i);
            y.add(e(0, i));

            //System.out.println(i);
            //System.out.println(e(0, i));

        }

        xs.add(x);
        ys.add(y);

        SwingUtilities.invokeLater(() -> {
            PlotElements plotElements = new PlotElements("Elements", xs, ys);
            plotElements.setSize(800, 400);
            plotElements.setLocationRelativeTo(null);
            plotElements.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            plotElements.setVisible(true);
        });

    }*/


    private void plotElements(){

        List<List<Double>> xs = new ArrayList<>();
        List<List<Double>> ys = new ArrayList<>();

        for(int j = 0; j < elements; j += 1) {

            List<Double> x = new ArrayList<>();
            List<Double> y = new ArrayList<>();

            int n = 100;

            for (double i = 0; i < 2.0; i += h / n) {

                x.add(i);
                y.add(e(j, i));
                //y.add(e_prim(j, i));

            }

            xs.add(x);
            ys.add(y);

        }

        SwingUtilities.invokeLater(() -> {
            PlotElements plotElements = new PlotElements("Elements", xs, ys);
            plotElements.setSize(800, 400);
            plotElements.setLocationRelativeTo(null);
            plotElements.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            plotElements.setVisible(true);
        });

    }

}

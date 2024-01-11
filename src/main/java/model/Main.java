package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.*;

import org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.linear.*;


public class Main {


    public static void main(String[] args) {

       Solver solver = new Solver();

       if(args.length == 0){

           //Domyślnie dla 3 elementów
           solver.solve(6);

       } else if (Integer.parseInt(args[0]) < 3) {

           throw new RuntimeException("Liczba elementóœ musi być >= 3");

       } else {

           //Użytkownik podał liczbę elementów
           solver.solve(Integer.parseInt(args[0]));

       }

    }



}
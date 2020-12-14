/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Miguerubsk
 */
public class Configurador {

    private ArrayList<String> Ficheros;
    private ArrayList<Long> Semillas;
    private Integer Iteraciones, TamPoblacion, alfa, beta, greedy;
    private float delta, fi, p, q0;

    public Configurador(String ruta) {
        Ficheros = new ArrayList<>();
        Semillas = new ArrayList<>();

        String linea;
        FileReader f = null;
        try {
            f = new FileReader(ruta);
            BufferedReader b = new BufferedReader(f);

            while ((linea = b.readLine()) != null) {
                String[] split = linea.split("=");
                switch (split[0]) {
                    case "Archivos":
                        String[] vF = split[1].split(" ");
                        for (int i = 0; i < vF.length; i++) {
                            Ficheros.add(vF[i]);
                        }
                        break;
                    case "Semillas":
                        String[] vS = split[1].split(" ");
                        for (int i = 0; i < vS.length; i++) {
                            Semillas.add(Long.parseLong(vS[i]));
                        }
                        break;
                    case "Iteraciones":
                        Iteraciones = Integer.parseInt(split[1]);
                        break;
                    case "Poblacion":
                        TamPoblacion = Integer.parseInt(split[1]);
                        break;
                    case "greedy":
                        greedy = Integer.parseInt(split[1]);
                        break;
                    case "alfa":
                        alfa = Integer.parseInt(split[1]);
                        break;
                    case "beta":
                        beta = Integer.parseInt(split[1]);
                        break;
                    case "q0":
                        q0 = Float.parseFloat(split[1]);
                        break;
                    case "p":
                        p = Float.parseFloat(split[1]);
                        break;
                    case "fi":
                        fi = Float.parseFloat(split[1]);
                        break;
                    case "delta":
                        delta = Float.parseFloat(split[1]);
                        break;
                }
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public ArrayList<String> getFicheros() {
        return Ficheros;
    }

    public ArrayList<Long> getSemillas() {
        return Semillas;
    }

    public Integer getIteraciones() {
        return Iteraciones;
    }

    public int getTamPoblacion() {
        return TamPoblacion;
    }

    public float getDelta() {
        return delta;
    }

    public float getFi() {
        return fi;
    }

    public Integer getAlfa() {
        return alfa;
    }

    public Integer getBeta() {
        return beta;
    }

    public Integer getGreedy() {
        return greedy;
    }

    public float getP() {
        return p;
    }

    public float getQ0() {
        return q0;
    }

}

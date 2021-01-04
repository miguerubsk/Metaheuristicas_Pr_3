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
import java.util.HashMap;

/**
 *
 * @author Miguerubsk
 */
public class Configurador {

    private ArrayList<String> Ficheros;
    private Long Semilla;
    private ArrayList<Integer> alfa, beta;
    private HashMap<String, Double> fInicial;
    private Integer Iteraciones, TamPoblacion;
    private float delta, fi, p, q0;

    public Configurador(String ruta) {
        Ficheros = new ArrayList<String>();
        Semilla = null;
        fInicial = new HashMap<String, Double>();
        this.alfa = new ArrayList<>();
        this.beta = new ArrayList<>();

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

                        Semilla = Long.parseLong(split[1]);

                        break;
                    case "Iteraciones":
                        Iteraciones = Integer.parseInt(split[1]);
                        break;
                    case "Poblacion":
                        TamPoblacion = Integer.parseInt(split[1]);
                        break;
                    case "greedy":
                        String[] vG = split[1].split(" ");
                        for (int i = 0; i < vG.length; i++) {
                            fInicial.put(Ficheros.get(i), Double.parseDouble(vG[i]));
                        }
                        break;
                    case "alfa":
                        String[] vA = split[1].split(" ");
                        for (int i = 0; i < vA.length; i++) {
                            alfa.add(Integer.parseInt(vA[i]));
                        }
                        break;
                    case "beta":
                        String[] vB = split[1].split(" ");
                        for (int i = 0; i < vB.length; i++) {
                            beta.add(Integer.parseInt(vB[i]));
                        }
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

    public Long getSemilla() {
        return Semilla;
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

    public float getP() {
        return p;
    }

    public float getQ0() {
        return q0;
    }

    public ArrayList<Integer> getAlfa() {
        return alfa;
    }

    public ArrayList<Integer> getBeta() {
        return beta;
    }

    public HashMap<String, Double> getfInicial() {
        return fInicial;
    }

}

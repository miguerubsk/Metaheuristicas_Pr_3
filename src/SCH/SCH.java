/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SCH;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import tools.CargaDatos;
import tools.Configurador;

/**
 *
 * @author Miguerubsk
 */
public class SCH {

    private final CargaDatos datos; //Datos para realizar la ejecucion
    private final Configurador config; //Archivo de configuracion
    private final Long semilla; //Semilla para inicializacion del aleatorio
    private Poblacion poblacion;
    private double feromonas[][];
    private double matrizHeuristica[][];
    private int[][] listaVecinosCercanos;

    public SCH(CargaDatos data, Configurador conf, Long seed) {
        this.datos = data;
        this.config = conf;
        this.semilla = seed;
        this.poblacion = new Poblacion(semilla, datos, true, config);
        this.feromonas = new double[datos.getTamMatriz()][datos.getTamMatriz()];
        this.matrizHeuristica = new double[datos.getTamMatriz()][datos.getTamMatriz()];
        crearListaVecinosCercanos();
    }

    public void ejecutar() throws Exception {
        //TODO
        int iteracion = 0;
        while (iteracion < config.getIteraciones()) {
            for (int i = 0; i < poblacion.getTamPoblacion(); i++) {
                //inicializar primera ciudad
            }
        }
    }

    private void crearListaVecinosCercanos() {
        listaVecinosCercanos = new int[datos.getTamMatriz()][config.getTamPoblacion()];
        // For each node of the graph, sort the nearest neighbors by distance
        // and cut the list by the size nn.
        for (int i = 0; i < datos.getTamMatriz(); i++) {
            Integer[] nodeIndex = new Integer[datos.getTamMatriz()];
            Double[] nodeData = new Double[datos.getTamMatriz()];
            for (int j = 0; j < datos.getTamMatriz(); j++) {
                nodeIndex[j] = j;
                nodeData[j] = datos.getMatriz()[i][j];
            }
            // The edge of the current vertex with himself is let as last
            // option to be selected to nearest neighbors list
            nodeData[i] = Collections.max(Arrays.asList(nodeData));
            Arrays.sort(nodeIndex, new Comparator<Integer>() {
                public int compare(final Integer o1, final Integer o2) {
                    return Double.compare(nodeData[o1], nodeData[o2]);
                }
            });
            for (int r = 0; r < config.getTamPoblacion(); r++) {
                listaVecinosCercanos[i][r] = nodeIndex[r];
            }
        }
        
        System.out.println("SCH.SCH.crearListaVecinosCercanos()");
    }
}

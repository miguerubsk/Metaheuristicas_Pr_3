/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SCH;

import java.util.Vector;
import tools.CargaDatos;
import tools.Random;

/**
 *
 * @author Miguerubsk
 */
public class Hormiga {

    private Vector<Integer> sol;
    private int tamSol;
    private double coste;
    private Random aleatorio;
    private CargaDatos datos;
    private boolean marcados[];

    public Hormiga(long semilla, CargaDatos datos) {
        this.datos = datos;
        this.aleatorio = new Random(semilla);
        this.tamSol = this.datos.getTamSolucion();
        this.sol = new Vector<Integer>();
        this.coste = coste(this.datos);
        this.marcados = new boolean[datos.getTamMatriz()];
        for (boolean marcado : marcados) {
            marcado = false;
        }
    }

    /**
     * @brief Función que calcula el coste de la solucion
     * @param matriz matriz de distancias
     * @param tamañoSolucion tamaño de la solucion
     * @return Coste de la solucion
     */
    private double coste(CargaDatos datos) {
        double coste = 0.0;

        for (int i = 0; i < sol.size(); i++) {
            for (int j = i + 1; j < sol.size(); j++) {
                coste += datos.getMatriz()[sol.get(j)][sol.get(i)];
            }
        }

        return coste;
    }

    public void inicializar() {
        sol.removeAllElements();
        for (boolean marcado : marcados) {
            marcado = false;
        }
        int primero = aleatorio.Randint(0, datos.getTamMatriz() - 1);
        sol.add(primero);
        marcados[primero] = true;
    }

    public boolean isMarcado(int cual) {

        return marcados[cual];
    }

    public void setMarcado(int cual) {
        marcados[cual] = true;
    }

    public int getElementoSol(int cual) {
        return sol.get(cual);
    }

    public void setElementoSol(int cual, int ele) {
        sol.set(cual, ele);
    }

    public void setElementoSol(int ele) {
        sol.add(ele);
    }

    public Vector<Integer> getSol() {
        return sol;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SCH;

import tools.CargaDatos;
import tools.Random;

/**
 *
 * @author Miguerubsk
 */
public class Hormiga {

    private Integer sol[];
    private int tamSolMax;
    private int tamSol;
    private double coste;
    private Random aleatorio;
    private CargaDatos datos;
    private boolean marcados[];

    public Hormiga(long semilla, CargaDatos datos) {
        this.datos = datos;
        this.aleatorio = new Random(semilla);
        this.tamSolMax = this.datos.getTamSolucion();
        this.tamSol = 0;
        this.sol = new Integer[datos.getTamSolucion()];
        this.coste = coste(this.datos);
        this.marcados = new boolean[datos.getTamMatriz()];
    }

    /**
     * @brief Función que calcula el coste de la solucion
     * @param matriz matriz de distancias
     * @param tamañoSolucion tamaño de la solucion
     * @return Coste de la solucion
     */
    private double coste(CargaDatos datos) {
        double coste = 0.0;

        for (int i = 0; i < tamSol; i++) {
            for (int j = i + 1; j < tamSol; j++) {
                coste += datos.getMatriz()[sol[j]][sol[i]];
            }
        }

        return coste;
    }

    public void inicializar() {
        for (int i = 0; i < datos.getTamSolucion(); i++) {
            sol[i] = 0;
        }
        for (int i = 0; i < datos.getTamMatriz(); i++) {
            marcados[i] = false;
        }
        
        int primero = aleatorio.Randint(0, datos.getTamMatriz() - 1);
        sol[0] = primero;
        marcados[primero] = true;
        tamSol++;
    }

    public boolean isMarcado(int cual) {
        return marcados[cual];
    }

    public void setMarcado(int cual) {
        marcados[cual] = true;
    }

    public int getElementoSol(int cual) {
        return sol[cual];
    }

    public void setElementoSol(int cual, int ele) {
        sol[cual] = ele;
    }

    public void addElementoSol(int ele) {
        sol[tamSol] = ele;
        tamSol++;
    }

    public Integer[] getSol() {
        return sol;
    }

    void reiniciar() {
        
    }

}

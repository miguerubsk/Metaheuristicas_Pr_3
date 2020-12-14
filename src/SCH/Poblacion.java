/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SCH;

import java.util.Vector;
import tools.CargaDatos;
import tools.Configurador;
import tools.Random;

/**
 *
 * @author Miguerubsk
 */
public class Poblacion {
    
    private Vector<Hormiga> poblacion;
    private int tamPoblacion;
    private long semilla;
    private CargaDatos datos;
    private final Configurador config;
    private Random aleatorio;
    
    public Poblacion(long semilla, CargaDatos datos, boolean generar, Configurador config) {
        this.poblacion = new Vector<Hormiga>();
        this.tamPoblacion = config.getTamPoblacion();
        this.semilla = semilla;
        this.datos = datos;
        this.config = config;
        this.aleatorio = new Random(semilla);
        if (generar) {
            generarPoblacion();
        }
    }
    
    private void generarPoblacion() {
        for (int i = 0; i < config.getTamPoblacion(); i++) {
            Hormiga nuevaHormiga = new Hormiga(semilla + aleatorio.Randint(0, datos.getTamMatriz()), datos);
            poblacion.add(nuevaHormiga);
            tamPoblacion++;
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metaheuristicas_pr_3;

import SCH.SCH;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.CargaDatos;
import tools.Configurador;

/**
 *
 * @author Miguerubsk
 */
public class Metaheuristicas_Pr_3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //Cargamos el archivo de configuracion
        Configurador config = new Configurador("config.txt");

        //Cargamos los ficheros de datos
        ArrayList<CargaDatos> Datos = new ArrayList<>();
        for (int i = 0; i < config.getFicheros().size(); i++) {
            Datos.add(new CargaDatos(config.getFicheros().get(i)));
        }
        
        SCH algoritmo = new SCH();
        try {
            algoritmo.ejecutar(Datos.get(0).getMatriz(), Datos.get(0).getTamMatriz(), Datos.get(0).getTamSolucion(), 100, config.getTamPoblacion(), 19000, 1, 1, 0.95, 0.1, 0.1,0.6);
        } catch (Exception ex) {
            Logger.getLogger(Metaheuristicas_Pr_3.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}

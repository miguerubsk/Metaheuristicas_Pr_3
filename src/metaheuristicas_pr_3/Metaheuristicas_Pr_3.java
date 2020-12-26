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
        
        SCH algoritmo = new SCH(Datos.get(0), config, (long)26522589, 19000);
        try {
            algoritmo.ejecutar();
        } catch (Exception ex) {
            Logger.getLogger(Metaheuristicas_Pr_3.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}

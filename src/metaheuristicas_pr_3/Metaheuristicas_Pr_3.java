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

        for (CargaDatos Dato : Datos) {
            for (Integer alfa : config.getAlfa()) {
                if (alfa == 2) {
                    SCH algoritmo = new SCH(config.getSemilla(), Dato.getNombreFichero(), Dato.getMatriz(), Dato.getTamMatriz(), Dato.getTamSolucion(), config.getIteraciones(), config.getTamPoblacion(), config.getfInicial().get(Dato.getNombreFichero()), alfa, 1, config.getQ0(), config.getP(), config.getFi(), config.getDelta(), config.getTiempo());
                    try {
                        algoritmo.ejecutar();
                    } catch (Exception ex) {
                        Logger.getLogger(Metaheuristicas_Pr_3.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    for (Integer beta : config.getBeta()) {
                        SCH algoritmo = new SCH(config.getSemilla(), Dato.getNombreFichero(), Dato.getMatriz(), Dato.getTamMatriz(), Dato.getTamSolucion(), config.getIteraciones(), config.getTamPoblacion(), config.getfInicial().get(Dato.getNombreFichero()), alfa, beta, config.getQ0(), config.getP(), config.getFi(), config.getDelta(), config.getTiempo());
                        try {
                            algoritmo.ejecutar();
                        } catch (Exception ex) {
                            Logger.getLogger(Metaheuristicas_Pr_3.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

            }
        }
    }

}

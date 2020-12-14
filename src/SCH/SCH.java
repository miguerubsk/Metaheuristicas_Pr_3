/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SCH;

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
    
    public SCH(CargaDatos data, Configurador conf, Long seed){
         this.datos = data;
         this.config = conf;
         this.semilla = seed;
         this.poblacion = new Poblacion(semilla, datos, true, config);
    }
    
    public void ejecutar() throws Exception{
        //TODO
    }
}

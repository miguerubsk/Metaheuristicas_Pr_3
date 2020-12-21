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
public class SCH {

    private final CargaDatos datos; //Datos para realizar la ejecucion
    private final Configurador config; //Archivo de configuracion
    private final Long semilla; //Semilla para inicializacion del aleatorio
    private double feromonas[][];
    private double heuristica[][];
    private Vector<Integer> LRC;
    private Poblacion poblacion;

    public SCH(CargaDatos data, Configurador conf, Long seed, double greedy) {
        this.datos = data;
        this.config = conf;
        this.semilla = seed;
        this.poblacion = new Poblacion(semilla, datos, true, config);
        this.feromonas = new double[datos.getTamMatriz()][datos.getTamMatriz()];
        this.heuristica = new double[datos.getTamMatriz()][datos.getTamMatriz()];
        this.LRC = new Vector<>();

        double fInicial = greedy;
        for (int i = 0; i < config.getTamPoblacion(); i++) {
            for (int j = 0; j < config.getTamPoblacion(); j++) {
                if (i != j) {
                    feromonas[i][j] = fInicial;
                    heuristica[i][j] = datos.getMatriz()[i][j];
                }
            }
        }

    }

    public void ejecutar() throws Exception {
        //TODO
        int iteracion = 0;
        while (iteracion < config.getIteraciones()) {
            poblacion.inicializar();

            for (int comp = 1; comp < datos.getTamSolucion(); comp++) {
                //Para cada hormiga
                for (int h = 0; h < poblacion.getTamPoblacion(); h++) {

                    //vector que almacena las distancias
                    Vector<Double> distancias = new Vector<>(datos.getTamMatriz());
                    for (Double distancia : distancias) {
                        distancia = -1.0;
                    }

                    //Calculamos las distancias de las n componentes candidatas:               
                    for (int i = 0; i < datos.getTamMatriz(); i++) {
                        double d = 0.0;
                        if(!poblacion.getHormiga(h).isMarcado(i)){
                            for (int k = 0; k < comp; k++) {
                                d += datos.getMatriz()[i][poblacion.getHormiga(h).getElementoSol(k)];
                            }
                            distancias.add(d);
                        }
                    }

                    //Calculamos la distancia minima y maxima
                    double mayord = 0, menord = 999999999;
                    for (int i = 0; i < datos.getTamMatriz() - 1; i++) {
                        if (!poblacion.getHormiga(h).isMarcado(i)) {
                            if (distancias.get(i) < menord) {
                                menord = distancias.get(i);
                            } else if (distancias.get(i) > mayord) {
                                mayord = distancias.get(i);
                            }
                        }
                    }

                    //elegimos componentes para LRC
                    for (int i = 0; i < datos.getTamMatriz() - 1; i++) {
                        if (!(poblacion.getHormiga(h).isMarcado(i)) && (distancias.get(i) >= (menord + (config.getDelta() * (mayord - menord))))) {
                            LRC.add(i);
                        }
                    }

                    //ELECCION del ELEMENTO de la LRC a incluir en la solucion 
                    Vector<Double> ferxHeu = new Vector<>(LRC.size());
                    for (Double double1 : ferxHeu) {
                        double1 = 0.0;
                    }

                    //calculo la cantidad total de feromonaxheuristica por cada elemento de la LRC 
                    //respecto de los elementos de la solución
                    for (int i = 0; i < LRC.size(); i++) {
                        for (int j = 0; j < comp; j++) {
                            Double aux = Math.pow(heuristica[j][LRC.get(i)], 1) * Math.pow(feromonas[j][LRC.get(i)], 1);//HAY QUE VARIAR EL BETA Y ALFA
                            ferxHeu.add(aux);
                        }
                    }

                    //calculo del argMax y sumatoria del total de feromonaxHeuristica
                    //(denominador)
                    double denominador = 0.0;
                    double argMax = 0.0;
                    int posArgMax = 0;
                    for (int i = 0; i < LRC.size(); i++) {
                        denominador += ferxHeu.get(i);
                        if (ferxHeu.get(i) > argMax) {
                            argMax = ferxHeu.get(i);
                            posArgMax = LRC.get(i);
                        }
                    }

                    //FUNCION de TRANSICION
                    //vector de probabilidades de transicion
                    int elegido = -1;
                    Vector<Double> prob = new Vector<>(LRC.size());
                    for (Double double1 : prob) {
                        double1 = 0.0;
                    }
                    
                    Random aleatorio = new Random(semilla*iteracion);
                    double q = aleatorio.Randfloat(0, (float) 1.01);  //aleatorio inicial

                    if (0.95 <= q) {  //aplicamos argumento maximo y nos quedamos con el mejor ESTO ES EL q0!!!!!!!!!!!!!
                        elegido = posArgMax;
                    } else {  //aplicamos regla de transicion normal
                        for (int i = 0; i < LRC.size(); i++) {
                            double numerador = ferxHeu.get(i);
                            Double aux = numerador / denominador;
                            prob.add(aux);
                        }

                        //elegimos la componente a añadir buscando en los intervalos de probabilidad                                        
                        double Uniforme = aleatorio.Randfloat(0, 1);  //aleatorio para regla de transición
                        double acumulado = 0.0;
                        for (int i = 0; i < LRC.size(); i++) {
                            acumulado += prob.get(i);
                            if (Uniforme <= acumulado) {
                                elegido = LRC.get(i);
                                break;
                            }
                        }
                    }

                    poblacion.getHormiga(h).setElementoSol(elegido);
                    poblacion.getHormiga(h).setMarcado(elegido);

//                muestraHormiga(hormigas[h]);
                    LRC.clear();

                } //fin agregado una componente a cada hormiga

                //actualización de feromona local, que afecta a todos los ya incluidos en la solActual.
//                for (int h = 0; h < poblacion.getTamPoblacion(); h++) {
//                    for (int i = 0; i < comp; i++) {
//                        feromona[hormigas[h][i]][hormigas[h][comp]] = ((1 - fi) * feromona[hormigas[h][i]][hormigas[h][comp]]) + (fi * fInicial);
//                    }
//                }

            } //fin cuando las hormigas estan completas      
            System.out.println("///////////////////////////////////////////////////////////////////////////////");
                          
            for (int i = 0; i < poblacion.getTamPoblacion(); i++) {
                System.out.println("Hormiga " + i + ". Solucion: " + poblacion.getHormiga(i).getSol().toString() + "\n");
            }
        }
    }
}

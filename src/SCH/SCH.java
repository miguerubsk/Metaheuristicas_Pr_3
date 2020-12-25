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
    private double greedy;
    private double mejorCosteActual;
    private Hormiga mejorHormigaActual;
    private double mejorCosteGlobal;
    private Hormiga s;

    public SCH(CargaDatos data, Configurador conf, Long seed, double greedy) {
        this.datos = data;
        this.config = conf;
        this.semilla = seed;
        this.poblacion = new Poblacion(semilla, datos, true, config);
        this.feromonas = new double[datos.getTamMatriz()][datos.getTamMatriz()];
        this.heuristica = new double[datos.getTamMatriz()][datos.getTamMatriz()];
        this.LRC = new Vector<>();
        this.greedy = greedy;

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
        long tiempo = 0;

        while (iteracion < config.getIteraciones() || tiempo < 600000) {
            long startTime = System.currentTimeMillis();
            poblacion.inicializar();
//            System.out.println("SCH.SCH.ejecutar(): " + iteracion);

            for (int comp = 1; comp < datos.getTamSolucion(); comp++) {
                for (int h = 0; h < poblacion.getTamPoblacion(); h++) {

                    //VECTOR DE DISTANCIAS
                    Vector<Double> distancias = new Vector<>(datos.getTamMatriz());
                    for (int i = 0; i < datos.getTamMatriz(); i++) {
                        distancias.add(-1.0);
                    }

                    //DISTANCIAS DE LAS POSIBLES SOLUCIONES              
                    for (int i = 0; i < datos.getTamMatriz(); i++) {
                        double d = 0.0;
                        if (!poblacion.getHormiga(h).isMarcado(i)) {
                            for (int k = 0; k < comp; k++) {
                                d += datos.getMatriz()[i][poblacion.getHormiga(h).getElementoSol(k)];
                            }
                            distancias.setElementAt(d, i);
                        }
                    }

                    //CALCULAR DISTANCIA MINIMA Y MAXIMA
                    double mayor = 0, menor = 999999999;
                    for (int i = 0; i < datos.getTamMatriz(); i++) {
                        if (!poblacion.getHormiga(h).isMarcado(i)) {
                            if (distancias.get(i) < menor) {
                                menor = distancias.get(i);
                            } else if (distancias.get(i) > mayor) {
                                mayor = distancias.get(i);
                            }
                        }
                    }

                    //RELLENAMOS LA LRC
                    for (int i = 0; i < datos.getTamMatriz(); i++) {
                        if (!(poblacion.getHormiga(h).isMarcado(i)) && (distancias.get(i) >= (menor + (config.getDelta() * (mayor - menor))))) {
                            LRC.add(i);
                        }
                    }

                    //ELECCION DEL ELEMENTO DE LA LRC QUE INCLUIRA LA SOLUCION
                    Vector<Double> ferxHeu = new Vector<>(LRC.size());
                    for (int i = 0; i < LRC.size(); i++) {
                        ferxHeu.add(0.0);
                    }

                    //calculo la cantidad total de feromonaxheuristica por cada elemento de la LRC 
                    //respecto de los elementos de la solución
                    for (int i = 0; i < LRC.size(); i++) {
                        for (int j = 0; j < comp; j++) {
                            Double aux = Math.pow(heuristica[j][LRC.get(i)], 1) * Math.pow(feromonas[j][LRC.get(i)], 1);//HAY QUE VARIAR EL BETA Y ALFA
                            ferxHeu.setElementAt(aux, i);
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
                    int elegido = 0;
                    Vector<Double> prob = new Vector<>(LRC.size());
                    for (int i = 0; i < LRC.size(); i++) {
                        prob.add(0.0);
                    }

                    Random aleatorio = new Random(semilla * iteracion);
                    double q = aleatorio.Randfloat(0, (float) 1.01);  //aleatorio inicial

                    if (0.95 <= q) {  //aplicamos argumento maximo y nos quedamos con el mejor ESTO ES EL q0!!!!!!!!!!!!!
                        elegido = posArgMax;
                    } else {  //aplicamos regla de transicion normal
                        for (int i = 0; i < LRC.size(); i++) {
                            double numerador = ferxHeu.get(i);
                            Double aux = numerador / denominador;
                            prob.setElementAt(aux, i);
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

                    poblacion.getHormiga(h).setElementoSol(comp, elegido);
                    poblacion.getHormiga(h).setMarcado(elegido);

//                muestraHormiga(hormigas[h]);
                    LRC.clear();

                } //fin agregado una componente a cada hormiga

                //actualización de feromona local, que afecta a todos los ya incluidos en la solActual.
                for (int h = 0; h < poblacion.getTamPoblacion(); h++) {
                    for (int i = 0; i < comp; i++) {
                        feromonas[poblacion.getHormiga(h).getElementoSol(i)][poblacion.getHormiga(h).getElementoSol(comp)] = ((1 - 0.1) * feromonas[poblacion.getHormiga(h).getElementoSol(i)][poblacion.getHormiga(h).getElementoSol(comp)]) + (0.1 * greedy);
                    }
                }
            }

            mejorCosteActual = 0;
            for (int i = 0; i < poblacion.getTamPoblacion(); i++) {
                double coste = Coste(poblacion.getHormiga(i).getSol(), datos.getMatriz(), datos.getTamSolucion());
                if (coste > mejorCosteActual) {
                    mejorCosteActual = coste;
                    mejorHormigaActual = poblacion.getHormiga(i);
                }
            }

            if (mejorCosteActual > mejorCosteGlobal) {
                mejorCosteGlobal = mejorCosteActual;
                s = mejorHormigaActual;
            }

            double deltaMejor = mejorCosteActual;
            for (int i = 0; i < datos.getTamSolucion(); i++) {
                for (int j = 0; j < datos.getTamMatriz(); j++) {
                    if (s.getElementoSol(i) != j) {
                        feromonas[s.getElementoSol(i)][j] += (0.1 * deltaMejor);
                        feromonas[j][s.getElementoSol(i)] = feromonas[s.getElementoSol(i)][j];  //simetrica
                    }
                }
            }

            // y se evapora en todos los arcos de la matriz de feromona (cristobal), solo se evapora en los arcos
            //de la mejor solución global (UGR)
            for (int i = 0; i < datos.getTamMatriz(); i++) {
                for (int j = 0; j < datos.getTamMatriz(); j++) {
                    if (i != j) {
                        feromonas[i][j] = ((1 - 0.1) * feromonas[i][j]);
                    }
                }
            }

            //LIMPIAMOS HORMIGAS
            poblacion.reiniciar();
            iteracion++;
            long stopTime = System.currentTimeMillis();
            tiempo += stopTime - startTime;
        } //fin cuando las hormigas estan completas      
        System.out.println("///////////////////////////////////////////////////////////////////////////////");

        for (int i = 0; i < poblacion.getTamPoblacion(); i++) {
            System.out.println("Hormiga " + i + ". Tamaño: " + datos.getTamSolucion() + "/" + poblacion.getHormiga(i).getSol().length + ". Solucion: " + poblacion.getHormiga(i).getSol().toString() + "\n");
        }
    }

    private double Coste(Integer s[], double dist[][], int m) {
        double cost = 0.0;
        for (int i = 0; i < m - 1; i++) {
            for (int j = i + 1; j < m; j++) {
                cost += dist[s[i]][s[j]];
            }
        }
        return cost;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SCH;

import java.util.Arrays;
import java.util.Vector;
import tools.GuardarLog;
import tools.Random;

/**
 *
 * @author Miguerubsk
 */
public class SCH {

    private double matriz[][]; //Matriz de distancias
    private Integer mejorSolucion[], mejorHormigaActual[];
    private double mejorCosteActual, mejorCosteGlobal;
    private String Solucion;
    private final GuardarLog log;

    //Parametros para el sistema
    private int n, m, poblacion, numIteraciones, beta;
    private long tiempoMax;
    private double greedy, p, alfa, fi, q0, delta;
    String fichero;

    private Random aleatorio;

    public SCH(Long semilla, String fichero, double matriz[][], int n, int m, int iteraciones, int poblacion, double greedy, int alfa, int beta, double q0, double p, double fi, double delta, long time) {
        this.matriz = matriz;
        this.mejorSolucion = null;
        this.mejorCosteActual = 0;
        this.mejorCosteGlobal = 0;
        this.mejorHormigaActual = null;
        this.fichero = fichero;
        this.n = n;
        this.tiempoMax = time;
        this.m = m;
        this.poblacion = poblacion;
        this.greedy = greedy;
        this.numIteraciones = iteraciones;
        this.delta = delta;
        this.q0 = q0;
        this.fi = fi;
        this.beta = beta;
        this.alfa = alfa;
        this.p = p;
        this.Solucion = "";
        this.aleatorio = new Random(semilla);
        String ruta = "SCH-" + alfa + "-" + beta + "-" + fichero;
        String info = "[EJECUCION INICIADA]\n"
                + "Archivo: " + fichero
                + "\nAlfa: " + alfa
                + "\nBeta: " + beta
                + "\nTamaño matriz: " + n
                + "\nTamañoSolucion: " + m
                + "\nTamañoPoblacion: " + poblacion;
        System.out.println(info);
        this.log = new GuardarLog(ruta, info, "SCH-" + alfa + "-" + beta);
    }

    public void ejecutar() {

        //Declaracion de vectores e inicializacion
        //Matriz feromonas y heuristica
        double feromona[][] = new double[n][n];
        double heuristica[][] = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                heuristica[i][j] = 0.0;
                feromona[i][j] = 0.0;
            }
        }
        //Vector candidatos a elemento solucion
        Vector<Integer> LRC = new Vector<>();

        //Matriz de hormigas (soluciones)
        Integer hormigas[][] = new Integer[poblacion][m];
        for (Integer[] hormiga : hormigas) {
            for (Integer integer : hormiga) {
                integer = 0;
            }
        }

        //Elementos marcados de cada hormiga
        Boolean marcados[][] = new Boolean[poblacion][n];
        for (int i = 0; i < poblacion; i++) {
            for (int j = 0; j < n; j++) {
                marcados[i][j] = false;
            }
        }

        //Carga inicial de feromona y de la heuristica
        double ferInicio = greedy;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    feromona[i][j] = ferInicio;
                    heuristica[i][j] = matriz[i][j];
                }
            }
        }

        int iteracion = 0;
        double tiempo = 0;
        while (iteracion < numIteraciones && tiempo < tiempoMax) {
            log.escribir("----------------------------------------------------------------------------------------------------------------\n\n\nNUMERO DE ITERACION: " + iteracion);
            double start = System.currentTimeMillis();
            //Carga de las hormigas iniciales
            for (int i = 0; i < poblacion; i++) {
                hormigas[i][0] = aleatorio.Randint(0, n - 1);
                marcados[i][hormigas[i][0]] = true;
            }

            //Generamos la solucion completa para cada hormiga
            for (int elemento = 1; elemento < m; elemento++) {
                for (int hormiga = 0; hormiga < poblacion; hormiga++) {

                    //Vector para las distancias de los elementos candidatos
                    Double distancias[];
                    distancias = new Double[n];
                    for (Double distancia : distancias) {
                        distancia = -1.0;
                    }

                    //Calculamos las distancias de los elementos candidatos con nuestra solucion             
                    for (int i = 0; i < n; i++) {
                        double d = 0.0;
                        if (!marcados[hormiga][i]) {
                            for (int k = 0; k < elemento; k++) {
                                d += matriz[i][hormigas[hormiga][k]];
                            }
                            distancias[i] = d;
                        }
                    }
                    log.escribirNoInfo("\n\nDistancias de los elementos candidatos: " + toString(distancias));

                    //Calculamos la distancia minima y maxima para la LRC
                    double mayorDist = 0;
                    double menorDist = 999999999;
                    for (int i = 0; i < n; i++) {
                        if (!marcados[hormiga][i]) {
                            if (distancias[i] < menorDist) {
                                menorDist = distancias[i];
                            } else if (distancias[i] > mayorDist) {
                                mayorDist = distancias[i];
                            }
                        }
                    }
                    log.escribirNoInfo("\n\nDistancia máxima de la LRC: " + mayorDist);
                    log.escribirNoInfo("Distancia mínima de la LRC: " + menorDist);

                    //Elegimos los elementos que se añadiran a la LRC
                    //Si no esta en la solucion de la hormiga 
                    for (int i = 0; i < n; i++) {
                        if (!(marcados[hormiga][i]) && (distancias[i] >= (menorDist + (delta * (mayorDist - menorDist))))) {
                            LRC.add(i);
                        }
                    }
                    log.escribirNoInfo("LRC: " + LRC.toString());

                    //Elegimos el elemento de la LRC que se añadira a la solucion 
                    Double feromonasXheuristica[] = new Double[LRC.size()];
                    for (int i = 0; i < LRC.size(); i++) {
                        feromonasXheuristica[i] = 0.0;
                    }

                    //Cantidad total de feromona*heuristica por cada elemento de la LRC 
                    //respecto de los elementos de la solución
                    for (int i = 0; i < LRC.size(); i++) {
                        for (int j = 0; j < elemento; j++) {
                            feromonasXheuristica[i] += Math.pow(heuristica[j][LRC.get(i)], beta) * Math.pow(feromona[j][LRC.get(i)], alfa);
                        }
                    }
                    log.escribirNoInfo("\n\nFeromona*Heurística: " + toString(feromonasXheuristica));

                    //calculo del argMax y sumatoria del total de feromonaxHeuristica
                    //(denominador)
                    double denominador = 0.0;
                    double argMax = 0.0;
                    int posArgMax = 0;
                    for (int i = 0; i < LRC.size(); i++) {
                        denominador += feromonasXheuristica[i];
                        if (feromonasXheuristica[i] > argMax) {
                            argMax = feromonasXheuristica[i];
                            posArgMax = LRC.get(i);
                        }
                    }

                    //Transicion
                    //Vector de probabilidades de transicion
                    int elegido = 0;
                    Double probabilidades[] = new Double[LRC.size()];
                    for (Double ele : probabilidades) {
                        ele = 0.0;
                    }
                    double q = aleatorio.Randfloat(0, (float) 1.01);

                    if (q0 <= q) {  //Nos quedamos con el mejor
                        elegido = posArgMax;
                    } else {  //Se aplica la transicion normal
                        for (int i = 0; i < LRC.size(); i++) {
                            double numerador = feromonasXheuristica[i];
                            probabilidades[i] = numerador / denominador;
                        }
                        log.escribirNoInfo("\n\nVector de probabilidades: " + toString(probabilidades));

                        //Elegimos el nuevo elemento en base a las probabilidades                                      
                        double Uniforme = aleatorio.Randfloat(0, (float) 1.0);  //Aleatorio uniforme
                        double acumulado = 0.0; //Acumulado
                        for (int i = 0; i < LRC.size(); i++) {
                            acumulado += probabilidades[i];
                            if (Uniforme <= acumulado) { //Si el acumulado sobrepasa al uniforme, elegimos al primero que sume por encima
                                elegido = LRC.get(i);
                                log.escribirNoInfo("Elemento elegido: " + elegido);
                                break; //Y salimos con el elemento
                            }
                        }
                        log.escribirNoInfo("Probabilidad acumulada: " + acumulado);
                    }

                    //Añadimos el elemento nuevo de la solucion
                    hormigas[hormiga][elemento] = elegido;
                    marcados[hormiga][elegido] = true;
                    log.escribirNoInfo("\n\nHormiga[" + hormiga + "]: " + toString(hormigas[hormiga]));

                    //Limpiamos la LRC
                    LRC.clear();
                }

                //Aqui ya habriamos añadido una componente a cada hormiga
                //Actualizacion de feromona local
                for (int h = 0; h < poblacion; h++) {
                    for (int i = 0; i < elemento; i++) {
                        feromona[hormigas[h][i]][hormigas[h][elemento]] = ((1 - fi) * feromona[hormigas[h][i]][hormigas[h][elemento]]) + (fi * ferInicio);
                    }
                }
//                log.escribirNoInfo("Matriz de feromona actualizada:\n"+toString(feromona));

            }

            //Ya hemos completado todas las hormigas
            //Buscamos la mejor hormiga actual
            mejorCosteActual = 0;
            for (int i = 0; i < poblacion; i++) {
                double coste = coste(hormigas[i], matriz, m);
                if (coste > mejorCosteActual) {
                    mejorCosteActual = coste;
                    mejorHormigaActual = hormigas[i];
                }
            }
            log.escribirNoInfo("\n\nMejor hormiga actual: " + toStringSol(mejorHormigaActual) + "\nCoste: " + mejorCosteActual);

            //Actualizamos si la nueva mejor hormiga de esta iteracion mejora la mejor de todas
            if (mejorCosteActual > mejorCosteGlobal) {
                mejorCosteGlobal = mejorCosteActual;
                mejorSolucion = mejorHormigaActual.clone();
            }
            log.escribirNoInfo("\n\nMejor hormiga global: " + toStringSol(mejorSolucion) + "\nCoste: " + mejorCosteGlobal);

            //Demonio
            log.escribirNoInfo("\nAPLICANDO DEMONIO");
            demonio(feromona);

            //Reiniciamos las hormigas
            log.escribirNoInfo("REINICIO DE LAS HORMIGAS");
            for (int i = 0; i < poblacion; i++) {
                for (int j = 0; j < m; j++) {
                    hormigas[i][j] = 0;
                }
            }
            for (int i = 0; i < poblacion; i++) {
                for (int j = 0; j < n; j++) {
                    marcados[i][j] = false;
                }
            }

            iteracion++;
            double stop = System.currentTimeMillis();
            tiempo += stop - start;
//            System.out.println("Iteracion: " + iteracion + " Coste mejor solucion actual: " + mejorCosteGlobal);
        }
        Solucion = toStringSol(mejorSolucion);
        System.out.println("\nFichero: " + fichero
                + "\nAlfa: " + alfa
                + "\nBeta: " + beta
                + "\nTiempo: " + tiempo
                + "\nCoste: " + mejorCosteGlobal
                + "\nSolucion: " + Solucion);
        log.escribirFinal("\nFichero: " + fichero
                + "\nAlfa: " + alfa
                + "\nBeta: " + beta
                + "\nTiempo: " + tiempo
                + "\nCoste: " + mejorCosteGlobal
                + "\nSolucion: " + Solucion
                + "\nIteraciones: " + iteracion);
        System.out.println("Total Iteraciones:" + iteracion);

    }

    private double coste(Integer s[], double matriz[][], long m) {
        double coste = 0.0;
        for (int i = 0; i < m - 1; i++) {
            for (int j = i + 1; j < m; j++) {
                coste += matriz[s[i]][s[j]];
            }
        }
        return coste;
    }

    private void demonio(double feromona[][]) {
        //Actualizacion de la feromona global (solo afecta a los elemnentos de la mejor solucion)        
        double deltaMejor = mejorCosteActual; //Delta maximizada (coste la mejor solucion)
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (mejorSolucion[i] != j) {
                    feromona[mejorSolucion[i]][j] += (p * deltaMejor);
                    feromona[j][mejorSolucion[i]] = feromona[mejorSolucion[i]][j];
                }
            }
        }
//        log.escribir("Matriz de fermonas tras actualizar con el demonio:\n"+toString(feromona));

        //Evaporacion
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    feromona[i][j] = ((1 - p) * feromona[i][j]);
                }
            }
        }
//        log.escribir("Matriz de feromonas tras evaporar con el demonio:\n"+toString(feromona));
    }

    private String toStringSol(Integer sol[]) {
        String aux = "[";
        Arrays.sort(sol);
        for (int i = 0; i < sol.length; i++) {
            aux += sol[i];
            if (i < sol.length - 1) {
                aux += ", ";
            }
        }
        aux += "]";
        return aux;
    }

    private String toString(Integer sol[]) {
        String aux = "[";
        for (int i = 0; i < sol.length; i++) {
            aux += sol[i];
            if (i < sol.length - 1) {
                aux += ", ";
            }
        }
        aux += "]";
        return aux;
    }

    private String toString(Double vec[]) {
        String aux = "[";
//        Arrays.sort(vec);
        for (int i = 0; i < vec.length; i++) {
            aux += vec[i];
            if (i < vec.length - 1) {
                aux += ", ";
            }
        }
        aux += "]";
        return aux;
    }

    private String toString(double mat[][]) {
        String aux = "[";
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[i].length; j++) {
                aux += mat[i][j];
                if (j < mat[i].length - 1) {
                    aux += ", ";
                }
            }
            aux += "\n";
        }
        aux += "]";
        return aux;
    }

    private String toString(int mat[][]) {
        String aux = "[";
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[i].length; j++) {
                aux += mat[i][j];
                if (j < mat[i].length - 1) {
                    aux += ", ";
                }
            }
            aux += "\n";
        }
        aux += "]";
        return aux;
    }
}

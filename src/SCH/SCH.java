/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SCH;

import java.util.Vector;
import tools.Random;

/**
 *
 * @author Miguerubsk
 */
public class SCH {

    public void ejecutar(double dist[][],
            int n, int m, long iteraciones, int poblacion,
            double greedy, int alfa, int beta, double q0, double p,
            double fi, double delta) {

        Integer s[] = null;
        Random aleatorio = new Random(26525289);
        //Declaracion de variables y estructuras de sistema 
        Double feromona[][];
        Double heuristica[][];
        Vector<Integer> LRC;
        Integer hormigas[][];
        Boolean marcados[][];

        double mejorCosteActual, mejorCosteGlobal = 0;
        Integer mejorHormigaActual[] = null;

        //Inicializamos la matriz de Hormigas, Heurística y de Feromona, vectores    
        feromona = new Double[n][n];
        heuristica = new Double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                heuristica[i][j] = 0.0;
                feromona[i][j] = 0.0;
            }
        }
        LRC = new Vector<>();
        hormigas = new Integer[poblacion][m];
        for (Integer[] hormiga : hormigas) {
            for (Integer integer : hormiga) {
                integer = 0;
            }
        }

        marcados = new Boolean[poblacion][n];
        for (int i = 0; i < poblacion; i++) {
            for (int j = 0; j < n; j++) {
                marcados[i][j] = false;
            }
        }

        //Carga inicial de feromona y de la heuristica
        double fInicial = greedy;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    feromona[i][j] = fInicial;
                    heuristica[i][j] = dist[i][j];
                }
            }
        }

        //Contador de iteraciones del sistema
        int cont = 0;
        //PRINCIPAL: Comienzan las iteraciones

        double tiempo = 0;
        while (cont < iteraciones && tiempo < 600000) {

            //Carga de las hormigas iniciales
            for (int i = 0; i < poblacion; i++) {
                hormigas[i][0] = aleatorio.Randint(0, n - 1);
                marcados[i][hormigas[i][0]] = true;
            }

            char c;
            //GENERAMOS las m-1 componentes pdtes. de las hormigas
            for (int comp = 1; comp < m; comp++) {
                //Para cada hormiga
                for (int h = 0; h < poblacion; h++) {

                    //vector que almacena las distancias
                    Double distancias[];
                    distancias = new Double[n];
                    for (Double distancia : distancias) {
                        distancia = -1.0;
                    }

                    //Calculamos las distancias de las n componentes candidatas:               
                    for (int i = 0; i < n; i++) {
                        double d = 0.0;
                        if (!marcados[h][i]) {
                            for (int k = 0; k < comp; k++) {
                                d += dist[i][hormigas[h][k]];
                            }
                            distancias[i] = d;
                        }
                    }

                    //Calculamos la distancia minima y maxima
                    double mayord = 0, menord = 999999999;
                    for (int i = 0; i < n; i++) {
                        if (!marcados[h][i]) {
                            if (distancias[i] < menord) {
                                menord = distancias[i];
                            } else if (distancias[i] > mayord) {
                                mayord = distancias[i];
                            }
                        }
                    }

                    //elegimos componentes para LRC
                    for (int i = 0; i < n; i++) {
                        if (!(marcados[h][i]) && (distancias[i] >= (menord + (delta * (mayord - menord))))) {
                            LRC.add(i);
                        }
                    }

                    //ELECCION del ELEMENTO de la LRC a incluir en la solucion 
                    Double ferxHeu[] = new Double[LRC.size()];
                    for (int i = 0; i < LRC.size(); i++) {
                        ferxHeu[i] = 0.0;
                    }

                    //calculo la cantidad total de feromonaxheuristica por cada elemento de la LRC 
                    //respecto de los elementos de la solución
                    for (int i = 0; i < LRC.size(); i++) {
                        for (int j = 0; j < comp; j++) {
                            ferxHeu[i] += Math.pow(heuristica[j][LRC.get(i)], beta) * Math.pow(feromona[j][LRC.get(i)], alfa);
                        }
                    }

                    //calculo del argMax y sumatoria del total de feromonaxHeuristica
                    //(denominador)
                    double denominador = 0.0;
                    double argMax = 0.0;
                    int posArgMax = 0;
                    for (int i = 0; i < LRC.size(); i++) {
                        denominador += ferxHeu[i];
                        if (ferxHeu[i] > argMax) {
                            argMax = ferxHeu[i];
                            posArgMax = LRC.get(i);
                        }
                    }

                    //FUNCION de TRANSICION
                    //vector de probabilidades de transicion
                    int elegido = 0;
                    Double prob[] = new Double[LRC.size()];
                    for (Double ele : prob) {
                        ele = 0.0;
                    }
                    double q = aleatorio.Randfloat(0, (float) 1.01);   //aleatorio inicial

                    if (q0 <= q) {  //aplicamos argumento maximo y nos quedamos con el mejor
                        elegido = posArgMax;
                    } else {  //aplicamos regla de transicion normal
                        for (int i = 0; i < LRC.size(); i++) {
                            double numerador = ferxHeu[i];
                            prob[i] = numerador / denominador;
                        }

                        //elegimos la componente a añadir buscando en los intervalos de probabilidad                                        
                        double Uniforme = aleatorio.Randfloat(0, (float) 1.0);  //aleatorio para regla de transición
                        double acumulado = 0.0;
                        for (int i = 0; i < LRC.size(); i++) {
                            acumulado += prob[i];
                            if (Uniforme <= acumulado) {
                                elegido = LRC.get(i);
                                break;
                            }
                        }
                    }

                    hormigas[h][comp] = elegido;
                    marcados[h][elegido] = true;

//                muestraHormiga(hormigas[h]);
                    LRC.clear();

                } //fin agregado una componente a cada hormiga

                //actualización de feromona local, que afecta a todos los ya incluidos en la solActual.
                for (int h = 0; h < poblacion; h++) {
                    for (int i = 0; i < comp; i++) {
                        feromona[hormigas[h][i]][hormigas[h][comp]] = ((1 - fi) * feromona[hormigas[h][i]][hormigas[h][comp]]) + (fi * fInicial);
                    }
                }

            } //fin cuando las hormigas estan completas                 

//         if (cont==14){
//             c=getchar();
//                    std::cout << "Iteracion" << ": " << cont << endl;
//                    for (int k=0; k<poblacion; k++){ 
//                        if (todosDif(hormigas[k]))
//                             std::cout << "Todos diferentes" << std::endl;
//                        std::cout << "Hormiga " << k << ":" <<std::endl;
//                        muestraHormiga(hormigas[k]);
//                    }
//                    c=getchar();
//                    
//                }  
            //CALCULAMOS la mejor HORMIGA
            mejorCosteActual = 0;
            for (int i = 0; i < poblacion; i++) {
                double coste = Coste(hormigas[i], dist, m);
                if (coste > mejorCosteActual) {
                    mejorCosteActual = coste;
                    mejorHormigaActual = hormigas[i];
                }
            }

            //ACTUALIZAMOS si la mejor actual mejora al mejor global
            if (mejorCosteActual > mejorCosteGlobal) {
                mejorCosteGlobal = mejorCosteActual;
                s = mejorHormigaActual;
            }

            //APLICAMOS el DEMONIO 
            //(actualizacion de feromona (aporta la mejor Global y solo a los arcos de dicha solucion        
            double deltaMejor = mejorCosteActual;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (s[i] != j) {
                        feromona[s[i]][j] += (p * deltaMejor);
                        feromona[j][s[i]] = feromona[s[i]][j];  //simetrica
                    }
                }
            }

            // y se evapora en todos los arcos de la matriz de feromona (cristobal), solo se evapora en los arcos
            //de la mejor solución global (UGR)
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        feromona[i][j] = ((1 - p) * feromona[i][j]);
                    }
                }
            }

            //LIMPIAMOS HORMIGAS
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

            cont++;

            System.out.println("Iteracion: " + cont + " Coste: " + mejorCosteGlobal);

        }

        System.out.println(" Total Iteraciones:" + cont);

    }

    private double Coste(Integer s[], double dist[][], long m) {
        double cost = 0.0;
        for (int i = 0; i < m - 1; i++) {
            for (int j = i + 1; j < m; j++) {
                cost += dist[s[i]][s[j]];
            }
        }
        return cost;
    }
}

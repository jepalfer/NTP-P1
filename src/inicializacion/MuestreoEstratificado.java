package inicializacion;

import imagen.Pixel;
import kmedias.KMedias;
import imagen.Utilidades;

import java.util.*;

/**
 * clase para aportar la seleccion de centroides
 * mediante muestreo estratificado
 */
public class MuestreoEstratificado implements EstrategiaInicializacion {
   /**
    * seleccion de centroides con muestreo estratificado.
    * Requiere determinar el numero de pixels asociado a
    * cada estrato y seleccionar posteriormente
    *
    * @param kmedias objeto a inicializar
    * @return lista de pixels seleccionados como
    * centroides
    * NOTA: por implementar
    */
   @Override
   public List<Pixel> seleccionar(KMedias kmedias) {
      // se obtiene el valor de k
      int k = kmedias.obtenerK();

      // se obtienen los pixels de la imagen
      List<Pixel> pixels = kmedias.obtenerPixels();

      // se crea la lista resultado
      List<Pixel> seleccionados;

      // agrupar los pixels de acuerdo a sus colores,
      // en los intervalos considerados
      Map<Integer, List<Pixel>> porIntervalo =
              agruparPixelsIntervalos(k, pixels);

      // se obtiene la distribucion de probabilidad
      // asociada al agrupamiento
      List<Double> distribucion = obtenerDistribucion(porIntervalo,
              pixels.size());

      // se realiza la seleccion
      seleccionados = muestrear(porIntervalo, distribucion, k);

      // se devuelve la lista de centroides seleccionados
      return seleccionados;
   }

   @Override
   public List<Pixel> seleccionarFuncional(KMedias kmedias) {
      // se obtiene el valor de k
      int k = kmedias.obtenerK();

      // se obtienen los pixels de la imagen
      List<Pixel> pixels = kmedias.obtenerPixels();

      // se crea la lista resultado
      List<Pixel> seleccionados;

      // agrupar los pixels de acuerdo a sus colores,
      // en los intervalos considerados
      Map<Integer, List<Pixel>> porIntervalo =
              agruparPixelsIntervalos(k, pixels);

      // se obtiene la distribucion de probabilidad
      // asociada al agrupamiento
      List<Double> distribucion = obtenerDistribucion(porIntervalo,
              pixels.size());

      // se realiza la seleccion
      seleccionados = muestrear(porIntervalo, distribucion, k);

      // se devuelve la lista de centroides seleccionados
      return seleccionados;
   }


   /**
    * metodo de agrupamiento de los pixels por intervalos
    * de color
    *
    * @param k      numero de tramos a considerar
    * @param pixels lista de pixels a agrupar
    * @return mapa con agrupamientos de pixels por
    * intervalos
    * NOTA: por implementar
    */
   private Map<Integer, List<Pixel>> agruparPixelsIntervalos(int k,
                                       List<Pixel> pixels) {
      // crear el diccionario a devolver
      Map<Integer, List<Pixel>> porIntervalo = new HashMap<>();

      // determinar colores minimo y maximo del rango de colores
      List<Integer> minMax = Utilidades.obtenerMinimoMaximo(pixels);
      System.out.println("maximo color: " + minMax.get(1));

      // se calcula el incremento para fijar las marcas
      double incremento = (minMax.get(1) - minMax.get(0)) / (k * 1.0);

      // se van obteniendo los pixels en cada intervalo
      for(int i=0; i < k; i++){
         // obtener el color de inicio del tramo que corresponda
         double inicio = minMax.get(0) + incremento*i;
         double fin = inicio + incremento;
         System.out.println("inicio: " + inicio + " fin: " + fin);

         // se obtienen los pixels con indices de colores
         // comprendidos en el intervalo
         List<Pixel> enIntervalo =
                 Utilidades.obtenerPuntosIntervalo(pixels, inicio, fin);

         // agrego la entrada al diccionario
         porIntervalo.put(i, enIntervalo);
      }

      // se devuelve el map
      return porIntervalo;
   }
   private Map<Integer, List<Pixel>> agruparPixelsIntervalosFuncional(int k,
                                                             List<Pixel> pixels) {
      // crear el diccionario a devolver
      Map<Integer, List<Pixel>> porIntervalo = new HashMap<>();

      // determinar colores minimo y maximo del rango de colores
      List<Integer> minMax = Utilidades.obtenerMinimoMaximo(pixels);
      System.out.println("maximo color: " + minMax.get(1));

      // se calcula el incremento para fijar las marcas
      double incremento = (minMax.get(1) - minMax.get(0)) / (k * 1.0);

      // se van obteniendo los pixels en cada intervalo
      for(int i=0; i < k; i++){
         // obtener el color de inicio del tramo que corresponda
         double inicio = minMax.get(0) + incremento*i;
         double fin = inicio + incremento;
         System.out.println("inicio: " + inicio + " fin: " + fin);

         // se obtienen los pixels con indices de colores
         // comprendidos en el intervalo
         List<Pixel> enIntervalo =
                 Utilidades.obtenerPuntosIntervalo(pixels, inicio, fin);

         // agrego la entrada al diccionario
         porIntervalo.put(i, enIntervalo);
      }

      // se devuelve el map
      return porIntervalo;
   }


   /**
    * obtiene una distribucion de probabilidad en funcion
    * del numero de puntos de cada intervalo.
    *
    * @param porIntervalo clasificacion de pixels por
    *                     intervalos
    * @param numeroPixels numero total de pixels
    * @return distribucion de probabilidad para cada
    * intervalo a considerar
    * NOTA: por implementar
    */
   private List<Double> obtenerDistribucion(
           Map<Integer, List<Pixel>> porIntervalo, int numeroPixels) {
      // se crea la coleccion a devolver
      List<Double> distribucion = new ArrayList<>();

      // se crean variables auxiliares
      double acumulado = 0;
      double valor;

      // se recorren los agrupamientos para generar los
      // valores de la distribucion
      for(int i=0; i < porIntervalo.size(); i++) {
         // se calcular el valor de probabilidad que
         // corresponde al grupo
         valor = porIntervalo.get(i).size() / (numeroPixels*1.0);
         System.out.println("tam grupo: " + porIntervalo.get(i).size() +
            " - " + numeroPixels);
         System.out.println("valor: " + valor);

         // incremento acumulado
         acumulado += valor;

         // almaceno el valor acumulado
         distribucion.add(acumulado);
         System.out.println(" acumulado: " + acumulado);
      }

      // se devuelve la distribucion
      return distribucion;
   }
   private List<Double> obtenerDistribucionFuncional(
           Map<Integer, List<Pixel>> porIntervalo, int numeroPixels) {
      // se crea la coleccion a devolver
      List<Double> distribucion = new ArrayList<>();

      // se crean variables auxiliares
      double acumulado = 0;
      double valor;

      // se recorren los agrupamientos para generar los
      // valores de la distribucion
      for(int i=0; i < porIntervalo.size(); i++) {
         // se calcular el valor de probabilidad que
         // corresponde al grupo
         valor = porIntervalo.get(i).size() / (numeroPixels*1.0);
         System.out.println("tam grupo: " + porIntervalo.get(i).size() +
                 " - " + numeroPixels);
         System.out.println("valor: " + valor);

         // incremento acumulado
         acumulado += valor;

         // almaceno el valor acumulado
         distribucion.add(acumulado);
         System.out.println(" acumulado: " + acumulado);
      }

      // se devuelve la distribucion
      return distribucion;
   }
   /**
    * se realiza el muestreo usando los pixels en la
    * distribucion y las probabilidades asociadas
    *
    * @param porIntervalo agrupamiento de pixels por
    *                     intervalo
    * @param distribucion distribucion de probabilidad
    *                     de cada intervalo
    * @param k            numero de pixels a seleccionar
    * @return lista de pixels seleccionados como
    * centroides
    * NOTA: por implementar
    */
   private List<Pixel> muestrear(
           Map<Integer, List<Pixel>> porIntervalo,
           List<Double> distribucion, int k) {
      // se crea la lista de pixels a devolver
      List<Pixel> seleccionados = new ArrayList<>();

      // se crea el generador de numeros aleatorios
      Random generador = new Random();

      // se realiza el muestreo mediante generacion
      // de numeros aleatorios
      double semilla;
      int indiceIntervalo;
      for(int i=0; i < k; i++){
         // generar el numero aleatorio
         semilla = generador.nextDouble();

         // se obtiene el tramo al que corresponde el
         // valor de semilla de acuerdo a la distribucion
         indiceIntervalo = obtenerTramo(semilla, distribucion);

         // se selecciona pixel del intervalo correspondiente
         Pixel seleccionado =
             seleccionarPixelTramo(porIntervalo.get(indiceIntervalo));

         // se agrega el pixel seleccionado a la lista de
         // seleccionados
         seleccionados.add(seleccionado);
      }

      // se devuelve la lista de pixels seleccionados
      return seleccionados;
   }
   private List<Pixel> muestrearFuncional(
           Map<Integer, List<Pixel>> porIntervalo,
           List<Double> distribucion, int k) {
      // se crea la lista de pixels a devolver
      List<Pixel> seleccionados = new ArrayList<>();

      // se crea el generador de numeros aleatorios
      Random generador = new Random();

      // se realiza el muestreo mediante generacion
      // de numeros aleatorios
      double semilla;
      int indiceIntervalo;
      for(int i=0; i < k; i++){
         // generar el numero aleatorio
         semilla = generador.nextDouble();

         // se obtiene el tramo al que corresponde el
         // valor de semilla de acuerdo a la distribucion
         indiceIntervalo = obtenerTramo(semilla, distribucion);

         // se selecciona pixel del intervalo correspondiente
         Pixel seleccionado =
                 seleccionarPixelTramo(porIntervalo.get(indiceIntervalo));

         // se agrega el pixel seleccionado a la lista de
         // seleccionados
         seleccionados.add(seleccionado);
      }

      // se devuelve la lista de pixels seleccionados
      return seleccionados;
   }

   /**
    * se obtiene el tramo al que corresponde un determinado
    * valor aleatorio, de acuerdo con la distribucion pasada
    * como segundo argumento
    *
    * @param semilla      valor de probabilidad a considerar
    * @param distribucion distribucion de probabilidad
    * @return indice del tramo seleccionado
    * NOTA: por implementar
    */
   private int obtenerTramo(double semilla, List<Double> distribucion) {
      boolean terminar = false;
      int indice = -1;

      // bucle de recorrido de los valores de la
      // distribucion
      for(int i=0; i < distribucion.size() && !terminar; i++){
         if(distribucion.get(i) >= semilla){
            terminar = true;
            indice = i;
         }
      }

      // se devuelve el valor de indice
      return indice;
   }
   private int obtenerTramoFuncional(double semilla, List<Double> distribucion) {
      boolean terminar = false;
      int indice = -1;

      // bucle de recorrido de los valores de la
      // distribucion
      for(int i=0; i < distribucion.size() && !terminar; i++){
         if(distribucion.get(i) >= semilla){
            terminar = true;
            indice = i;
         }
      }

      // se devuelve el valor de indice
      return indice;
   }


   /**
    * se selecciona de forma aleatoria un pixel de la lista
    * de pixels pasados como argumentos
    *
    * @param pixels lista de pixels a considerar
    * @return pixel seleccionado de forma aleatoria
    */
   private Pixel seleccionarPixelTramo(List<Pixel> pixels) {
      // se selecciona un indice entre 0 y el numero de pixels
      // de la lista pasada como argumento
      Random generador = new Random();
      int indice = generador.nextInt(pixels.size());

      // se devuelve el pixel asociado al indice
      return pixels.get(indice);
   }
}

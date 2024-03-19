package convergencia;

import imagen.Pixel;
import kmedias.KMedias;

/**
 * clase para determinacion de condicion de parada considerando
 * las diferencias entre los centros considerados al inicio y al
 * final de cada iteracion
 */
public class ConvergenciaEstabilidad extends EstrategiaConvergencia {
   /**
    * controla el nivel de estabilidad permitido
    */
   private final double umbral;

   /**
    * maximo numero permitido de iteraciones
    */
   private final int maxIteraciones;

   /**
    * constructor de la clase
    *
    * @param umbral         umbral a considerar para el chequeo de
    *                       estabilidad
    * @param maxIteraciones numero maximo de iteraciones a
    *                       realizar
    */
   public ConvergenciaEstabilidad(double umbral, int maxIteraciones) {
      this.umbral = umbral;
      this.maxIteraciones = maxIteraciones;
      medida = 0;
   }

   /**
    * deteccion de parada considerando distancias
    * entre centroides de iteraciones sucesivas
    *
    * @param kmedias objeto analizado
    * @return flag booleano indicando si hay convergencia
    * NOTA: por implementar
    */
   @Override
   public boolean convergencia(KMedias kmedias) {
      boolean convergencia = false;

      // se consideran los pares de centros al inicio
      // y final de la iteracion en curso
      for(int i=0; i < kmedias.obtenerK(); i++){
         Pixel centro1 = kmedias.obtenerCentrosT1().get(i);
         Pixel centro2 = kmedias.obtenerCentrosT2().get(i);

         // se acumula la distancia entre los centros
         medida += centro1.distanciaCuadratica(centro2);
      }

      // se determina si la media de la medida es mayor
      // que el umbral
      medida = medida / kmedias.obtenerK();

      // se determina si hay convergencia
      if(medida > umbral ||
              kmedias.obtenerContadorIteraciones() >= maxIteraciones){
         convergencia = true;
      }

      // se devuelve el resultado
      return convergencia;
   }
   @Override
   public boolean convergenciaFuncional(KMedias kmedias) {
      boolean convergencia = false;

      // se consideran los pares de centros al inicio
      // y final de la iteracion en curso
      for(int i=0; i < kmedias.obtenerK(); i++){
         Pixel centro1 = kmedias.obtenerCentrosT1().get(i);
         Pixel centro2 = kmedias.obtenerCentrosT2().get(i);

         // se acumula la distancia entre los centros
         medida += centro1.distanciaCuadratica(centro2);
      }

      // se determina si la media de la medida es mayor
      // que el umbral
      medida = medida / kmedias.obtenerK();

      // se determina si hay convergencia
      if(medida > umbral ||
              kmedias.obtenerContadorIteraciones() >= maxIteraciones){
         convergencia = true;
      }

      // se devuelve el resultado
      return convergencia;
   }

}

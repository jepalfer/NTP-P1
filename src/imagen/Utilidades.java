package imagen;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * clase con metodos estaticos de utilidad
 */
public interface Utilidades {
   /**
    * metodo estatico para calcular la media de un
    * conjunto de pixels
    *
    * @param pixels coleccion de pixels para el calculo
    *               de la media (del centro)
    * @return pixel cuyas componentes son los valores
    * medios calculados
    * NOTA: por implementar
    */
   static Pixel calcularMedia(List<Pixel> pixels) {
      double sumaRojo = 0;
      double sumaVerde = 0;
      double sumaAzul = 0;

      // recorrido de todos los pixels pasados como argumentos
      for(int i=0; i < pixels.size(); i++){
         Pixel pixel = pixels.get(i);
         sumaRojo += pixel.obtenerComponente(ComponentesRGBA.ROJO);
         sumaVerde += pixel.obtenerComponente(ComponentesRGBA.VERDE);
         sumaAzul += pixel.obtenerComponente(ComponentesRGBA.AZUL);
      }

      // se calculan los valores medios
      sumaRojo = sumaRojo / pixels.size();
      sumaVerde = sumaVerde / pixels.size();
      sumaAzul = sumaAzul / pixels.size();

      // se devuelve el pixel que representa el centro
      // del grupo de pixels pasado como argumento
      return new Pixel(sumaRojo, sumaVerde, sumaAzul);
   }
   static Pixel calcularMediaFuncional(List<Pixel> pixels) {
      double sumaRojo = pixels.stream().mapToDouble(pixel -> pixel.obtenerComponente(ComponentesRGBA.ROJO)).
                  average().orElse(0);
      double sumaVerde = pixels.stream().mapToDouble(pixel -> pixel.obtenerComponente(ComponentesRGBA.VERDE)).
                  average().orElse(0);
      double sumaAzul = pixels.stream().mapToDouble(pixel -> pixel.obtenerComponente(ComponentesRGBA.AZUL)).
                  average().orElse(0);

      // se devuelve el pixel que representa el centro
      // del grupo de pixels pasado como argumento
      return new Pixel(sumaRojo, sumaVerde, sumaAzul);
   }


   /**
    * calcula los valores minimos y maximos para todos
    * los pixels de una coleccion
    *
    * @param pixels lista de pixels a coniderar
    * @return lista con el minimo y maximo indice de
    * colores
    * NOTA: por implementar
    */
   static List<Integer> obtenerMinimoMaximo(List<Pixel> pixels) {
      // inicializar los valores minimo y maximo
      Pixel primero = pixels.get(0);
      int minimo = primero.obtenerIndice();
      int maximo = primero.obtenerIndice();

      // se consideran todos los pixels
      for(int i=1; i < pixels.size(); i++){
         Pixel pi = pixels.get(i);
         int indiceColor = pi.obtenerIndice();
         if(indiceColor < minimo){
            minimo = indiceColor;
         }
         if(indiceColor > maximo){
            maximo = indiceColor;
         }
      }

      // creo una lista para devolver los valores calculados
      ArrayList<Integer> minMax = new ArrayList<>();
      minMax.add(minimo);
      minMax.add(maximo);

      // devolver la lista
      return minMax;
   }

   static List<Integer> obtenerMinimoMaximoFuncional(List<Pixel> pixels) {
      // ordenamos la lista de indices
      List<Integer> listaOrdenada = pixels.stream().map(pixel -> pixel.obtenerIndice()).sorted().
              collect(Collectors.toList());
      // creo una lista para devolver los valores calculados
      ArrayList<Integer> minMax = new ArrayList<>();
      minMax.add(listaOrdenada.get(0));
      minMax.add(listaOrdenada.get(listaOrdenada.size() - 1));

      // devolver la lista
      return minMax;
   }


   /**
    * obtiene una lista con todos los pixels cuyo indice
    * de color pertenece al intervalo especificado por
    * minimo y maximo
    *
    * @param pixels lista de pixels a analizar
    * @param minimo valor minimo del intervalo
    * @param maximo valor maximo del intervalo
    * @return lista de pixels que pertenecen al intervalo
    * NOTA: por implementar
    */
   static List<Pixel> obtenerPuntosIntervalo(List<Pixel> pixels,
                                             double minimo, double maximo) {
      List<Pixel> enIntervalo = new ArrayList<>();

      // recorrido de los pixels de la coleccion
      for(Pixel pixel : pixels){
         if(pixel.enIntervalo(minimo, maximo)){
            enIntervalo.add(pixel);
         }
      }

      // se devuelve la lista
      return enIntervalo;
   }

   static List<Pixel> obtenerPuntosIntervaloFuncional(List<Pixel> pixels,
                                             double minimo, double maximo) {
      return pixels.stream().filter(pixel -> pixel.enIntervalo(minimo, maximo)).
              collect(Collectors.toList());
   }

   static List<Pixel> obtenerPuntosIntervaloFuncionalV2(List<Pixel> pixels,
                                                        double minimo, double maximo) {
      return pixels.stream().map(pixel -> {
         if (pixel.enIntervalo(minimo, maximo)) {
            return pixel;
         }
         else {
            return null;
         }
      }).collect(Collectors.toList());
   }

   /**
    * metodo para leer un fichero y devolver la imagen creada
    * a partir de sus datos
    *
    * @param ruta ruta desde la que hacer la carga de la imagen
    * @return imagen cargada
    */
   static Imagen cargarImagen(String ruta) {
      Imagen imagen = null;
      try {
         FileInputStream fichero = new FileInputStream(ruta);
         imagen = cargarImagenFuncional(fichero);

         // se asigna la ruta del fichero
         fichero.close();
      } catch (Exception e) {
         System.out.println("error de carga de " + ruta);
         System.out.println(e);
      }

      // se devuelve la imagen creada o null
      return imagen;
   }

   /**
    * metodo auxiliar para realizar la carga de los datos
    * de la imagen y la construccion del objeto
    * correspondiente
    *
    * @param flujo flujo a usar para la carga de datos
    * @return imagen cargada
    * NOTA: por implementar
    */
   private static Imagen cargarImagen(InputStream flujo) {
      Imagen imagen = null;

      try{
         BufferedImage buffer = ImageIO.read(flujo);

         // se crea una lista con los indices de los colores
         List<Integer> datos = new ArrayList<>();
         int dimension = buffer.getHeight()*buffer.getWidth();
         for(int i=0; i < dimension; i++){
            List<Integer> indices = convertirDesplazamientoIndices(i,
                    buffer.getWidth());
            datos.add(buffer.getRGB(indices.get(0), indices.get(1)));
         }

         // se puede crear la imagen
         imagen = new Imagen(buffer.getWidth(), buffer.getHeight(), datos);
      }catch(Exception e){
         System.out.println("error al cargar la imagen");
         System.out.println(e);
      }
      // se devuelve la imagen creada o null
      return imagen;
   }
   private static Imagen cargarImagenFuncional(InputStream flujo) {
      Imagen imagen = null;

      try{
         BufferedImage buffer = ImageIO.read(flujo);

         // se crea una lista con los indices de los colores
         int dimension = buffer.getHeight()*buffer.getWidth();

         List<Integer> datos = IntStream.range(0, dimension).
                 boxed().
                 map(indice -> {
                    List<Integer> indices = convertirDesplazamientoIndices(indice,
                            buffer.getWidth());

                    return buffer.getRGB(indices.get(0), indices.get(1));
                 }).
                 collect(Collectors.toList());

         // se puede crear la imagen
         imagen = new Imagen(buffer.getWidth(), buffer.getHeight(), datos);
      }catch(Exception e){
         System.out.println("error al cargar la imagen");
         System.out.println(e);
      }
      // se devuelve la imagen creada o null
      return imagen;
   }

   /**
    * metodo publico para guardar una imagen en una
    * ruta
    *
    * @param imagen imagen a salvar
    * @param ruta   ruta del archivo a generar
    *               NOTA: por implementar
    */
   static void salvarImagen(Imagen imagen, String ruta) {
      try{
         FileOutputStream fichero = new FileOutputStream(ruta);
         BufferedImage buffer = new BufferedImage(
                 imagen.obtenerColumnas(), imagen.obtenerFilas(),
                 BufferedImage.TYPE_INT_ARGB);

         // recorrido para almacenar los pixels
         int dimension = imagen.obtenerColumnas() * imagen.obtenerFilas();
         for(int i=0; i < dimension; i++){
            List<Integer> indices =
                    convertirDesplazamientoIndices(i, imagen.obtenerColumnas());
            buffer.setRGB(indices.get(0), indices.get(1),
                    imagen.obtenerColorPixel(i));
         }

         // se guarda el buffer
         ImageIO.write(buffer, "png", fichero);
         fichero.close();
      }catch (Exception e){
         System.out.println("error en almacenamiento de archivo");
         System.out.println(e);
      }
   }

   static void salvarImagenFuncional(Imagen imagen, String ruta) {
      try{
         FileOutputStream fichero = new FileOutputStream(ruta);
         BufferedImage buffer = new BufferedImage(
                 imagen.obtenerColumnas(), imagen.obtenerFilas(),
                 BufferedImage.TYPE_INT_ARGB);

         // recorrido para almacenar los pixels
         int dimension = imagen.obtenerColumnas() * imagen.obtenerFilas();

         IntStream.range(0, dimension).
                 boxed().
                 map(indice -> {
                    List<Integer> indices =
                            convertirDesplazamientoIndices(indice, imagen.obtenerColumnas());

                    buffer.setRGB(indices.get(0), indices.get(1),
                            imagen.obtenerColorPixel(indice));

                    return 1;
                 }).collect(Collectors.toList());

         System.out.println(buffer);

         // se guarda el buffer
         ImageIO.write(buffer, "png", fichero);
         fichero.close();
      }catch (Exception e){
         System.out.println("error en almacenamiento de archivo");
         System.out.println(e);
      }
   }

   /**
    * convierte la localizacion mediante filas y columnas
    * en un desplazamiento sobre el primer pixel
    *
    * @param columna  columna del pixel objetivo
    * @param fila     fila del pixel objetivo
    * @param columnas numero de columnas de la imagen (ancho)
    * @return desplazamiento respecto al primer pixel
    */
   static int convertirIndicesDesplazamiento(int columna, int fila,
                                             int columnas) {
      return fila * columnas + columna;
   }

   /**
    * recibe como argumento un desplazamiento y determina los
    * indices de fila y columna correspondientes. Los indices se
    * devuelven en una lista: el primer valor sera el numero
    * de fila y el segundo el numero de columna
    *
    * @param desplazamiento desplazamiento del pixel objetivo
    * @param columnas       numero de columnas de la imagen (ancho)
    * @return lista con la fila y columna que corresponden al
    * desplazamiento indicado
    */
   static List<Integer> convertirDesplazamientoIndices(int desplazamiento,
                                                       int columnas) {
      List<Integer> indices = new ArrayList<>();

      // realiza la conversion
      int fila = desplazamiento % columnas;
      int columna = desplazamiento / columnas;

      // se agregan las coordenadas a la lista
      indices.add(fila);
      indices.add(columna);

      // se devuelve la lista
      return indices;
   }
}

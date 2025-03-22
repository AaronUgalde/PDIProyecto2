package Operations;

import Model.ImageMatrix;
import java.util.LinkedHashMap;

@FunctionalInterface
public interface OperationFunction {
    /**
     * Aplica la operación sobre la imagen utilizando los parámetros dados.
     * @param imageMatrix La imagen original.
     * @param parameters Un mapa con los parámetros de la operación.
     * @return La nueva ImageMatrix resultante de la operación.
     * @throws Exception en caso de error durante la operación.
     */
    ImageMatrix apply(ImageMatrix imageMatrix, LinkedHashMap<String, Object> parameters) throws Exception;
}

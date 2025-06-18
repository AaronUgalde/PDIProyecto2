package Operations.MorphologicalOperations;

import Model.ImageMatrix;
import Operations.OperationFunction;
import Operations.Result;

import java.util.LinkedHashMap;

public class ClosingOperation extends OperationFunction {
    @Override
    public Result apply(ImageMatrix original, LinkedHashMap<String, Object> params) throws Exception {
        // Paso 1: dilatación
        Result dilated = new DilationOperation().apply(original, params);
        ImageMatrix dilatedMatrix = dilated.getImageMatrix();
        // Paso 2: erosión
        return new ErosionOperation().apply(dilatedMatrix, params);
    }
}
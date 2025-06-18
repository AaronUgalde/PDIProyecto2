package Operations.MorphologicalOperations;

import Model.ImageMatrix;
import Operations.OperationFunction;
import Operations.Result;

import java.util.LinkedHashMap;

public class OpeningOperation extends OperationFunction {
    @Override
    public Result apply(ImageMatrix original, LinkedHashMap<String, Object> params) throws Exception {
        // Paso 1: erosión
        Result eroded = new ErosionOperation().apply(original, params);
        ImageMatrix erodedMatrix = eroded.getImageMatrix();
        // Paso 2: dilatación
        return new DilationOperation().apply(erodedMatrix, params);
    }
}
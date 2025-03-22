package Operations.ModelColors;

import Model.ImageMatrix;
import Operations.OperationFunction;

public abstract class ModelColor implements OperationFunction {

    public abstract ImageMatrix deApply(ImageMatrix imageMatrix);
}

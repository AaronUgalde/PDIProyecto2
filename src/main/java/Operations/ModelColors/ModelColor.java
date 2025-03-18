package Operations.ModelColors;

import Model.ImageMatrix;
import Operations.Operation;

public abstract class ModelColor extends Operation {

    public abstract ImageMatrix deApply(ImageMatrix imageMatrix);
}

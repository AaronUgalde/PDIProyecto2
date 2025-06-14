package GUI;

public enum OperationCategory {
    COLOR_SPACES("Espacios de Color"),
    FILTERS("Filtros"),
    TRANSFORMATIONS("Transformaciones"),
    SEGMENTATION("Segmentación"),
    ANALYSIS("Análisis"),
    UTILITY("Utilidades"),
    BINARIZATION("Binarizacion"),
    GEOMETRICOPERATIONS("Operaciones de Geometricos"),
    ARITHMETICOPERATIONS("Operaciones de Aritmeticos"),
    LOGICOPERATIONS("Operaciones logicas"),
    RELATIONALOPERATIONS("Operaciones de Relacional"),
    HISTOGRAMOPERATIONS("Operaciones de Histograma"),
    EDGEDETECTION("Deteccion de bordes"),
    CONVOLUTIONFILTERS("Convolution filters"),
    NONLINEARFILTERS("Filtros no lineales");

    private final String displayName;

    OperationCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
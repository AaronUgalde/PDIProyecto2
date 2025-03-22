package GUI;

public enum OperationCategory {
    COLOR_SPACES("Espacios de Color"),
    FILTERS("Filtros"),
    TRANSFORMATIONS("Transformaciones"),
    SEGMENTATION("Segmentación"),
    ANALYSIS("Análisis"),
    UTILITY("Utilidades");

    private final String displayName;

    OperationCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
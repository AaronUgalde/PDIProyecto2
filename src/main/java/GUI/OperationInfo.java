package GUI;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

import static GUI.ImageInterface.ACCENT_COLOR;

public class OperationInfo {
    public final String name;
    public final Runnable action;
    public final OperationCategory category;
    public final Color buttonColor;

    /** Nuevo: definición de argumentos: nombre → tipo */
    public final LinkedHashMap<String,Class<?>> argDefs;

    /** Nuevo: para cada argumento que sea combo, lista de opciones */
    public final Map<String,Object[]> comboOptions;

    public OperationInfo(String name,
                         Runnable action,
                         OperationCategory category,
                         LinkedHashMap<String,Class<?>> argDefs,
                         Map<String,Object[]> comboOptions) {
        this(name, action, category, ACCENT_COLOR, argDefs, comboOptions);
    }

    public OperationInfo(String name,
                         Runnable action,
                         OperationCategory category,
                         Color buttonColor,
                         LinkedHashMap<String,Class<?>> argDefs,
                         Map<String,Object[]> comboOptions) {
        this.name        = name;
        this.action      = action;
        this.category    = category;
        this.buttonColor = buttonColor;
        this.argDefs     = argDefs;
        this.comboOptions= comboOptions;
    }
}

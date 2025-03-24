package GUI;

import Operations.OperationFunction;

import java.awt.*; /**
 * Clase auxiliar para definir la configuración de un botón.
 */
public class ButtonConfig {
    private final String text;
    private final OperationFunction action;
    private final Color baseColor;

    public ButtonConfig(String text, OperationFunction action, Color baseColor) {
        this.text = text;
        this.action = action;
        this.baseColor = baseColor;
    }

    public String getText() {
        return text;
    }

    public OperationFunction getAction() {
        return action;
    }

    public Color getBaseColor() {
        return baseColor;
    }
}

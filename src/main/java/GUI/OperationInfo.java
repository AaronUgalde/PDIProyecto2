package GUI;

import java.awt.*;

import static GUI.ImageInterface.ACCENT_COLOR;

public class OperationInfo {
    public final String name;
    public final Runnable action;
    public final OperationCategory category;
    public final Color buttonColor;

    public OperationInfo(String name, Runnable action, OperationCategory category) {
        this(name, action, category, ACCENT_COLOR);
    }

    public OperationInfo(String name, Runnable action, OperationCategory category, Color buttonColor) {
        this.name = name;
        this.action = action;
        this.category = category;
        this.buttonColor = buttonColor;
    }
}
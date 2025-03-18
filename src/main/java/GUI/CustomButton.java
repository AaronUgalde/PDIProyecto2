package GUI;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CustomButton extends JButton {

    public CustomButton(String nombre, Runnable accion) {
        super(nombre);
        this.addActionListener(e -> accion.run());
    }

    // Sobrecarga si necesitas acceso al ActionEvent
    public CustomButton(String nombre, ActionListener accion) {
        super(nombre);
        this.addActionListener(accion);
    }
}
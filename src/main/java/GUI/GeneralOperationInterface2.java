package GUI;

import Model.ImageMatrix;
import Operations.OperationFunction;
import Operations.Result;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GeneralOperationInterface2 extends JFrame {
    private final String operationName;
    private final LinkedHashMap<String, Class<?>> argumentDefinitions;
    private final LinkedHashMap<String, JTextField> inputFields;
    private ImageMatrix imageMatrix;

    private JPanel mainPanel;
    private JPanel resultPanel;
    private JPanel controlPanel;

    private JScrollPane resultScrollPane;

    /**
     * Constructor de la interfaz genérica para operaciones.
     * @param operationName Nombre de la operación.
     * @param argumentDefinitions Mapa de parámetros (nombre y tipo).
     * @param imageMatrix Imagen original sobre la cual operar.
     * @param Buttons Lista de configuraciones adicionales para botones (texto, acción y color).
     */
    public GeneralOperationInterface2(String operationName,
                                     LinkedHashMap<String, Class<?>> argumentDefinitions,
                                     ImageMatrix imageMatrix,
                                     List<ButtonConfig> Buttons) {
        super("Interfaz de Operación: " + operationName);
        this.operationName = operationName;
        this.argumentDefinitions = argumentDefinitions;
        this.inputFields = new LinkedHashMap<>();
        this.imageMatrix = imageMatrix;

        initComponents(Buttons);
    }

    /**
     * Inicializa la interfaz y crea los paneles principales.
     */
    private void initComponents(List<ButtonConfig> extraButtons) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Panel principal
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 245, 250));
        setContentPane(mainPanel);

        // Panel de título
        JPanel titlePanel = createTitlePanel("Operación: " + operationName);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Panel de parámetros
        JPanel inputPanel = createInputPanel();
        // Panel de resultado
        resultPanel = createResultPanel();

        // Se guarda el scroll pane que contiene el panel de resultado
        resultScrollPane = new JScrollPane(resultPanel);
        resultScrollPane.setBorder(null);

        // JSplitPane para dividir parámetros y resultado
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputPanel, resultScrollPane);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.3);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Panel de control (botones)
        controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        controlPanel.setBackground(new Color(240, 240, 245));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        // Botón save
        JButton saveButton = createStyledButton("Guardar", this::saveResult, new Color(200, 100, 200));
        controlPanel.add(saveButton);

        // Agregar botones pasados por parámetro
        if (extraButtons != null) {
            for (ButtonConfig config : extraButtons) {
                JButton extraButton = createStyledButton(config.getText(), config.getAction(), config.getBaseColor());
                controlPanel.add(extraButton);
            }
        }
    }
    /**
     * Crea el panel de título.
     */
    private JPanel createTitlePanel(String title) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(60, 60, 70));
        panel.add(titleLabel);
        return panel;
    }

    /**
     * Crea el panel de parámetros basado en los argumentos definidos.
     */
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(argumentDefinitions.size(), 2, 10, 10));
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        for (Map.Entry<String, Class<?>> entry : argumentDefinitions.entrySet()) {
            JLabel label = new JLabel(entry.getKey() + " (" + entry.getValue().getSimpleName() + "):");
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            JTextField textField = new JTextField();
            panel.add(label);
            panel.add(textField);
            inputFields.put(entry.getKey(), textField);
        }
        return panel;
    }

    /**
     * Crea el panel de resultado.
     */
    private JPanel createResultPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(BorderFactory.createTitledBorder("Resultado"));
        ImageIcon imageIcon = new ImageIcon(imageMatrix.toBufferedImage());
        JLabel imageLabel = new JLabel(imageIcon);
        panel.add(imageLabel);
        return panel;
    }

    /**
     * Crea un botón estilizado.
     */

    private JButton styleOfButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(baseColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
            }
        });
        return button;
    }

    private JButton createStyledButton(String text, OperationFunction action, Color baseColor) {
        styleOfButton(text, baseColor);
        JButton button = styleOfButton(text, baseColor);
        button.addActionListener(_ -> performOperation(action));
        return button;
    }

    private JButton createStyledButton(String text, Runnable action, Color baseColor) {
        styleOfButton(text, baseColor);
        JButton button = styleOfButton(text, baseColor);
        button.addActionListener(_ -> action.run());
        return button;
    }

    /**
     * Método que recupera los parámetros, ejecuta la operación y actualiza la interfaz.
     */
    private void performOperation(OperationFunction operation) {
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        for (Map.Entry<String, JTextField> entry : inputFields.entrySet()) {
            String key = entry.getKey();
            String valueStr = entry.getValue().getText();
            Class<?> type = argumentDefinitions.get(key);
            Object value;
            try {
                if (type == Integer.class) {
                    value = Integer.parseInt(valueStr);
                } else if (type == Double.class) {
                    value = Double.parseDouble(valueStr);
                } else if (type == Float.class) {
                    value = Float.parseFloat(valueStr);
                } else if (type == Boolean.class) {
                    value = Boolean.parseBoolean(valueStr);
                } else {
                    value = valueStr;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Error: el valor para \"" + key + "\" no es un " + type.getSimpleName(),
                        "Error de conversión",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            parameters.put(key, value);
        }

        try {
            // Se ejecuta la operación y se obtiene el nuevo panel de resultado.
            Result miResult = operation.apply(imageMatrix, parameters);

            // Actualizar la imagen
            imageMatrix = miResult.getImageMatrix();

            // Actualizar el contenido del scroll pane con el nuevo panel
            resultScrollPane.setViewportView(miResult.getPanel());
            mainPanel.revalidate();
            mainPanel.repaint();
            System.out.println("Panel de resultado actualizado");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al ejecutar la operación: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Guarda la imagen resultado en un archivo.
     */
    private void saveResult() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Imagen");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Imágenes (*.jpg, *.jpeg, *.png, *.bmp)", "jpg", "jpeg", "png", "bmp"));

        int res = fileChooser.showSaveDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            try {
                JPanel currentResultPanel = (JPanel) resultScrollPane.getViewport().getView();
                BufferedImage toSave = panelToBufferedImage(currentResultPanel);
                File file = fileChooser.getSelectedFile();
                String format = "png";
                String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1);
                if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")) {
                    format = "JPEG";
                } else if (extension.equalsIgnoreCase("bmp")) {
                    format = "BMP";
                }
                javax.imageio.ImageIO.write(toSave, format, file);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar la imagen: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private BufferedImage panelToBufferedImage(JPanel panel) {
        int w = panel.getWidth();
        int h = panel.getHeight();
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        panel.paint(g);
        g.dispose();
        return image;
    }
}

